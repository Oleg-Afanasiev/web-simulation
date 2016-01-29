function onClickDelete() {
    var name = $('#station_name').val();
    var id = $('#station_id').val();
    return confirm("Are you sure to delete this station: '" + name + "' [ID: " + id + "]?");
}

function onLoad() {
    $("#del_btn").click(onClickDelete);
}

window.onload = onLoad;