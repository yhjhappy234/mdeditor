/**
 * Documents JavaScript Module
 */

// Load documents
async function loadDocuments() {
    const gridEl = document.getElementById('documents-grid');
    const loadingEl = document.getElementById('loading');
    const emptyEl = document.getElementById('empty-state');
    const token = localStorage.getItem('auth-token');

    if (!token) {
        window.location.href = '/login';
        return;
    }

    try {
        const response = await fetch('/api/documents', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const documents = await response.json();

            loadingEl.style.display = 'none';

            if (documents.length === 0) {
                emptyEl.style.display = 'block';
                gridEl.style.display = 'none';
            } else {
                emptyEl.style.display = 'none';
                gridEl.style.display = 'grid';
                renderDocuments(documents);
            }
        } else {
            loadingEl.textContent = '加载失败';
        }
    } catch (error) {
        console.error('Load documents failed:', error);
        loadingEl.textContent = '网络错误';
    }
}

// Render documents grid
function renderDocuments(documents) {
    const gridEl = document.getElementById('documents-grid');

    gridEl.innerHTML = documents.map(doc => `
        <div class="document-card" data-id="${doc.id}">
            <div class="doc-title">${doc.title || '未命名文档'}</div>
            <div class="doc-preview">${getPreviewText(doc.content)}</div>
            <div class="doc-meta">
                <span>版本: ${doc.version}</span>
                <span>${formatDate(doc.updatedAt)}</span>
            </div>
            <div class="doc-actions">
                <button class="edit-btn" onclick="openDocument(${doc.id})">编辑</button>
                <button class="delete-btn" onclick="deleteDocument(${doc.id})">删除</button>
            </div>
        </div>
    `).join('');
}

// Get preview text
function getPreviewText(content) {
    if (!content) return '暂无内容';
    return content.substring(0, 100).replace(/[#*`]/g, '');
}

// Format date
function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString('zh-CN');
}

// Open document
function openDocument(id) {
    localStorage.setItem('current-document-id', id);
    window.location.href = '/editor?id=' + id;
}

// Delete document
async function deleteDocument(id) {
    if (!confirm('确定删除此文档？')) return;

    const token = localStorage.getItem('auth-token');

    try {
        const response = await fetch(`/api/documents/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            loadDocuments();
            showNotification('文档已删除', 'success');
        }
    } catch (error) {
        showNotification('删除失败', 'error');
    }
}

// Create new document
async function createNewDocument() {
    const token = localStorage.getItem('auth-token');

    try {
        const response = await fetch('/api/documents', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: '新文档',
                content: ''
            })
        });

        if (response.ok) {
            const doc = await response.json();
            openDocument(doc.id);
        }
    } catch (error) {
        showNotification('创建失败', 'error');
    }
}

// Show notification
function showNotification(message, type) {
    const container = document.getElementById('notifications');
    if (!container) return;

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
    loadDocuments();

    // New document buttons
    document.getElementById('btn-new')?.addEventListener('click', createNewDocument);
    document.getElementById('btn-new-empty')?.addEventListener('click', createNewDocument);

    // Logout button
    document.getElementById('btn-logout')?.addEventListener('click', () => {
        window.AuthModule.logout();
    });
});