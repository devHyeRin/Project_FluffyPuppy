document.addEventListener("DOMContentLoaded", () => {
    const hamburgerBtn = document.querySelector(".hamburger-btn");
    const headerCategory = document.querySelector(".headerCategory");

    if (!hamburgerBtn || !headerCategory) return;

    hamburgerBtn.addEventListener("click", () => {
        headerCategory.classList.toggle("open");
    });
});
