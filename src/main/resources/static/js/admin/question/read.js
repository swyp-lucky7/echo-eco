document.addEventListener("DOMContentLoaded", (event) => {
    readHelper.init();
});

const readHelper = {
    init() {
        readHelper.pullQuestion();
        readHelper.addEvent();
    },
    addEvent() {

    },

    pullQuestion() {
        $.ajax({
            type: "get",
            url: "/admin/question/read",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                let html = '';
                let index = 0;
                for (const row of res.data) {
                    html += `
                    <tr>
                        <td>${row.head}</td>
                        <td>${row.body}</td>
                        <td>${row.answer}</td>
                        <td>
                            <div class="dropdown">
                                <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown"  aria-expanded="false">
                                    <i class="bx bx-dots-vertical-rounded"></i>
                                </button>
                                <div class="dropdown-menu">
<!--                                    <a class="dropdown-item" href="/admin/character/create/${row.id}"><i class="bx bx-edit-alt me-1"></i> 수정하기</a>-->
                                    <a class="dropdown-item" href="javascript:void(0)" onclick="readHelper.deleteQuestion(${row.id});"><i class="bx bx-trash me-1"></i> 삭제하기</a>
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

                document.querySelector('#questionTBody').innerHTML = html;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },

    deleteQuestion(id) {
        if (!confirm("정말로 삭제하시겠습니까?")) {
            return;
        }
        $.ajax({
            type: "DELETE",
            url: `/admin/question/read/{id}`,
            data: JSON.stringify({"id": id}),
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success(res) {
                alert("삭제되었습니다.");
                location.reload();
            }, error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    },
}
