document.addEventListener("DOMContentLoaded", () => {
    OrderUI.init();
});

const OrderUI = {
    init() {
        this.formatPrices();
    },

    formatPrices() {
        const priceElements = document.querySelectorAll(".item-price");
        const formatter = new Intl.NumberFormat('ko-KR');

        priceElements.forEach(el => {
            const rawPrice = el.textContent.replace(/[^\d]/g, "");
            const price = parseInt(rawPrice);

            if (!isNaN(price)) {
                el.textContent = `${formatter.format(price)}원`;
            }
        });
    }
};

/* 주문 취소 */
async function cancelOrder(orderId) {
    const tokenMeta = document.querySelector("meta[name='_csrf']");
    const headerMeta = document.querySelector("meta[name='_csrf_header']");

    if (!tokenMeta || !headerMeta) {
        console.error("CSRF 메타 태그가 누락되었습니다.");
        return;
    }

    const token = tokenMeta.getAttribute("content");
    const header = headerMeta.getAttribute("content");

    if (!confirm("정말로 주문을 취소하시겠습니까?")) return;

    const url = `/order/${orderId}/cancel`;

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            }
        });

        if (response.ok) {
            alert("주문이 정상적으로 취소되었습니다.");
            location.reload();
        } else if (response.status === 401) {
            alert("로그인 후 이용 가능합니다.");
            location.href = '/members/login';
        } else {
            const errorMessage = await response.text();
            alert(errorMessage || "주문 취소 중 오류가 발생했습니다.");
        }
    } catch (error) {
        console.error("Network Error:", error);
        alert("네트워크 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }
}