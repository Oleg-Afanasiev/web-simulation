function onClickDelete() {
    return confirm("Are you sure to delete this map?");
}

function onLoad() {
    $("#del_btn").click(onClickDelete);
}

window.onload = onLoad;