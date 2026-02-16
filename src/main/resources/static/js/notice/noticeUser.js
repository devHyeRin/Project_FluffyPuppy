document.addEventListener("DOMContentLoaded", () => {
    NoticeUI.init();
});

const NoticeUI = {
    init() {
        this.bindEvents();
    },

    bindEvents() {
        const searchBtn = document.querySelector("#searchBtn");
        const paginationLinks = document.querySelectorAll(".page-link[data-page]");

        // 검색 버튼 클릭
        if (searchBtn) {
            searchBtn.addEventListener("click", (e) => {
                e.preventDefault();
                this.movePage(0);
            });
        }

        // 페이지 번호 클릭
        paginationLinks.forEach(link => {
            link.addEventListener("click", (e) => {
                e.preventDefault();
                const page = e.currentTarget.getAttribute("data-page");
                this.movePage(page);
            });
        });
    },

    movePage(page) {
        const searchBy = document.querySelector("#searchBy").value;
        const searchQuery = document.querySelector("#searchQuery").value;

        location.href = `/notices/${page}?searchBy=${searchBy}&searchQuery=${searchQuery}`;
    }
};