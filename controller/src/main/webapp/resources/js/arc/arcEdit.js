/**
 * Created by oleg on 1/21/16.
 */
function onClickDelete() {
    return confirm("Are you sure to delete this arc?");
}

function onLoad() {
    $("#del_btn").click(onClickDelete);
}

window.onload = onLoad;