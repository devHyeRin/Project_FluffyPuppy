document.addEventListener("DOMContentLoaded", () => {
    const priceElements = document.querySelectorAll(".price");

    priceElements.forEach(el => {
        const price = parseInt(el.textContent.replace(/\D/g, ""));
        el.textContent = price.toLocaleString() + "원";
    });
});
