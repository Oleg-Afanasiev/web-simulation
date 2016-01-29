function onClickDelete() {
    return confirm("Are you sure to delete this pair?");
}

function onLoad() {
    $("#del_btn").click(onClickDelete);
}

window.onload = onLoad;/**

