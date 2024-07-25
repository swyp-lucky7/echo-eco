document.addEventListener("DOMContentLoaded", (event) => {
    backgroundCreate.init();
});

const backgroundCreate = {
    baseImageUrl: '/images/vendor/icons/unicons/chart.png',
    id: -1,
    isUpdateMode: false,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/background\/create\/(\d+)/);
        if (match && match[1]) {
            backgroundCreate.id = parseInt(match[1]);
            backgroundCreate.isUpdateMode = true;
        }
        backgroundCreate.addEvent();
    },

    addEvent() {
        document.querySelector('#upload').addEventListener('change', () => {
            backgroundCreate.fileUpload('upload', 'uploadImage');
        });

        document.querySelector('#saveBtn').addEventListener('click', () => {
            const params = backgroundCreate.getParams();
            if (!valid(params)) {
                return;
            }
            $.ajax({
                type: "POST",
                url: "/admin/background/create",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: 'application/json; charset=utf-8',
                success(res) {
                    alert("성공적으로 생성되었습니다.");
                    location.href = '/admin/background';
                },
                error(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR, textStatus, errorThrown);
                }
            });

            function valid(params) {
                const url = new URL(params.image);
                if (url.pathname === backgroundCreate.baseImageUrl) {
                    alert("이미지를 업로드해주세요.");
                    return false;
                }
                if (params.name === '') {
                    alert("이름을 입력해주세요.");
                    return false;
                }
                if (params.level === '') {
                    alert("레벨을 입력해주세요.");
                    return false;
                }
                return true;
            }
        });
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

    getParams() {
        let params = {
            "name": document.querySelector('#name').value,
            "image": document.querySelector('#uploadImage').src,
            "environment": document.querySelector('#environment').value,
            "level": document.querySelector('#level').value,
        }
        if (backgroundCreate.isUpdateMode) {
            params['id'] = backgroundCreate.id;
        }
        return params;
    },
}