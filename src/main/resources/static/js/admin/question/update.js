document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    id: -1,
    isUpdateMode: false,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/question\/create\/(\d+)/);
        if (match && match[1]) {
            createHelper.id = parseInt(match[1]);
            createHelper.isUpdateMode = true;
        }
        createHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#questionType').addEventListener('change', () => {
            const value = document.querySelector('#questionType').value;
            switch (value) {
                case "MULTIPLE_CHOICE":
                    document.querySelector('#multipleChoiceDiv').style.display = '';
                    document.querySelector('#subjectiveDiv').style.display = 'none';
                    break;
                case "SUBJECTIVE":
                    document.querySelector('#multipleChoiceDiv').style.display = 'none';
                    document.querySelector('#subjectiveDiv').style.display = '';
                    break;
                default:
                    break;
            }
        });

        document.querySelector('#addButton').addEventListener('click', () => {
            let index = document.querySelectorAll('.multiple-checkbox').length + 1;
            document.querySelector('#multipleChoiceBox').insertAdjacentHTML('beforeend', createHelper.rowMultipleCheckbox(index, ''));
        });

        document.querySelector('#removeBtn').addEventListener('click', () => {
            let removeIndexList = [];
            document.querySelectorAll('.multiple-checkbox:checked').forEach(checkbox => {
                removeIndexList.push(checkbox.getAttribute('data-index'));
            });
            removeIndexList.forEach(index => document.querySelector(`#multipleCheckbox${index}`).remove());

            let remainValueList = [];
            document.querySelectorAll('.multiple-checkbox').forEach(checkbox => {
                let index = checkbox.getAttribute('data-index');
                remainValueList.push(document.querySelector(`#multipleInput${index}`).value);
            });

            let html = '';
            for (const index in remainValueList) {
                html += createHelper.rowMultipleCheckbox(Number(index) + 1, remainValueList[index]);
            }
            document.querySelector('#multipleChoiceBox').innerHTML = html;

        });

        document.querySelector('#modifyBtn').addEventListener('click', () => {
            const params = createHelper.getParam();
            if (!createHelper.valid(params)) {
                return;
            }
            if(createHelper.isUpdateMode) {
                const id = createHelper.id;
                const updateUrl = "/admin/question/create/" + id;
                $.ajax({
                    type: "POST",
                    url: updateUrl,
                    dataType: "json",
                    data: JSON.stringify(params),
                    contentType: 'application/json; charset=utf-8',
                    success: function (res) {
                        alert("성공적으로 업데이트되었습니다.");
                        location.href = '/admin/question'
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.error('Error:', textStatus, errorThrown);
                    }
                });
            }
        });
    },

    rowMultipleCheckbox(index, value) {
        return `
                <div class="row demo-vertical-spacing mb-3" id="multipleCheckbox${index}">
                    <div class="input-group">
                        <span class="input-group-text">${index}</span>
                        <div class="input-group-text">
                            <input class="form-check-input mt-0 multiple-checkbox" data-index="${index}" type="checkbox" aria-label="Checkbox for following text input">
                        </div>
                        <input type="text" class="form-control multiple-checkbox-input" aria-label="Text input with checkbox" id="multipleInput${index}" value="${value}">
                    </div>
                </div>
            `;
    },

    getParam() {
        let params = {
            "name": document.querySelector('#questionName').value,
            "questionType": document.querySelector('#questionType').value,
            "head": document.querySelector('#questionHead').value,
            "answer": document.querySelector('#answerInput').value,
        }
        if (document.querySelector('#questionType').value === 'MULTIPLE_CHOICE') {
            const valueList = [];
            document.querySelectorAll('.multiple-checkbox-input').forEach(el => {
                valueList.push({"row": el.value});
            });
            params['body'] = JSON.stringify(valueList);
        } else if (document.querySelector('#questionType').value === 'SUBJECTIVE') {
            params['body'] = document.querySelector('#subjectiveInput').value;
        }

        return params;
    },

    valid(params) {
        if (params['name'] === '') {
            alert("이름을 입력해주세요.");
            return false;
        }
        if (params['head'] === '') {
            alert("질문을 입력해주세요.");
            return false;
        }
        if (params['answer'] === '') {
            alert("정답을 입력해주세요.");
            return false;
        }
        return true;
    }
}