/**
 * Authentication JavaScript Module
 */

// Login handler
async function handleLogin(event) {
    event.preventDefault();

    const form = event.target;
    const username = form.querySelector('#username').value;
    const password = form.querySelector('#password').value;
    const errorEl = document.getElementById('auth-error');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (response.ok && data.token) {
            localStorage.setItem('auth-token', data.token);
            localStorage.setItem('username', data.username);
            localStorage.setItem('userId', data.userId);

            // Redirect to editor
            window.location.href = '/editor';
        } else {
            showError(errorEl, data.message || '登录失败');
        }
    } catch (error) {
        showError(errorEl, '网络错误，请稍后重试');
    }
}

// Register handler
async function handleRegister(event) {
    event.preventDefault();

    const form = event.target;
    const username = form.querySelector('#username').value;
    const email = form.querySelector('#email').value;
    const password = form.querySelector('#password').value;
    const confirmPassword = form.querySelector('#confirm-password').value;
    const errorEl = document.getElementById('auth-error');

    // Validate passwords match
    if (password !== confirmPassword) {
        showError(errorEl, '两次输入的密码不一致');
        return;
    }

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, email, password })
        });

        const data = await response.json();

        if (response.ok && data.token) {
            localStorage.setItem('auth-token', data.token);
            localStorage.setItem('username', data.username);
            localStorage.setItem('userId', data.userId);

            // Redirect to editor
            window.location.href = '/editor';
        } else {
            showError(errorEl, data.message || '注册失败');
        }
    } catch (error) {
        showError(errorEl, '网络错误，请稍后重试');
    }
}

// Show error message
function showError(errorEl, message) {
    if (errorEl) {
        errorEl.textContent = message;
        errorEl.style.display = 'block';
        setTimeout(() => {
            errorEl.style.display = 'none';
        }, 5000);
    }
}

// Check authentication
function isAuthenticated() {
    const token = localStorage.getItem('auth-token');
    return !!token;
}

// Logout
function logout() {
    localStorage.removeItem('auth-token');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
    window.location.href = '/';
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }

    // Password confirmation validation
    const confirmPassword = document.getElementById('confirm-password');
    if (confirmPassword) {
        confirmPassword.addEventListener('input', () => {
            const password = document.getElementById('password').value;
            if (confirmPassword.value !== password) {
                confirmPassword.setCustomValidity('密码不匹配');
            } else {
                confirmPassword.setCustomValidity('');
            }
        });
    }
});

// Export
window.AuthModule = {
    isAuthenticated,
    logout,
    getToken: () => localStorage.getItem('auth-token'),
    getUsername: () => localStorage.getItem('username'),
    getUserId: () => localStorage.getItem('userId')
};