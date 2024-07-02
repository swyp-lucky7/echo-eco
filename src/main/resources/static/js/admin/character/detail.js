document.addEventListener("DOMContentLoaded", (event) => {
    detailHelper.init();
});

const detailHelper = {
    init() {
        detailHelper.addEvent();

    },
    addEvent() {
        document.querySelector('#upload').addEventListener('change', () => {
            const fileInput = document.querySelector('#upload');
            console.log(fileInput);
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);

            // FormData 내용을 출력하는 디버깅 코드
            for (let pair of formData.entries()) {
                console.log(pair[0] + ': ' + pair[1]);
            }


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
            params['id'] = detailHelper.id;
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
            html += detailHelper.stepDescriptionInput(index++, description.step);
        }
        document.querySelector('#descriptionInputBox').innerHTML = html;
    }
}