<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Station add form</legend>
    <c:if test="${nameError eq 'stationError'}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-danger">${msgError}</p>
        </div>
        <c:choose>
            <c:when test="${nameError eq 'NullStationNameError'}">
                <c:set var="markStationNameError" value="has-error" scope="page" />
            </c:when>
        </c:choose>
    </c:if>
    <c:if test="${not empty saveStationSuccess}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-success">Station was saved</p>
        </div>
    </c:if>
    <div class="form-group ${markStationNameError}">
        <label for="station_name" class="col-sm-3 control-label">Name</label>
        <div class="col-sm-8">
            <input id="station_name" class="form-control" name="stationName" type="text" value="${stationName}">
        </div>
    </div>
    <div class="form-group text-center">
        <input id="save_station_btn" class="btn btn-primary" type="submit" name="saveStation" value="Save">
    </div>
</fieldset>
