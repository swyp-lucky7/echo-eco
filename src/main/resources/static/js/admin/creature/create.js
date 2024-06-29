document.addEventListener("DOMContentLoaded", (event) => {
    createHelper.init();
});

const createHelper = {
    init() {
        createHelper.pullCreature();
        createHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#saveBtn').addEventListener('click', () => {
            $.ajax({
                type: "POST",
                url: "/admin/creature/create",
                dataType: "json",
                data: JSON.stringify(createHelper.getParam()),
                contentType: 'application/json; charset=utf-8',
                success: function(res) {
                    alert("성공적으로 생성되었습니다.");
                    location.href = "/admin/creature";
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });
    },

    getParam() {
        return {
            "name": document.querySelector('#creatureName').value,
            "type": document.querySelector('#creatureType').value,
            "description": document.querySelector('#descriptionInput').value,
            "maxLevel": document.querySelector('#maxLevel').value,
        };
    },
    pullCreature() {
        $.ajax({
            type: "get",
            url: "/admin/creature/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                document.querySelector('#beforeDrawingTableBody').innerHTML = createHelper.createModalDrawingTableRow(res.data);
                document.querySelectorAll('.modalDrawingCheckbox').forEach(checkbox => {
                    checkbox.addEventListener('change', function () {
                        document.querySelector('#modalDrawingCheckboxAll').checked = document.querySelectorAll('.modalDrawingCheckbox').length === document.querySelectorAll('.modalDrawingCheckbox:checked').length;
                    });
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    }
}