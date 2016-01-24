/**
 * Created by oleg on 1/21/16.
 */
var sortClickHandle;

function onClickHeadId() {
    sortClickHandle.onClick("id");
}

function onClickHeadName() {
    sortClickHandle.onClick("name");
}

function onLoad() {
    sortClickHandle = new SortClickHandle();
    $("#head_id").click(onClickHeadId);
    $("#head_name").click(onClickHeadName);
}

window.onload = onLoad;

