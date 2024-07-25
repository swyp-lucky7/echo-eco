document.addEventListener("DOMContentLoaded", (event) => {
    detailHelper.init();
});

const detailHelper = {
    init() {
        detailHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#backBtn').addEventListener('click', () => {
            location.href = "/admin/gifticon";
        });
    },
}