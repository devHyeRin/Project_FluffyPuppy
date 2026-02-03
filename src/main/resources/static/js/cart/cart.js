document.addEventListener("DOMContentLoaded", () => {
    bindEvents();
    formatAllPrices();
    getOrderTotalPrice();
});

/* =========================
   Event Binding
   ========================= */
function bindEvents() {
    document.querySelectorAll("input[name=cartChkBox]").forEach(cb => {
        cb.addEventListener("change", getOrderTotalPrice);
    });

    document.querySelectorAll(".count-input").forEach(input => {
        input.addEventListener("change", e => {
            const id = e.target.id.split("_")[1];
            changeCount(e.target, id);
        });
    });

    const checkAllBox = document.getElementById("checkall");
    if (checkAllBox) {
        checkAllBox.addEventListener("change", (e) => {
            const isChecked = e.target.checked;
            document.querySelectorAll("input[name=cartChkBox]").forEach(cb => {
                cb.checked = isChecked;
            });
            getOrderTotalPrice();
        });
    }
}

/* =========================
   Price Format
   ========================= */
function formatPriceWithCommas(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatAllPrices() {
    document.querySelectorAll(".item-price").forEach(el => {
        const price = parseInt(el.textContent.replace(/[^\d]/g, ""));
        el.textContent = formatPriceWithCommas(price) + "원";
    });
}

/* =========================
   Total Price
   ========================= */
function getOrderTotalPrice() {
    let total = 0;

    document.querySelectorAll("input[name=cartChkBox]:checked").forEach(cb => {
        const id = cb.value;
        const price = document.querySelector(`#price_${id}`).dataset.price;
        const count = document.querySelector(`#count_${id}`).value;
        total += parseInt(price) * parseInt(count);
    });

    document.getElementById("orderTotalPrice").textContent = formatPriceWithCommas(total) + "원";
}

/* =========================
   Quantity Change + AJAX
   ========================= */
function changeCount(input, cartItemId) {
    const count = input.value;
    if (count < 1) {
        alert("최소 수량은 1개입니다.");
        input.value = 1;
        return;
    }

    const price = document.querySelector(`#price_${cartItemId}`).dataset.price;
    document.querySelector(`#totalPrice_${cartItemId}`).textContent = formatPriceWithCommas(price * count) + "원";

    getOrderTotalPrice();
    updateCartItemCount(cartItemId, count);
}

function updateCartItemCount(cartItemId, count) {
    const token = document.querySelector("meta[name=_csrf]").content;
    const header = document.querySelector("meta[name=_csrf_header]").content;

    $.ajax({
        url: `/cartItem/${cartItemId}?count=${count}`,
        type: "PATCH",
        beforeSend: xhr => xhr.setRequestHeader(header, token),
        success: () => console.log("수량 업데이트 완료"),
        error: handleAjaxError
    });
}

/* =========================
   Delete
   ========================= */
function deleteSelected() {
    const selected = [...document.querySelectorAll("input[name=cartChkBox]:checked")];

    if (selected.length === 0) {
        alert("삭제할 상품을 선택해주세요.");
        return;
    }

    if (!confirm("선택한 상품을 삭제하시겠습니까?")) return;

    const promises = selected.map(cb => deleteCartItemRequest(cb.value));

    Promise.all(promises).then(() => {
        alert("삭제되었습니다.");
        location.reload();
    });
}

function deleteCartItemRequest(cartItemId) {
    const token = document.querySelector("meta[name=_csrf]").content;
    const header = document.querySelector("meta[name=_csrf_header]").content;

    return $.ajax({
        url: `/cartItem/${cartItemId}`,
        type: "DELETE",
        beforeSend: xhr => xhr.setRequestHeader(header, token),
        error: handleAjaxError
    });
}

/* =========================
   Order
   ========================= */
function orders() {
    const token = document.querySelector("meta[name=_csrf]").content;
    const header = document.querySelector("meta[name=_csrf_header]").content;

    const cartOrderDtoList = [...document.querySelectorAll("input[name=cartChkBox]:checked")]
        .map(cb => ({ cartItemId: cb.value }));

    if (cartOrderDtoList.length === 0) {
        alert("주문할 상품을 선택해주세요.");
        return;
    }

    $.ajax({
        url: "/cart/orders",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(cartOrderDtoList),
        beforeSend: xhr => xhr.setRequestHeader(header, token),
        success: (result) => {
            alert("주문이 완료되었습니다.");
            location.href = "/orders";
        },
        error: handleAjaxError
    });
}

/* =========================
   Error
   ========================= */
function handleAjaxError(jqXHR) {
    if (jqXHR.status === 401) {
        alert("로그인 후 이용해주세요.");
        location.href = "/members/login";
    } else {
        alert(jqXHR.responseText || "오류가 발생했습니다.");
    }
}