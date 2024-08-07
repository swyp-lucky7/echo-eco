document.addEventListener("DOMContentLoaded", (event) => {
    createHelper.init();
});

const createHelper = {
    id: -1,
    isUpdateMode: false,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/character\/create\/(\d+)/);
        if (match && match[1]) {
            createHelper.id = parseInt(match[1]);
            createHelper.isUpdateMode = true;
            createHelper.initUpdate();
        }
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
                url: "/admin/character/create",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: 'application/json; charset=utf-8',
                success: function(res) {
                    if (createHelper.isUpdateMode === false) {
                        alert("성공적으로 생성되었습니다.");
                    } else {
                        alert("성공적으로 업데이트되었습니다.");
                    }
                    location.href = "/admin/character";
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
        });

        document.querySelector('#addButton').addEventListener('click', () => {
            let index = document.querySelectorAll('.description-checkbox').length + 1;
            document.querySelector('#descriptionInputBox').insertAdjacentHTML('beforeend', createHelper.stepDescriptionInput(index, ''));
        });
        document.querySelector('#addCompleteButton').addEventListener('click', () => {
            let index = document.querySelectorAll('.complete-checkbox').length + 1;
            document.querySelector('#completeInputBox').insertAdjacentHTML('beforeend', createHelper.stepCompleteMessagesInput(index, ''));
        });

        document.querySelector('#removeBtn').addEventListener('click', () => {
            let removeIndexList = [];
            document.querySelectorAll('.description-checkbox:checked').forEach(checkbox => {
                removeIndexList.push(checkbox.getAttribute('data-index'));
            });
            removeIndexList.forEach(index => document.querySelector(`#descriptionCheckbox${index}`).remove());

            let remainValueList = [];
            document.querySelectorAll('.description-checkbox').forEach(checkbox => {
                let index = checkbox.getAttribute('data-index');
                remainValueList.push(document.querySelector(`#descriptionInput${index}`).value);
            });

            let html = '';
            for (const index in remainValueList) {
                html += createHelper.stepDescriptionInput(Number(index) + 1, remainValueList[index]);
            }
            document.querySelector('#descriptionInputBox').innerHTML = html;
        });

        document.querySelector('#removeCompleteBtn').addEventListener('click', () => {
            let removeIndexList = [];
            document.querySelectorAll('.complete-checkbox:checked').forEach(checkbox => {
                removeIndexList.push(checkbox.getAttribute('data-index'));
            });
            removeIndexList.forEach(index => document.querySelector(`#completeCheckbox${index}`).remove());

            let remainValueList = [];
            document.querySelectorAll('.complete-checkbox').forEach(checkbox => {
                let index = checkbox.getAttribute('data-index');
                remainValueList.push(document.querySelector(`#completeInput${index}`).value);
            });

            let html = '';
            for (const index in remainValueList) {
                html += createHelper.stepCompleteMessagesInput(Number(index) + 1, remainValueList[index]);
            }
            document.querySelector('#completeInputBox').innerHTML = html;
        });

        document.querySelector('#upload').addEventListener('change', () => {
            createHelper.fileUpload('upload', 'uploadImage');
        });
    },

    getParam() {
        const description = [];
        document.querySelectorAll('.description-checkbox-input').forEach(el => {
            description.push({"step": el.value});
        });

        const completeMessages = [];
        document.querySelectorAll('.complete-checkbox-input').forEach(el => {
            completeMessages.push({"step": el.value});
        });
        let params = {
            "name": document.querySelector('#characterName').value,
            "type": document.querySelector('#characterType').value,
            "descriptions": JSON.stringify(description),
            "maxLevel": document.querySelector('#maxLevel').value,
            "isPossible": document.querySelector('#isPossible').value,
            "image": document.querySelector('#uploadImage').src,
            "completeMessages": JSON.stringify(completeMessages),
        };
        if (createHelper.isUpdateMode === true) {
            params['id'] = createHelper.id;
        }
        return params;
    },

    stepDescriptionInput(index, value) {
        return `
                <div class="row demo-vertical-spacing mb-3" id="descriptionCheckbox${index}">
                    <div class="input-group">
                        <span class="input-group-text">${index}</span>
                        <div class="input-group-text">
                            <input class="form-check-input mt-0 description-checkbox" data-index="${index}" type="checkbox" aria-label="Checkbox for following text input">
                        </div>
                        <input type="text" class="form-control description-checkbox-input" aria-label="Text input with checkbox" id="descriptionInput${index}" value="${value}">
                    </div>
                </div>
            `;
    },

    stepCompleteMessagesInput(index, value) {
        return `
                <div class="row demo-vertical-spacing mb-3" id="completeCheckbox${index}">
                    <div class="input-group">
                        <span class="input-group-text">${index}</span>
                        <div class="input-group-text">
                            <input class="form-check-input mt-0 complete-checkbox" data-index="${index}" type="checkbox" aria-label="Checkbox for following text input">
                        </div>
                        <input type="text" class="form-control complete-checkbox-input" aria-label="Text input with checkbox" id="completeInput${index}" value="${value}">
                    </div>
                </div>
            `;
    },
    initUpdate() {
        const descriptions = JSON.parse(document.querySelector('#descriptionFrame').getAttribute('data-json'));
        let html = '';
        let index = 0;
        for (const description of descriptions) {
            html += createHelper.stepDescriptionInput(index++, description.step);
        }
        document.querySelector('#descriptionInputBox').innerHTML = html;

        //
        const completeMessages = JSON.parse(document.querySelector('#completeFrame').getAttribute('data-json'));
        html = '';
        index = 0;
        for (const completeMessage of completeMessages) {
            html += createHelper.stepCompleteMessagesInput(index++, completeMessage.step);
        }
        document.querySelector('#completeInputBox').innerHTML = html;
    },

    valid(params) {
        const pickUrl = new URL(params['image']);
        if (pickUrl.pathname === '/images/vendor/icons/unicons/chart.png') {
            alert("pick 이미지를 업로드해주세요.");
            return false;
        }
        if (params['name'] === '') {
            alert("이름을 입력해주세요");
            return false;
        }
        let descriptions = JSON.parse(params['descriptions']);
        if (descriptions.length === 0) {
            alert("설명이 하나도 존재하지 않습니다.");
            return false;
        }
        for (const description of descriptions) {
            if (description['step'] === '') {
                alert('설명에 빈칸이 존재합니다.');
                return false;
            }
        }
        if (params['maxLevel'] === '') {
            alert("최대 레벨을 입력해주세요");
            return false;
        }
        return true;
    },

    fileUpload(fileInputId, uploadImageId) {
        const fileInput = document.querySelector(`#${fileInputId}`);
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
                document.querySelector(`#${uploadImageId}`).src = res.data;
            },
            error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    },
}

