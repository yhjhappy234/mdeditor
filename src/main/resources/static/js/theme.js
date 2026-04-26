// Theme Management
(function() {
    'use strict';

    const ThemeManager = {
        themes: {
            meihei: { primary: '#333333', primaryLight: '#666666' },
            github: { primary: '#24292e', primaryLight: '#586069' },
            wechat: { primary: '#07c160', primaryLight: '#38c27d' },
            vuepress: { primary: '#3eaf7c', primaryLight: '#6bbd8b' },
            cobalt: { primary: '#0047ab', primaryLight: '#3575c5' },
            marry: { primary: '#e74c3c', primaryLight: '#f1978e' }
        },

        currentTheme: 'meihei',

        init() {
            this.loadTheme();
            this.bindEvents();
        },

        loadTheme() {
            const saved = localStorage.getItem('md-theme');
            if (saved && this.themes[saved]) {
                this.currentTheme = saved;
            }
            this.applyTheme(this.currentTheme);
        },

        applyTheme(name) {
            const theme = this.themes[name];
            if (!theme) return;

            document.documentElement.style.setProperty('--theme-primary', theme.primary);
            document.documentElement.style.setProperty('--theme-primary-light', theme.primaryLight);
            this.currentTheme = name;
            localStorage.setItem('md-theme', name);
        },

        bindEvents() {
            const themeButtons = document.querySelectorAll('.theme-btn[data-theme]');
            themeButtons.forEach(btn => {
                btn.addEventListener('click', () => {
                    const themeName = btn.dataset.theme;
                    this.applyTheme(themeName);
                    this.updateActiveButton(btn);
                });
            });
        },

        updateActiveButton(activeBtn) {
            document.querySelectorAll('.theme-btn[data-theme]').forEach(btn => {
                btn.classList.remove('active');
            });
            activeBtn.classList.add('active');
        }
    };

    // Font Size Manager
    const FontSizeManager = {
        currentSize: 16,

        init() {
            this.loadSize();
            this.bindEvents();
        },

        loadSize() {
            const saved = localStorage.getItem('md-font-size');
            if (saved) {
                this.currentSize = parseInt(saved, 10);
            }
            this.applySize(this.currentSize);
        },

        applySize(size) {
            document.documentElement.style.setProperty('--theme-font-size', size + 'px');
            this.currentSize = size;
            localStorage.setItem('md-font-size', size);

            const display = document.getElementById('font-size-display');
            if (display) display.textContent = size + 'px';

            const slider = document.getElementById('font-size-slider');
            if (slider) slider.value = size;
        },

        bindEvents() {
            const slider = document.getElementById('font-size-slider');
            if (slider) {
                slider.addEventListener('input', (e) => {
                    this.applySize(parseInt(e.target.value, 10));
                });
            }
        }
    };

    // Line Height Manager
    const LineHeightManager = {
        currentHeight: 1.6,

        init() {
            this.loadHeight();
            this.bindEvents();
        },

        loadHeight() {
            const saved = localStorage.getItem('md-line-height');
            if (saved) {
                this.currentHeight = parseFloat(saved);
            }
            this.applyHeight(this.currentHeight);
        },

        applyHeight(height) {
            document.documentElement.style.setProperty('--theme-line-height', height);
            this.currentHeight = height;
            localStorage.setItem('md-line-height', height);

            const display = document.getElementById('line-height-display');
            if (display) display.textContent = height;

            const slider = document.getElementById('line-height-slider');
            if (slider) slider.value = height;
        },

        bindEvents() {
            const slider = document.getElementById('line-height-slider');
            if (slider) {
                slider.addEventListener('input', (e) => {
                    this.applyHeight(parseFloat(e.target.value));
                });
            }
        }
    };

    // Initialize on DOM ready
    document.addEventListener('DOMContentLoaded', () => {
        ThemeManager.init();
        FontSizeManager.init();
        LineHeightManager.init();
    });

    // Export for global access
    window.ThemeManager = ThemeManager;
    window.FontSizeManager = FontSizeManager;
    window.LineHeightManager = LineHeightManager;
})();