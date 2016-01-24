/**
 * Created by oleg on 1/21/16.
 */
var ParameterURL = function() {
    var _urlParam = location.search.substr(1);

    var _getJsonFromUrl = function() {

        var result = {};
        _urlParam.split("&").forEach(function(part) {
            var item = part.split("=");
            if (item != null && item != undefined && item != "")
                result[item[0]] = decodeURIComponent(item[1]);
        });
        return result;
    };

    var _getUrlParamFromJson = function(json) {
        var _url = "";

        for (var key in json) {
            _url += key + "=" + json[key] + "&";
        }
        return _url.substr(0, _url.length - 1);
    };

    var _setParameter = function (name, value) {
        var json = _getJsonFromUrl();

        json[name] = value;
        _urlParam = _getUrlParamFromJson(json);
    };

    var _getParameter = function(name) {
        return _getJsonFromUrl()[name];
    };

    return {
        setParameter: function(name, value) {
            _setParameter(name, value);
        },

        getParameter: function(name) {
            return _getParameter(name);
        },

        getUrl: function() {
            return location.protocol + '//' + location.host + location.pathname +
                (_urlParam == "" || _urlParam == "undefined" || _urlParam == null ? "" : "?" + _urlParam);
        }
    }
};