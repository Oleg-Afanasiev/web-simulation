<%--
  Created by IntelliJ IDEA.
  User: oleg
  Date: 1/25/16
  Time: 10:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Station add form</legend>
    <div class="form-group">
        <label for="station_name" class="col-sm-3 control-label">Name</label>
        <div class="col-sm-8">
            <input id="station_name" class="form-control" name="stationName" type="text" value="${stationName}">
        </div>
    </div>
    <div class="form-group text-center">
        <input id="save_station_btn" class="btn btn-primary" type="submit" name="saveStation" value="Save">
    </div>
</fieldset>
