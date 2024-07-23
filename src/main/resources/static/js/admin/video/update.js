document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    id: -1,
    isUpdateMode: false,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/video\/create\/(\d+)/);
        if (match && match[1]) {
            createHelper.id = parseInt(match[1]);
            createHelper.isUpdateMode = true;
        }
        createHelper.addEvent();
    },
    addEvent() {
        document.querySelector('#modifyBtn').addEventListener('click', () => {
            const params = createHelper.getParam();
            if (!createHelper.valid(params)) {
                return;
            }
            if(createHelper.isUpdateMode) {
                const id = createHelper.id;
                const updateUrl = "/admin/video/create/" + id;
                $.ajax({
                    type: "POST",
                    url: updateUrl,
                    dataType: "json",
                    data: JSON.stringify(params),
                    contentType: 'application/json; charset=utf-8',
                    success: function (res) {
                        alert("성공적으로 업데이트되었습니다.");
                        location.href = '/admin/video'
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.error('Error:', textStatus, errorThrown);
                    }
                });
            }
        });

        document.querySelector('#fileInput').addEventListener('change', () => {
            createHelper.fileUpload('fileInput');
        });
    },

    getParam() {
        let params = {
            "name": document.querySelector('#name').value,
            "url": document.querySelector('#fileInput').src
        }

        return params;
    },

    valid(params) {
        if (params['name'] === '') {
            alert("이름을 입력해주세요.");
            return false;
        }
        if (params['url'] === '') {
            alert("파일을 업로드해주세요.");
            return false;
        }
        return true;
    },

    fileUpload(fileInputId) {
        const fileInput = document.querySelector(`#${fileInputId}`);
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);
        $.ajax({
            type: "POST",
            url: "/file/upload",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            cache: false,
            success(res) {
                console.log("Upload successfully.");
            },
            error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    }
}