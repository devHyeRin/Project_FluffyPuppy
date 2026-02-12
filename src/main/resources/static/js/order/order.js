document.addEventListener("DOMContentLoaded", () => {
    formatPrices();
});

// 가격 포맷팅
function formatPrices() {
    const priceElements = document.querySelectorAll(".item-price");
    const formatter = new Intl.NumberFormat('ko-KR', {
        style: 'currency',
        currency: 'KRW'
    });

    priceElements.forEach(el => {
        const price = parseInt(el.textContent.replace(/[^\d]/g, ""));
        if (!isNaN(price)) {
            el.textContent = formatter.format(price).replace('₩', '') + '원';
        }
    });
}

// 주문 취소
function cancelOrder(orderId) {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    if (!confirm("주문을 취소하시겠습니까?")) return;

    const url = `/order/${orderId}/cancel`;

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify({ orderId: orderId })
    })
        .then(res => {
            if (res.ok) {
                alert("주문이 취소되었습니다.");
                location.reload();
            } else if (res.status === 401) {
                alert("로그인 후 이용해주세요.");
                location.href = '/members/login';
            } else {
                res.text().then(text => alert(text));
            }
        })
        .catch(err => console.error("Error:", err));
}