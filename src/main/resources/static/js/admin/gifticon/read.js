document.addEventListener("DOMContentLoaded", (event) => {
    readHelper.init();
});

const readHelper = {
    init() {
        readHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#isSendAll').addEventListener('click', () => {
            const checked = document.querySelector('#isSendAll').checked;
            document.querySelectorAll(".isSend").forEach(chk => chk.checked = checked);
        });

        document.querySelectorAll('.isSend').forEach(chk => {
            chk.addEventListener('click', () => {
                const checkedLength = document.querySelectorAll('.isSend:checked').length;
                document.querySelector('#isSendAll').checked = checkedLength === 2;
            });
        });

        document.querySelector('#searchBtn').addEventListener('click', () => {
            readHelper.search();
        });

        document.querySelector('#modalSendBtn').addEventListener('click', () => {
            readHelper.sendGifticon();
        });
    },

    search() {
        $.ajax({
            type: "get",
            url: "/admin/gifticon/search",
            data: readHelper.getParams(),
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success: function (res) {
                let html = '';
                let index = 0;
                for (const row of res.data) {
                    html += `
                    <tr data-row='${JSON.stringify(row)}'>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${row.email}</strong></td>
                        <td>${row.name}</td>
                        <td><span class="badge bg-label-primary me-1">${sendKoreaName(row.isSend)}</span></td>
                        <td>
                            <button class="btn btn-primary" id="detailBtn${row.id}" onclick="location.href='/admin/gifticon/${row.id}'">
                                <strong>자세히보기</strong>
                            </button>
                        </td>
                        <td>${getSendBtn(row)}</td>
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
                        <td></td>
                        </tr>`;
                    index++;
                }

                document.querySelector('#gifticonTBody').innerHTML = html;

                document.querySelectorAll('.sendBtn').forEach(btn => {
                    btn.addEventListener('click', () => {
                        let trNode = btn.parentNode.parentNode;
                        const row = JSON.parse(trNode.getAttribute('data-row'));
                        document.querySelector('#sendId').value = row.id;
                        document.querySelector('#modalEmail').value = row.email;
                        document.querySelector('#modalName').value = row.name;
                    });
                });

                function getSendBtn(row) {
                    if (row.isSend === true) {
                        return '';
                    }
                    return `
                        <button class="btn btn-primary sendBtn" id="sendBtn${row.id}" data-bs-toggle="modal" data-bs-target="#sendModalToggle">
                                <strong>보내기</strong>
                        </button>
                    `;
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });

        function sendKoreaName(isSend) {
            if (isSend === true) {
                return "완료";
            }
            return "미완료";
        }
    },

    getParams: function () {
        let params = {}
        let email = document.querySelector('#emailSearch').value;
        if (email !== '') {
            params['email'] = email;
        }
        let chkList = document.querySelectorAll('.isSend:checked');
        if (chkList.length === 1) {
            params['isSend'] = chkList.item(0).value;
        }
        return params;
    },

    sendGifticon() {
        const formData = readHelper.getSendParams();
        if (formData.get('file') === '') {
            alert("기프티콘 파일이 비워져 있습니다.");
            return;
        }
        if (!confirm("정말 보내시겠습니까?")) {
            return;
        }

        $.ajax({
            type: "POST",
            url: "/admin/gifticon/send",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            cache: false,
            success: function(res) {
                alert("성공적으로 전송되었습니다.");
                location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
            }
        });
    },

    getSendParams() {
        const formData = new FormData();
        formData.append("id", document.querySelector("#sendId").value);
        formData.append("file", document.querySelector('#modalFile').files[0]);
        return formData;
    },
}