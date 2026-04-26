// Export functionality
(function() {
    'use strict';

    const ExportManager = {
        init() {
            this.bindEvents();
        },

        bindEvents() {
            const exportBtn = document.getElementById('export-btn') || document.getElementById('btn-export');
            if (exportBtn) {
                exportBtn.addEventListener('click', (e) => {
                    e.preventDefault();
                    this.showExportOptions();
                });
            }
        },

        showExportOptions() {
            // Simple export menu - could be enhanced with dropdown
            const content = this.getEditorContent();
            if (!content) {
                this.showNotification('请先输入内容', 'warning');
                return;
            }

            // For now, directly trigger PDF export
            this.exportAsPDF(content);
        },

        getEditorContent() {
            const editor = document.getElementById('editor') || document.getElementById('markdown-content');
            if (editor && editor.value) {
                return editor.value;
            }
            // Check for CodeMirror instance
            if (window.codeMirrorEditor) {
                return window.codeMirrorEditor.getValue();
            }
            return '';
        },

        async exportAsPDF(content) {
            try {
                this.showNotification('正在生成 PDF...', 'info');

                const response = await fetch('/api/export/pdf', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        content: content,
                        colorTheme: localStorage.getItem('md-theme') || 'meihei'
                    })
                });

                if (!response.ok) {
                    throw new Error('Export failed');
                }

                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'document.pdf';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);

                this.showNotification('PDF 已下载', 'success');
            } catch (error) {
                console.error('Export error:', error);
                this.showNotification('导出失败，请重试', 'error');
            }
        },

        async exportAsImage(content) {
            try {
                this.showNotification('正在生成图片...', 'info');

                const response = await fetch('/api/export/image', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        content: content,
                        colorTheme: localStorage.getItem('md-theme') || 'meihei'
                    })
                });

                if (!response.ok) {
                    throw new Error('Export failed');
                }

                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'document.png';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);

                this.showNotification('图片已下载', 'success');
            } catch (error) {
                console.error('Export error:', error);
                this.showNotification('导出失败，请重试', 'error');
            }
        },

        showNotification(message, type) {
            const container = document.getElementById('notifications') || document.getElementById('notification-container');
            if (!container) {
                console.log(message);
                return;
            }

            const notification = document.createElement('div');
            notification.className = 'notification ' + type;
            notification.textContent = message;
            container.appendChild(notification);

            setTimeout(() => {
                notification.classList.add('slide-out');
                setTimeout(() => notification.remove(), 300);
            }, 2000);
        }
    };

    // Initialize on DOM ready
    document.addEventListener('DOMContentLoaded', () => {
        ExportManager.init();
    });

    // Export for global access
    window.ExportManager = ExportManager;
})();