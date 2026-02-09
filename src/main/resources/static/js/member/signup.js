document.addEventListener('DOMContentLoaded', () => {
    const emailInput = document.getElementById('username');
    const emailCheckBtn = document.getElementById('usernameCheckBtn');
    const emailMsg = document.getElementById('usernameMsg');

    const sendMailBtn = document.getElementById('sendMailBtn');
    const codeInput = document.getElementById('codecheck');
    const codeCheckBtn = document.getElementById('codeCheckBtn');
    const codeMsg = document.getElementById('codeMsg');

    const addressBtn = document.getElementById('addressBtn');
    const form = document.querySelector('form');

    let isEmailChecked = false;
    let isMailSent = false;
    let isCodeVerified = false;

    /* =====================
       이메일 중복 확인
    ===================== */
    emailCheckBtn.addEventListener('click', async () => {
        const email = emailInput.value.trim();

        if (!email) {
            showMessage(emailMsg, '이메일을 입력해주세요.', true);
            return;
        }

        try {
            const response = await fetch(`/members/checkUsername?username=${email}`);
            const result = await response.json();

            if (result.result === true) {
                showMessage(emailMsg, '사용 가능한 이메일입니다.', false);
                isEmailChecked = true;
                sendMailBtn.disabled = false;
            } else {
                showMessage(emailMsg, '이미 사용 중인 이메일입니다.', true);
                isEmailChecked = false;
                sendMailBtn.disabled = true;
                emailInput.focus();
            }
        } catch (e) {
            showMessage(emailMsg, '이메일 확인 중 오류가 발생했습니다.', true);
        }
    });

    sendMailBtn.addEventListener('click', async () => {
        if (!isEmailChecked) {
            alert("이메일 중복 확인을 먼저 해주세요!");
            return;
        }

        try {
            const email = emailInput.value.trim();
            const response = await fetch(`/members/${email}/emailConfirm`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    ...getCsrfHeaders()
                },
                body: JSON.stringify({ email })
            });

            const data = await response.json();

            if (response.ok) {
                showMessage(emailMsg, data.message, false);
                isMailSent = true;
            } else {
                showMessage(emailMsg, data.message || '인증 메일 발송에 실패했습니다.', true);
            }
        } catch (e) {
            console.error(e);
            showMessage(emailMsg, '메일 발송 중 오류가 발생했습니다.', true);
        }
    });

    /* =====================
       이메일 인증 코드 확인
    ===================== */
    codeCheckBtn.addEventListener('click', async () => {
        const code = codeInput.value.trim();

        if (!isMailSent) {
            showMessage(codeMsg, '먼저 인증 메일을 발송해주세요.', true);
            return;
        }

        try {
            const response = await fetch(`/members/${code}/codeCheck`, {
                method: 'POST',
                headers: getCsrfHeaders()
            });

            const data = await response.json();

            if (response.ok) {
                showMessage(codeMsg, data.message, false);
                isCodeVerified = true;
            } else {
                showMessage(codeMsg, data.message, true);
                isCodeVerified = false;
            }
        } catch (e) {
            console.error(e);
            showMessage(codeMsg, '인증 확인 중 오류가 발생했습니다.', true);
        }
    });

    /* =====================
       주소 검색
    ===================== */
    addressBtn.addEventListener('click', execDaumPostcode);

    /* =====================
       폼 제출 검증
    ===================== */
    form.addEventListener('submit', (e) => {
        if (!isEmailChecked) {
            e.preventDefault();
            showMessage(emailMsg, '이메일 중복 확인을 해주세요.', true);
            return;
        }

        if (!isCodeVerified) {
            e.preventDefault();
            showMessage(codeMsg, '이메일 인증을 완료해주세요.', true);
        }
    });
});

/* =====================
   공통 유틸
===================== */
function showMessage(target, message, isError) {
    target.textContent = message;
    target.style.color = isError ? '#e54848' : '#28a745';
}

function getCsrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    return { [header]: token };
}

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('roadAddress').value = data.roadAddress;
        }
    }).open();
}
