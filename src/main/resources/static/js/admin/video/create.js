document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    init() {
        createHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#saveBtn').addEventListener('click', () => {
            const params = createHelper.getParam();
            if (!createHelper.valid(params)) {
                return;
            }
            $.ajax({
                type: "POST",
                url: "/admin/video/create",
                dataType: "json",
                data: JSON.stringify(createHelper.getParam()),
                contentType: 'application/json; charset=utf-8',
                success: function(res) {
                    alert("성공적으로 생성되었습니다.");
                    location.href = '/admin/video'
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });
    },

    getParam() {
        let params = {
            "name": document.querySelector('#name').value,
            "url": document.querySelector('#urlInput').value
        }

        return params;
    },

    valid(params) {
        if (params['name'] === '') {
            alert("이름을 입력해주세요.");
            return false;
        }
        if (params['url'] === '') {
            alert("URL을 입력해주세요.");
            return false;
        }
        return true;
    }
}