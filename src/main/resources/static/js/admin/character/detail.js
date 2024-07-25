document.addEventListener("DOMContentLoaded", (event) => {
    detailHelper.init();
});

const detailHelper = {
    baseImageUrl: "/images/vendor/icons/unicons/chart.png",
    characterId: -1,
    init() {
        const url = window.location.pathname;
        const regex = /\/admin\/character\/(\d+)\/detail/;
        const match = url.match(regex);
        detailHelper.characterId = match[1];

        detailHelper.pullList();
        detailHelper.addEvent();

    },
    addEvent() {
        document.querySelector('#upload').addEventListener('change', () => {
            const fileInput = document.querySelector('#upload');
            console.log(fileInput);
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);
            $.ajax({
                type: "POST",
                url: "/admin/file/upload",
                data: formData,
                enctype: "multipart/form-data",
                processData: false,
                contentType: false,
                cache: false,
                success(res) {
                    document.querySelector('#uploadImage').src = res.data;
                },
                error(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR, textStatus, errorThrown);
                }
            });
        });
        document.querySelector('#updateUpload').addEventListener('change', () => {
            const fileInput = document.querySelector('#updateUpload');
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);
            $.ajax({
                type: "POST",
                url: "/admin/file/upload",
                data: formData,
                enctype: "multipart/form-data",
                processData: false,
                contentType: false,
                cache: false,
                success(res) {
                    document.querySelector('#updateUploadImage').src = res.data;
                },
                error(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR, textStatus, errorThrown);
                }
            });
        });

        document.querySelector('#modalSaveBtn').addEventListener('click', (event) => {
            const src = document.querySelector('#uploadImage').src;
            const level = document.querySelector('#level').value;
            const environment = document.querySelector('#environment').value;
            detailHelper.save('', src, level, environment);
        });

        document.querySelector('#backBtn').addEventListener('click', () => {
            location.href = `/admin/character`;
        });

        document.querySelector('.detailAddBtn').addEventListener('click', () => {
            detailHelper.setModalData(detailHelper.baseImageUrl, "");
        });

        document.querySelector('#updateModalSaveBtn').addEventListener('click', () => {
            const id = document.querySelector('#updateModalSaveBtn').getAttribute('data-id');
            const src = document.querySelector('#updateUploadImage').src;
            const level = document.querySelector('#updateLevel').value;
            const environment = document.querySelector('#updateEnvironment').value;
            detailHelper.save(id, src, level, environment);
        });
    },

    getParam() {
        const description = [];
        document.querySelectorAll('.description-checkbox-input').forEach(el => {
            description.push({"step": el.value});
        });
        let params = {
            "name": document.querySelector('#characterName').value,
            "type": document.querySelector('#characterType').value,
            "descriptions": JSON.stringify(description),
            "maxLevel": document.querySelector('#maxLevel').value,
            "isPossible": document.querySelector('#isPossible').value,
        };
        if (detailHelper.isUpdateMode === true) {
            params['id'] = detailHelper.characterId;
        }
        return params;
    },

    save(id = '', src, level, environment) {
        const url = new URL(src);
        if (url.pathname === detailHelper.baseImageUrl) {
            alert("이미지를 업로드해주세요.");
            return;
        }
        if (level === "") {
            alert("레벨을 입력해주세요.");
            return;
        }
        const param = {
            "characterId": detailHelper.characterId,
            "imageUrl": src,
            "level": level,
            "environment": environment
        }
        if (id !== '') {
            param['id'] = id;
        }

        $.ajax({
            type: "POST",
            url: "/admin/character/detail/create",
            dataType: "json",
            data: JSON.stringify(param),
            contentType: 'application/json; charset=utf-8',
            success(res) {
                alert("성공적으로 생성되었습니다.");
                location.reload();
            },
            error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    },
    pullList() {
        $.ajax({
            type: "GET",
            url: "/admin/character/detail/list",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            data: {"id": detailHelper.characterId},
            success(res) {
                console.log(res);
                document.querySelector('#characterTBody').innerHTML = detailHelper.createTableRows(res.data);
            },
            error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    },

    createTableRows(data) {
        let html = '';
        let index = 0;
        for (const row of data) {
            html += `
            <tr>
                <td><i class="fab fa-angular fa-lg text-danger me-3"></i>${row.level}</td>
                <td>
                    <img src="${row.imageUrl}" alt="" class="d-block rounded" height="50" width="50" id="rowImage${index}">
                </td>
                <td><i class="fab fa-angular fa-lg text-danger me-3"></i>${environment[row.environment]}</td>
                <td>
                    <div class="dropdown">
                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                            <i class="bx bx-dots-vertical-rounded"></i>
                        </button>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="javascript:void(0);" onclick="detailHelper.pullDetail(${row.id});" data-bs-toggle="modal" data-bs-target="#updateModalToggle"><i class="bx bx-edit-alt me-1"></i> 수정하기</a>
                            <a class="dropdown-item" href="javascript:void(0);" onclick="detailHelper.deleteDetail(${row.id});"><i class="bx bx-trash me-1"></i> 삭제하기</a>
                        </div>
                    </div>
                </td>
            </tr>
            `;
            index++;
        }
        html = detailHelper.addEmptyRow(html, 4);
        return html;
    },

    pullDetail(id) {
        $.ajax({
            type: "GET",
            url: `/admin/character/detail`,
            data: {"id": id},
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            success(res) {
                document.querySelector('#updateUploadImage').src = res.data.imageUrl;
                document.querySelector('#updateLevel').value = res.data.level;
                document.querySelector('#updateModalSaveBtn').setAttribute('data-id', id);
                document.querySelector('#updateEnvironment').value = res.data.environment;
            }, error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    },

    deleteDetail(id) {
        $.ajax({
            type: "POST",
            url: `/admin/character/detail/delete`,
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

    addEmptyRow(html, count) {
        for (let i = 0; i < count; i++) {
            html += `
            <tr>
                <td><i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            `;
        }
        return html;
    },
    setModalData(imageUrl, level) {
        document.querySelector('#uploadImage').src = imageUrl;
        document.querySelector('#level').value = level;
    },
}

environment = {
    "TRASH": "쓰레기",
    "CLEAN": "꺠긋"
}