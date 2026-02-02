document.addEventListener("DOMContentLoaded", () => {
    const hamburgerBtn = document.querySelector(".hamburger-btn");
    const menu = document.querySelector(".headerCategory");

    if (!hamburgerBtn || !menu) return;

    // 햄버거 버튼 클릭 → 메뉴 열기/닫기
    hamburgerBtn.addEventListener("click", (e) => {
        e.stopPropagation();

        const isOpen = menu.classList.toggle("open");
        document.body.style.overflow = isOpen ? "hidden" : "";
        hamburgerBtn.setAttribute("aria-expanded", isOpen);
    });

    // 메뉴 바깥 클릭 시 닫기
    document.addEventListener("click", (e) => {
        if (
            menu.classList.contains("open") &&
            !menu.contains(e.target) &&
            !hamburgerBtn.contains(e.target)
        ) {
            menu.classList.remove("open");
            document.body.style.overflow = "";
            hamburgerBtn.setAttribute("aria-expanded", "false");
        }
    });
});
