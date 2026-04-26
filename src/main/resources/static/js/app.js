/**
 * App JavaScript Module
 *
 * Main application functionality
 */

// Settings state
let settings = {
    colorTheme: 'meihei',
    codeStyle: 'mac',
    themeSystem: 'default',
    fontFamily: 'system-default',
    fontSize: 16,
    lineHeight: 1.6,
    letterSpacing: 0
};

// Theme colors
const themeColors = {
    meihei: { primary: '#333333', light: '#666666', dark: '#1a1a1a' },
    github: { primary: '#0366d6', light: '#c8e1ff', dark: '#0056b3' },
    wechat: { primary: '#07c160', light: '#c3f7c4', dark: '#05a050' },
    vuepress: { primary: '#3eaf7c', light: '#c8f0d4', dark: '#2d8f60' },
    cobalt: { primary: '#0888ff', light: '#b8d4ff', dark: '#0666cc' },
    marry: { primary: '#fa5151', light: '#ffc1c1', dark: '#d32f2f' }
};

// Apply theme
function applyTheme(themeId) {
    settings.colorTheme = themeId;
    const theme = themeColors[themeId] || themeColors.meihei;

    document.documentElement.style.setProperty('--theme-primary', theme.primary);
    document.documentElement.style.setProperty('--theme-primary-light', theme.light);
    document.documentElement.style.setProperty('--theme-primary-dark', theme.dark);

    // Update active button
    document.querySelectorAll('.theme-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.theme === themeId);
    });

    saveSettings();
}

// Apply font size
function applyFontSize(size) {
    settings.fontSize = size;
    document.documentElement.style.setProperty('--font-size', size + 'px');
    document.documentElement.style.setProperty('--theme-font-size', size + 'px');
    const display = document.getElementById('font-size-display');
    if (display) display.textContent = size + 'px';
    saveSettings();
}

// Apply line height
function applyLineHeight(height) {
    settings.lineHeight = height;
    document.documentElement.style.setProperty('--line-height', height);
    document.documentElement.style.setProperty('--theme-line-height', height);
    const display = document.getElementById('line-height-display');
    if (display) display.textContent = height;
    saveSettings();
}

// Load settings
async function loadSettings() {
    const token = localStorage.getItem('auth-token');

    if (token) {
        try {
            const response = await fetch('/api/settings', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const data = await response.json();
                settings = { ...settings, ...data };
                applyAllSettings();
            }
        } catch (error) {
            console.error('Load settings failed:', error);
        }
    } else {
        // Load from localStorage
        const saved = localStorage.getItem('mdeditor-settings');
        if (saved) {
            settings = { ...settings, ...JSON.parse(saved) };
        }
        applyAllSettings();
    }
}

// Save settings
async function saveSettings() {
    const token = localStorage.getItem('auth-token');

    localStorage.setItem('mdeditor-settings', JSON.stringify(settings));

    if (token) {
        try {
            await fetch('/api/settings', {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(settings)
            });
        } catch (error) {
            console.error('Save settings failed:', error);
        }
    }
}

// Apply all settings
function applyAllSettings() {
    applyTheme(settings.colorTheme);
    applyFontSize(settings.fontSize);
    applyLineHeight(settings.lineHeight);
}

// Open settings panel
function openSettings() {
    const panel = document.getElementById('settings-panel');
    if (panel) {
        panel.style.display = 'flex';
    }
}

// Close settings panel
function closeSettings() {
    const panel = document.getElementById('settings-panel');
    if (panel) {
        panel.style.display = 'none';
    }
}

// Copy content
async function copyContent(format) {
    const content = window.EditorModule?.getContent();
    if (!content) {
        showNotification('请先编辑内容', 'warning');
        return;
    }

    try {
        const response = await fetch('/api/markdown/parse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content,
                forSocial: format === 'social',
                colorTheme: settings.colorTheme
            })
        });

        if (response.ok) {
            const data = await response.json();
            const textToCopy = format === 'social' ? data.socialHtml : data.html;

            await navigator.clipboard.writeText(textToCopy);
            showNotification('已复制到剪贴板', 'success');
        }
    } catch (error) {
        showNotification('复制失败', 'error');
    }
}

// Export content
async function exportContent(format) {
    const content = window.EditorModule?.getContent();
    if (!content) {
        showNotification('请先编辑内容', 'warning');
        return;
    }

    try {
        const response = await fetch(`/api/export/${format}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content,
                colorTheme: settings.colorTheme
            })
        });

        if (response.ok) {
            const blob = await response.blob();
            const filename = response.headers.get('Content-Disposition')?.match(/filename=(.+)/)?.[1] || `export.${format}`;

            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = filename;
            a.click();
            URL.revokeObjectURL(url);

            showNotification('导出成功', 'success');
        }
    } catch (error) {
        showNotification('导出失败', 'error');
    }
}

// Show notification
function showNotification(message, type) {
    const container = document.getElementById('notifications');
    if (!container) {
        const newContainer = document.createElement('div');
        newContainer.id = 'notifications';
        newContainer.className = 'notification-container';
        document.body.appendChild(newContainer);
        container = newContainer;
    }

    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    container.appendChild(notification);

    setTimeout(() => {
        notification.classList.add('slide-out');
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadSettings();

    // Settings button
    document.getElementById('settings-btn')?.addEventListener('click', openSettings);
    document.getElementById('settings-close')?.addEventListener('click', closeSettings);
    document.getElementById('settings-overlay')?.addEventListener('click', closeSettings);

    // Theme buttons
    document.querySelectorAll('.theme-btn').forEach(btn => {
        btn.addEventListener('click', () => applyTheme(btn.dataset.theme));
    });

    // Font size slider
    const fontSizeSlider = document.getElementById('font-size-slider');
    if (fontSizeSlider) {
        fontSizeSlider.value = settings.fontSize;
        fontSizeSlider.addEventListener('input', () => {
            applyFontSize(parseInt(fontSizeSlider.value, 10));
        });
    }

    // Line height slider
    const lineHeightSlider = document.getElementById('line-height-slider');
    if (lineHeightSlider) {
        lineHeightSlider.value = settings.lineHeight;
        lineHeightSlider.addEventListener('input', () => {
            applyLineHeight(parseFloat(lineHeightSlider.value));
        });
    }

    // Copy button
    document.getElementById('copy-btn')?.addEventListener('click', () => {
        const format = document.querySelector('.copy-format.active')?.dataset.format || 'social';
        copyContent(format);
    });

    // Export button
    document.getElementById('export-btn')?.addEventListener('click', () => {
        const format = document.querySelector('.export-format.active')?.dataset.format || 'pdf';
        exportContent(format);
    });
});