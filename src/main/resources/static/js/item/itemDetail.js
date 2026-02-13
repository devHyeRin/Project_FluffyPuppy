$(document).ready(function(){
    calculateTotalPrice();

    $("#count").on("change", function(){
        calculateTotalPrice();
    });

    formatAllPrices();
});

/* 총 금액 계산 */
function calculateTotalPrice(){
    const count = $("#count").val();
    const price = $("#price").val();
    const totalPrice = price * count;
    $("#totalPrice").html(formatPriceWithCommas(totalPrice) + '원');
}

/* 장바구니 담기 (AJAX) */
function addCart(){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const url = "/cart";
    const paramData = {
        itemId : $("#itemId").val(),
        count : $("#count").val()
    };

    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json",
        data : JSON.stringify(paramData),
        beforeSend : function (xhr){
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        success : function(result){
            if(confirm("상품을 장바구니에 담았습니다. 장바구니로 이동하시겠습니까?")){
                location.href='/cart';
            } else {
                return;
            }
        },
        error : function(jqXHR){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/members/login';
            } else {
                // 서버에서 보낸 에러 메시지 출력
                alert(jqXHR.responseText || "에러가 발생했습니다.");
            }
        }
    });
}

function order(){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const url = "/order";
    const paramData = {
        itemId : $("#itemId").val(),
        count : $("#count").val()
    };

    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json",
        data : JSON.stringify(paramData),
        beforeSend : function (xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(){
            alert("주문이 완료되었습니다.");
            location.href = "/";
        },
        error : function(jqXHR){
            if(jqXHR.status == 401){
                alert("로그인 후 이용해주세요.");
                location.href="/members/login";
            } else {
                alert(jqXHR.responseText || "주문 중 오류 발생");
            }
        }
    });
}

/* 스크롤 이동 함수 */
function scrollToSection(sectionId, element) {
    // 모든 탭에서 active 클래스 제거
    $('.detail-nav li').removeClass('active');
    // 클릭된 탭에 active 클래스 추가
    $(element).addClass('active');

    $('html, body').animate({
        scrollTop: $('#' + sectionId).offset().top - 120
    }, 500);
}

/* 가격 포맷팅 */
function formatPriceWithCommas(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatAllPrices() {
    $(".item-price").each(function() {
        const price = parseInt($(this).text().replace(/[^\d]/g, ""));
        if(!isNaN(price)) {
            $(this).text(formatPriceWithCommas(price) + '원');
        }
    });
}