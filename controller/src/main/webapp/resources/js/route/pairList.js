var sortClickHandle;

function onClickHeadId() {
    sortClickHandle.onClick("forw");
}

function onClickHeadNumber() {
    sortClickHandle.onClick("back");
}

function onLoad() {

    sortClickHandle = new SortClickHandle();
    $("#head_forw").click(onClickHeadId);
    $("#head_back").click(onClickHeadNumber);
}

window.onload = onLoad;
