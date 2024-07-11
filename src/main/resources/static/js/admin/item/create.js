document.addEventListener("DOMContentLoaded", (event) => {
    backgroundCreate.init();
});

const backgroundCreate = {
    baseImageUrl: '/images/yes.png',
    id: -1,
    isUpdateMode: false,
    init() {
        const url = window.location.href;
        const match = url.match(/\/admin\/item\/(\d+)\/edit/);
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
            if(backgroundCreate.isUpdateMode){
                const id = backgroundCreate.id;
                const updateUrl = "/admin/item/" + id + "/edit";
                $.ajax({
                    type: "POST",
                    url: updateUrl,
                    dataType: "json",
                    data: JSON.stringify(params),
                    contentType: 'application/json; charset=utf-8',
                    success(res) {
                        alert("성공적으로 업데이트되었습니다.");
                        location.href = '/admin/item';
                    },
                    error(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR, textStatus, errorThrown);
                    }
                }
                );
                return;
            }

            $.ajax({
                type: "POST",
                url: "/admin/item/create",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: 'application/json; charset=utf-8',
                success(res) {
                    alert("성공적으로 생성되었습니다.");
                    location.href = '/admin/item';
                },
                error(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR, textStatus, errorThrown);
                }
            }
            );

            function valid(params) {
                const url = new URL(params.imageUrl);
                if (url.pathname === backgroundCreate.baseImageUrl) {
                    alert("이미지를 업로드해주세요.");
                    return false;
                }
                if (params.name === '') {
                    alert("이름을 입력해주세요.");
                    return false;
                }
                if (params.levelUp === '') {
                    alert("상승시킬 레벨을 입력해주세요.");
                    return false;
                }
                if (params.price === '') {
                    alert("가격을 입력해주세요.")
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
            url: "/file/upload",
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
            "imageUrl": document.querySelector('#uploadImage').src,
            "description": document.querySelector('#description').value,
            "price": document.querySelector('#price').value,
            "levelUp": document.querySelector('#levelUp').value,
            "isUse": document.querySelector('#isUse').value
        }
        if (backgroundCreate.isUpdateMode) {
            params['id'] = backgroundCreate.id;
        }
        return params;
    },
}