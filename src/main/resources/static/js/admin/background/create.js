document.addEventListener("DOMContentLoaded", (event) => {
    backgroundRead.init();
});

const backgroundCreate = {
    init() {
        backgroundCreate.addEvent();
    },

    addEvent() {

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