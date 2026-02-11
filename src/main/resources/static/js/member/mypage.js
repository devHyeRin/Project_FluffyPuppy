document.addEventListener('DOMContentLoaded', () => {
    const updateForm = document.querySelector('form');

    updateForm.addEventListener('submit', (e) => {
        const pwd = document.getElementById('password');
        const pwdConfirm = document.getElementById('confirmPassword');

        const pwdVal = pwd.value.trim();
        const pwdConfirmVal = pwdConfirm.value.trim();

        if (pwdVal.length > 0 || pwdConfirmVal.length > 0) {
            if (pwdVal !== pwdConfirmVal) {
                e.preventDefault();
                alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
                pwdConfirm.focus();
                return;
            }
        }

        if (!confirm("회원 정보를 수정하시겠습니까?")) {
            e.preventDefault();
        }
    });
});

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            if (data.userSelectedType === 'R') { addr = data.roadAddress; }
            else { addr = data.jibunAddress; }
            document.getElementById("roadAddress").value = addr;
            document.getElementById("address2").focus();
        }
    }).open();
}