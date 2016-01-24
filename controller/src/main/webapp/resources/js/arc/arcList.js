/**
 * Created by oleg on 1/21/16.
 */
var sortClickHandle;

function onClickHeadId() {
    sortClickHandle.onClick("id");
}

function onClickHeadNodeLeft() {
    sortClickHandle.onClick("nodeleft");
}

function onClickHeadNodeRight() {
    sortClickHandle.onClick("noderight");
}

function onClickHeadDuration() {
    sortClickHandle.onClick("duration");
}

function onLoad() {

    sortClickHandle = new SortClickHandle();
    $("#head_id").click(onClickHeadId);
    $("#head_node_left").click(onClickHeadNodeLeft);
    $("#head_node_right").click(onClickHeadNodeRight);
    $("#head_duration").click(onClickHeadDuration);
}

window.onload = onLoad;
