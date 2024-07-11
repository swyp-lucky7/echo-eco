document.addEventListener("DOMContentLoaded", (event) => {
    backgroundCreate.init();
});

const backgroundCreate = {
    baseImageUrl: '/images/yes.png',
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
}