<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fieldset>
    <legend>Edit map</legend>
    <c:if test="${nameError eq 'mapError'}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-danger">${msgError}</p>
        </div>
    </c:if>
    <c:if test="${not empty saveMapSuccess}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-success">Map was updated</p>
        </div>
    </c:if>
    <div class="form-group">
        <div class="col-sm-8 col-sm-offset-3">
            <div id="circ_routes_accordion" class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading text-left">
                        <h6 class="panel-title">
                            <a data-toggle="collapse" data-parent="#circ_routes_accordion" href="#collapse_circ_routes" title="Show/Hide">
                                Circular routes: ${countCircRoutesInMap}
                            </a>
                        </h6>
                    </div>
                    <div id="collapse_circ_routes" class="panel-collapse collapse in">
                        <div class="panel-body text-left ">
                            <c:if test="${not empty circRoutesInMap}">
                                <ul class="list-unstyled text-success">
                                    <c:forEach items="${circRoutesInMap}" var="route">
                                        <li>
                                            <span title="${route.toString()}">
                                                "${route.number} [ID: ${route.id}]"
                                            </span>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <input id="undock_circ_route_btn" class="btn btn-xs btn-info" name="undockCircRoute" type="submit" value="Undock all">
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-8 col-sm-offset-3">
            <div id="pair_routes_accordion" class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading text-left">
                        <h6 class="panel-title">
                            <a data-toggle="collapse" data-parent="#pair_routes_accordion" href="#collapse_pair_routes" title="Show/Hide">
                                Coupled routes: ${countPairsRoutesInMap}
                            </a>
                        </h6>
                    </div>
                    <div id="collapse_pair_routes" class="panel-collapse collapse in">
                        <div class="panel-body text-left ">
                            <c:if test="${not empty pairsRoutesInMap}">
                                <ul class="list-unstyled text-success">
                                    <c:forEach items="${pairsRoutesInMap}" var="pair">
                                        <li>
                                            <span title="${pair.forwardRoute.toString()}">
                                                "${pair.forwardRoute.number} [ID: ${pair.forwardRoute.id}]"
                                            </span> -
                                            <span title="${pair.backRoute.toString()}">
                                                "${pair.backRoute.number} [ID: ${pair.backRoute.id}]"
                                            </span>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <input id="undock_pair_route_btn" class="btn btn-xs btn-info" name="undockPairRoute" type="submit" value="Undock all">
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label for="map_id" class="col-sm-3 control-label">ID</label>
        <div class="col-sm-8">
            <input id="map_id" class="form-control disabled" type="text" name="mapId" readonly="readonly" value="${id}">
            <input type="hidden" name="id" value="${id}">
        </div>
    </div>
    <div class="form-group ${markMapNameError}">
        <label for="map_name" class="col-sm-3 control-label">Name</label>
        <div class="col-sm-8">
            <input id="map_name" class="form-control" name="mapName" type="text" value="${mapName}">
        </div>
    </div>
    <div class="form-group">
        <label for="map_circ_routes" class="col-sm-3 control-label">Circular routes</label>
        <div class="col-sm-8">
            <select id="map_circ_routes" class="form-control" name="circRouteId">
                <option value="0">
                    ---Select circular route---
                </option>
                <c:forEach items="${circRoutesNotInMap}" var="circRoute">
                    <option value="${circRoute.id}" ${circRouteId == circRoute.id ? 'selected' : ''}>
                            ${circRoute.number} [ID: ${circRoute.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${not empty circRoutesNotInMap}">
            <div class="col-sm-8 col-sm-offset-3 text-left">
                <input id="dock_circ_route_btn" class="btn btn-xs btn-info" name="dockCircRoute" type="submit" value="Dock">
            </div>
        </c:if>
    </div>
    <div class="form-group">
        <label for="map_circ_routes" class="col-sm-3 control-label">Coupled routes</label>
        <div class="col-sm-8">
            <select id="map_pair_routes" class="form-control" name="routePairs">
                <option value="0">
                    ---Select pair of routes---
                </option>
                <c:forEach items="${routePairsNotInMap}" var="routePair">
                    <option value="${routePair.forwardRoute.id}" ${pairForwardRouteId == routePair.forwardRoute.id ? 'selected' : ''}>
                            ${routePair.forwardRoute.number} [ID: ${routePair.forwardRoute.id}] - ${routePair.backRoute.number} [ID: ${routePair.backRoute.id}]
                            <%--${routePair.forwardRoute.number}--%>
                            <%--'${fn:substring(routePair.forwardRoute.firstNode.name, 0, 7)}..' - '${fn:substring(routePair.forwardRoute.lastNode.name, 0, 7)}..'--%>
                            <%--[ID: ${routePair.forwardRoute.id}] - ${routePair.backRoute.number} [ID: ${routePair.backRoute.id}]--%>
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${not empty routePairsNotInMap}">
            <div class="col-sm-8 col-sm-offset-3 text-left">
                <input id="dock_pair_route_btn" class="btn btn-xs btn-info" name="dockPairRoute" type="submit" value="Dock">
            </div>
        </c:if>
    </div>
    <div class="form-group">
        <label for="map_describe" class="col-sm-3 control-label">Description</label>
        <div class="col-sm-8">
            <textarea id="map_describe" class="form-control" name="mapDescribe" rows="3" cols="30">${mapDescribe}</textarea>
        </div>
    </div>
    <div class="form-group text-center">
        <input id="edit_btn" class="btn btn-primary" type="submit" name="saveMap" value="Save">
        <input id="del_btn" class="btn btn-danger" type="submit" name="deleteMap" value="Delete">
        <a href="/map/list" class="btn btn-default">Cancel</a>
    </div>
</fieldset>
