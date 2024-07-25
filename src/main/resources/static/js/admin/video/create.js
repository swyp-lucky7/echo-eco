document.addEventListener("DOMContentLoaded", (event) => {
    console.log("DOM fully loaded and parsed");
    createHelper.init();
});

const createHelper = {
    init() {
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
                url: "/admin/video/create",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: 'application/json; charset=utf-8',
                success: function() {
                    alert("성공적으로 생성되었습니다.");
                    location.href = '/admin/video'
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error:', textStatus, errorThrown);
                }
            });
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
            url: "/admin/file/upload",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            cache: false,
            success(res) {
                document.querySelector(`#${fileInputId}`).src = res.data;
            },
            error(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR, textStatus, errorThrown);
            }
        });
    }
}