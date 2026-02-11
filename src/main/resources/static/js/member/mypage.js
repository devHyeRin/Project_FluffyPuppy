document.addEventListener('DOMContentLoaded', () => {
    const serverMsgInput = document.getElementById('serverMsg');
    if (serverMsgInput && serverMsgInput.value.trim() !== "") {
        alert(serverMsgInput.value);
    }

    const updateForm = document.querySelector('form');

    if (updateForm) {
        updateForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const pwd = document.getElementById('password');
            const pwdConfirm = document.getElementById('confirmPassword');

            const pwdVal = pwd.value.trim();
            const pwdConfirmVal = pwdConfirm.value.trim();

            if (pwdVal.length > 0) {
                if (pwdVal.length < 8 || pwdVal.length > 16) {
                    alert("비밀번호는 8~16자 사이로 입력해주세요.");
                    pwd.focus();
                    return;
                }

                if (pwdVal !== pwdConfirmVal) {
                    alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
                    pwdConfirm.focus();
                    return;
                }
            }

            if (confirm("회원 정보를 수정하시겠습니까?")) {
                this.submit();
            }
        });
    }
});

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            document.getElementById("roadAddress").value = addr;
            const addr2 = document.getElementById("address2");
            if(addr2) addr2.focus();
        }
    }).open();
}