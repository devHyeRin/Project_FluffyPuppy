document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const signupBtn = document.getElementById('signupBtn');

    signupBtn.addEventListener('click', () => {
        window.location.href = '/members/new';
    });

    loginForm.addEventListener('submit', (e) => {
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();

        if (!email || !password) {
            e.preventDefault();
            alert('이메일과 비밀번호를 모두 입력해주세요.');
        }
    });
});
