document.addEventListener('DOMContentLoaded', () => {
    const emailInput = document.getElementById('email');
    const checkEmailBtn = document.getElementById('checkEmailBtn');
    const sendMailBtn = document.getElementById('sendMailBtn');
    const checkCodeBtn = document.getElementById('checkCodeBtn');
    const addressBtn = document.getElementById('addressBtn');

    checkEmailBtn.addEventListener('click', checkEmail);
    sendMailBtn.addEventListener('click', sendMail);
    checkCodeBtn.addEventListener('click', checkCode);
    addressBtn.addEventListener('click', execDaumPostcode);
});
