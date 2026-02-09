$(document).ready(function(){
    var err = [[${errorMessage}]];
    if(err != null){
        alert(err);
    }

    $("#updateBtn").on("click",function(e){
        alert("내 정보가 수정되었습니다");
    });
});


function execDaumPostcode(){
    new daum.Postcode({
        oncomplete: function(data) {
            document.querySelector("#roadAddress").value =  data.address;
        }
    }).open();
}