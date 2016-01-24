/**
 * Created by oleg on 1/21/16.
 */
var SortClickHandle = function() {

    var _onSortHead = function (colName) {
        var paramUrl = new ParameterURL();
        var sort = paramUrl.getParameter("sort");
        var order = paramUrl.getParameter("order");

        if (sort != colName) {
            paramUrl.setParameter("sort", colName);
            paramUrl.setParameter("order", "inc");
            location.href = paramUrl.getUrl();
            return;
        }

        if (order == "inc") {
            paramUrl.setParameter("order", "desc");
        } else {
            paramUrl.setParameter("order", "inc");
        }

        location.href = paramUrl.getUrl();
    };
    return {
        onClick: function (colName) {
            _onSortHead(colName);
        }
    }
};