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
            $.ajax({
                type: "POST",
                url: "/admin/character/create",
                dataType: "json",
                data: JSON.stringify(createHelper.getParam()),
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
    initUpdate() {
        const descriptions = JSON.parse(document.querySelector('#descriptionFrame').getAttribute('data-json'));
        console.log(descriptions);
        let html = '';
        let index = 0;
        for (const description of descriptions) {
            html += createHelper.stepDescriptionInput(index++, description.step);
        }
        document.querySelector('#descriptionInputBox').innerHTML = html;
    }
}