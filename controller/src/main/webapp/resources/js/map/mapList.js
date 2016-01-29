var sortClickHandle;

function onClickHeadId() {
    sortClickHandle.onClick("id");
}

function onClickHeadNodeLeft() {
    sortClickHandle.onClick("name");
}

function onLoad() {

    sortClickHandle = new SortClickHandle();
    $("#head_id").click(onClickHeadId);
    $("#head_name").click(onClickHeadNodeLeft);
}

window.onload = onLoad;
