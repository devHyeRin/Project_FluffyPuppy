document.addEventListener("DOMContentLoaded", () => {
    const hamburgerBtn = document.querySelector(".hamburger-btn");
    const menu = document.querySelector(".headerCategory");
    const cartBadges = document.querySelectorAll("#cart-count, #mobile-cart-count");

    const isLoggedIn = document.querySelector(".cart-wrapper") !== null;

    initMenu();

    if (isLoggedIn) {
        updateCartCount();
    }

    // [메뉴 함수]
    function initMenu() {
        if (!hamburgerBtn || !menu) return;

        hamburgerBtn.addEventListener("click", (e) => {
            e.stopPropagation();
            const isOpen = menu.classList.toggle("open");
            document.body.style.overflow = isOpen ? "hidden" : "";
            hamburgerBtn.setAttribute("aria-expanded", isOpen);
        });

        document.addEventListener("click", (e) => {
            if (menu.classList.contains("open") && !menu.contains(e.target) && !hamburgerBtn.contains(e.target)) {
                closeMenu();
            }
        });
    }

    function closeMenu() {
        menu.classList.remove("open");
        document.body.style.overflow = "";
        hamburgerBtn.setAttribute("aria-expanded", "false");
    }

    // [장바구니 함수]
    async function updateCartCount() {

        if (!isLoggedIn) return;

        try {
            const response = await fetch('/cart/count');
            if (response.status === 401) return;

            if (response.ok) {
                const count = await response.json();

                cartBadges.forEach(badge => {
                    badge.textContent = count;
                    badge.style.display = count > 0 ? "flex" : "none";
                });
            }
        } catch (error) {
            console.error("장바구니 수량을 가져오는데 실패했습니다:", error);
        }
    }

    window.refreshCartCount = updateCartCount;
});