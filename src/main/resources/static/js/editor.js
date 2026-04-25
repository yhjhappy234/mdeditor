/**
 * Editor JavaScript Module
 *
 * Handles CodeMirror initialization and editor functionality
 */

// Global state
let editor = null;
let currentContent = '';
let syncScrollEnabled = true;
let currentViewMode = 'both';
let currentViewportMode = 'desktop';

// Initialize CodeMirror
function initEditor() {
    const textarea = document.getElementById('editor');
    if (!textarea) return;

    editor = CodeMirror.fromTextArea(textarea, {
        mode: 'markdown',
        theme: 'idea',
        lineNumbers: false,
        autofocus: true,
        lineWrapping: true,
        indentUnit: 2,
        tabSize: 2,
        indentWithTabs: false,
        extraKeys: {
            'Enter': 'newlineAndIndentContinueMarkdownList',
            'Ctrl-B': toggleBold,
            'Ctrl-I': toggleItalic,
            'Ctrl-K': insertLink
        }
    });

    // Handle content changes
    editor.on('change', debounce(handleContentChange, 300));

    // Handle scroll
    editor.on('scroll', handleEditorScroll);

    // Load initial content
    const savedContent = localStorage.getItem('mdeditor-content');
    if (savedContent) {
        editor.setValue(savedContent);
    }

    updateStats();
}

// Handle content change
function handleContentChange() {
    currentContent = editor.getValue();
    localStorage.setItem('mdeditor-content', currentContent);
    updatePreview();
    updateStats();
}

// Update preview
async function updatePreview() {
    const previewEl = document.getElementById('preview');
    if (!previewEl) return;

    try {
        const response = await fetch('/api/markdown/preview', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                content: currentContent,
                forSocial: false
            })
        });

        if (response.ok) {
            const data = await response.json();
            previewEl.innerHTML = data.html;
        }
    } catch (error) {
        console.error('Preview update failed:', error);
    }
}

// Update statistics
function updateStats() {
    const content = editor.getValue();

    document.getElementById('char-count').textContent = content.length + ' 字符';
    document.getElementById('line-count').textContent = (content.split('\n').length - 1) + ' 行';

    const wordCount = countWords(content);
    document.getElementById('word-count').textContent = wordCount + ' 词';

    const readTime = Math.max(1, Math.ceil(wordCount / 200));
    document.getElementById('read-time').textContent = readTime + ' 分钟';
}

// Count words
function countWords(text) {
    const chineseChars = text.match(/[一-龥]/g);
    const chineseCount = chineseChars ? chineseChars.length : 0;

    const englishWords = text.match(/[a-zA-Z]+/g);
    const englishCount = englishWords ? englishWords.length : 0;

    return chineseCount + englishCount;
}

// Handle scroll sync
function handleEditorScroll() {
    if (!syncScrollEnabled) return;

    const previewEl = document.getElementById('preview');
    if (!previewEl) return;

    const scrollInfo = editor.getScrollInfo();
    const scrollPercentage = scrollInfo.top / (scrollInfo.height - scrollInfo.clientHeight);
    const previewMaxScroll = previewEl.scrollHeight - previewEl.clientHeight;
    previewEl.scrollTop = scrollPercentage * previewMaxScroll;
}

// Toggle bold
function toggleBold(cm) {
    const selection = cm.getSelection();
    if (selection.startsWith('**') && selection.endsWith('**')) {
        cm.replaceSelection(selection.slice(2, -2));
    } else {
        cm.replaceSelection('**' + selection + '**');
    }
}

// Toggle italic
function toggleItalic(cm) {
    const selection = cm.getSelection();
    if (selection.startsWith('*') && selection.endsWith('*') && !selection.startsWith('**')) {
        cm.replaceSelection(selection.slice(1, -1));
    } else {
        cm.replaceSelection('*' + selection + '*');
    }
}

// Insert link
function insertLink(cm) {
    const selection = cm.getSelection();
    cm.replaceSelection('[链接文字](' + (selection || 'url') + ')');
}

// Debounce helper
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// View mode switching
function switchViewMode(mode) {
    currentViewMode = mode;
    const mainEl = document.querySelector('.app-main');
    if (mainEl) {
        mainEl.className = 'app-main view-' + mode;
    }

    // Update buttons
    document.querySelectorAll('.view-toggle-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.view === mode);
    });
}

// Viewport mode switching
function switchViewportMode(mode) {
    currentViewportMode = mode;
    const containerEl = document.querySelector('.preview-container');
    if (containerEl) {
        containerEl.className = 'preview-container viewport-' + mode;
    }

    // Update buttons
    document.querySelectorAll('.mode-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.mode === mode);
    });
}

// Export initialization on DOM ready
document.addEventListener('DOMContentLoaded', () => {
    initEditor();

    // View mode buttons
    document.querySelectorAll('.view-toggle-btn').forEach(btn => {
        btn.addEventListener('click', () => switchViewMode(btn.dataset.view));
    });

    // Viewport mode buttons
    document.querySelectorAll('.mode-btn').forEach(btn => {
        btn.addEventListener('click', () => switchViewportMode(btn.dataset.mode));
    });

    // Sync scroll toggle
    const syncScrollCheckbox = document.getElementById('sync-scroll');
    if (syncScrollCheckbox) {
        syncScrollEnabled = syncScrollCheckbox.checked;
        syncScrollCheckbox.addEventListener('change', () => {
            syncScrollEnabled = syncScrollCheckbox.checked;
        });
    }
});

// Export functions for global use
window.EditorModule = {
    getContent: () => editor?.getValue() || '',
    setContent: (content) => editor?.setValue(content),
    clear: () => editor?.setValue(''),
    updatePreview
};