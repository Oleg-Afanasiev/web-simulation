var sortClickHandle;

function onClickHeadId() {
    sortClickHandle.onClick("id");
}

function onClickHeadNumber() {
    sortClickHandle.onClick("number");
}

function onClickHeadCost() {
    sortClickHandle.onClick("cost");
}

function onClickHeadFirstNode() {
    sortClickHandle.onClick("firstnode");
}

function onClickHeadLastNode() {
    sortClickHandle.onClick("lastnode");
}

function onLoad() {

    sortClickHandle = new SortClickHandle();
    $("#head_id").click(onClickHeadId);
    $("#head_number").click(onClickHeadNumber);
    $("#head_cost").click(onClickHeadCost);
    $("#head_first_node").click(onClickHeadFirstNode);
    $("#head_last_node").click(onClickHeadLastNode)
}

window.onload = onLoad;
