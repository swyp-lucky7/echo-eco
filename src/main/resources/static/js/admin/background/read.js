document.addEventListener("DOMContentLoaded", (event) => {
    backgroundRead.init();
});

const backgroundRead = {
    init() {
        backgroundRead.search();
        backgroundRead.addEvent();
    },

    addEvent() {

    },

    search() {
        $.ajax({
            type: "get",
            url: "/admin/background/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                let html = '';
                let index = 0;
                for (const row of res.data) {
                    html += `
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${row.name}</strong></td>
                        <td><img src="${row.image}" alt="" class="d-block rounded" height="50" width="50" id="rowImage${index}"></td>
                        <td><span class="badge bg-label-primary me-1">${getEnvironmentType(row.environment)}</span></td>
                        <td>${row.level}</td>
                        <td>
                            <div class="dropdown">
                                <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"  aria-expanded="false">
                                    <i class="bx bx-dots-vertical-rounded"></i>
                                </button>
                                <div class="dropdown-menu">
                                    <a class="dropdown-item" href="/admin/background/create/${row.id}"><i class="bx bx-edit-alt me-1"></i> 수정하기</a>
                                    <a class="dropdown-item" href="javascript:void(0)" onclick="backgroundRead.deleteBackground(${row.id});"><i class="bx bx-trash me-1"></i> 삭제하기</a>
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
                        <td></td>
                        <td>
                            <div class="dropdown-menu">
                                
                            </div>
                        </tr>`;
                    index++;
                }

                document.querySelector('#backgroundTBody').innerHTML = html;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });

        function getEnvironmentType(type) {
            if (type === "CLEAN") {
                return "깨끗";
            } else if (type === "TRASH") {
                return "쓰레기"
            }
            return "";
        }
    },

    deleteBackground(id) {
        if (!confirm('정말로 삭제하시겠습니까?')) {
            return;
        }
        $.ajax({
            type: "POST",
            url: "/admin/background/delete",
            dataType: "json",
            data: JSON.stringify({"id": id}),
            contentType: 'application/json; charset=utf-8',
            success: function(res) {
                alert("성공적으로 삭제되었습니다.");
                location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },
}