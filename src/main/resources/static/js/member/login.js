document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const signupBtn = document.getElementById('signupBtn');
    const errorMessage = document.querySelector('.error-message');

    signupBtn.addEventListener('click', () => {
        window.location.href = '/members/new';
    });

    loginForm.addEventListener('submit', (e) => {
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();

        // 기존 에러 숨김
        errorMessage.classList.add('hidden');

        let message = '';

        if (!email && !password) {
            message = '이메일과 비밀번호를 입력해주세요.';
        } else if (!email) {
            message = '이메일을 입력해주세요.';
        } else if (!password) {
            message = '비밀번호를 입력해주세요.';
        }

        if (message) {
            e.preventDefault();
            errorMessage.textContent = message;
            errorMessage.classList.remove('hidden');
        }
    });
});
