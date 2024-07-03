document.addEventListener("DOMContentLoaded", (event) => {
    readHelper.init();
});

const readHelper = {
    init() {
        readHelper.pullCharacter();
        readHelper.addEvent();
    },
    addEvent() {

    },

    pullCharacter() {
        $.ajax({
            type: "get",
            url: "/admin/character/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                let html = '';
                let index = 0;
                for (const row of res.data) {
                    html += `
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${row.name}</strong></td>
                        <td><span class="badge bg-label-primary me-1">${getConvertType(row.type)}</span></td>
                        <td>${row.maxLevel}</td>
                        <td>
                            <div class="dropdown">
                                <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"  aria-expanded="false">
                                    <i class="bx bx-dots-vertical-rounded"></i>
                                </button>
                                <div class="dropdown-menu">
                                    <a class="dropdown-item" href="/admin/character/${row.id}/detail"><i class="bx bx-edit-alt me-1"></i> Detail</a>
                                    <a class="dropdown-item" href="/admin/character/create/${row.id}"><i class="bx bx-edit-alt me-1"></i> 수정하기</a>
                                    <a class="dropdown-item" href="/admin/character/delete/${row.id}"><i class="bx bx-trash me-1"></i> 삭제하기</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    `;
                    index++;
                }
                // 드롭다운 문제로 추가로 생성
                for (let i = 0; i < 4; i++) {
                    html += `<tr>
                        <td><i class="fab fa-angular fa-lg me-3"></i></td>
                        <td></td>
                        <td></td>
                        <td>
                            <div class="dropdown-menu">
                                
                            </div>
                        </tr>`;
                    index++;
                }

                document.querySelector('#characterTBody').innerHTML = html;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });

        function getConvertType(type) {
            if (type === "ANIMAL") {
                return "동물";
            } else if (type === "PLANT") {
                return "식물"
            }
            return "";
        }
    }
}