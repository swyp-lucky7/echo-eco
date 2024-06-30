document.addEventListener("DOMContentLoaded", (event) => {
    createHelper.init();
});

const createHelper = {
    id: -1,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/character\/create\/(\d+)/);
        if (match && match[1]) {
            createHelper.id = parseInt(match[1]);
        }
        createHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#saveBtn').addEventListener('click', () => {
            $.ajax({
                type: "POST",
                url: "/admin/character/create",
                dataType: "json",
                data: JSON.stringify(createHelper.getParam()),
                contentType: 'application/json; charset=utf-8',
                success: function(res) {
                    alert("성공적으로 생성되었습니다.");
                    location.href = "/admin/character";
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });
    },

    getParam() {
        let params = {
            "name": document.querySelector('#characterName').value,
            "type": document.querySelector('#characterType').value,
            "description": document.querySelector('#descriptionInput').value,
            "maxLevel": document.querySelector('#maxLevel').value,
        };
        if (createHelper.id !== -1) {
            params['id'] = createHelper.id;
        }
        return params;
    },
}