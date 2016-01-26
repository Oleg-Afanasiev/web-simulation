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
    <legend>Route pair add form</legend>
    <div class="form-group">
        <label for="route_forw" class="col-sm-3 control-label">Route forward</label>
        <div class="col-sm-8">
            <select id="route_forw" class="form-control" name="routeForward">
                <option value="0">
                    ---Select forward route---
                </option>
                <c:forEach items="${notPairedRoutes}" var="notPairedRoute">
                    <option value="${notPairedRoute.id}" ${notPairedForwRouteId == notPairedRoute.id ? 'selected' : ''}>
                            ${notPairedRoute.number} [ID: ${notPairedRoute.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="route_back" class="col-sm-3 control-label">Route back</label>
        <div class="col-sm-8">
            <select id="route_back" class="form-control" name="routeBack">
                <option value="0">
                    ---Select back route---
                </option>
                <c:forEach items="${notPairedRoutes}" var="notPairedRoute">
                    <option value="${notPairedRoute.id}" ${notPairedBackRouteId == notPairedRoute.id ? 'selected' : ''}>
                            ${notPairedRoute.number} [ID: ${notPairedRoute.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group text-center">
        <input id="save_route_pair_btn" class="btn btn-primary" type="submit" name="saveRoutePair" value="Save">
    </div>
</fieldset>
