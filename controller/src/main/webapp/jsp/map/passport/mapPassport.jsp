<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../../fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="../../fragments/menu.jsp"/>
<div class="col-xs-8 col-sm-6 col-xs-offset-2 col-sm-offset-3 main">

    <c:if test="${not empty map}">
        <h3>
            <b>${map.name}</b>
        </h3>
        <p>
                ${map.describe}
        </p>

        <h4>
            <b>Search path</b>
        </h4>
        <c:if test="${not empty msgError}">
            <div class="col-xs-10 col-xs-offset-1">
                <p class="text-danger">${msgError}</p>
            </div>
        </c:if>
        <jsp:include page="search.jsp" />

        <c:if test="${not empty map.circularRoutes}">
            <h4><b>Circular routes</b></h4>
            <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr class="bg-success">
                    <th class="text-center">ID</th>
                    <th class="text-center">Number</th>
                    <th class="text-center">Cost</th>
                    <th class="text-center">Stations</th>
                </tr>
                </thead>
                <c:forEach items="${map.circularRoutes}" var="route">
                    <tr title="${route.toString()}">
                        <td class="text-center">${route.id}</td>
                        <td class="text-center">
                            <a href="/route/passport?id=${route.id}">${route.number}</a>
                        </td>
                        <td class="text-center">${route.cost} UAH</td>
                        <td class="text-center">
                            <div class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading text-left">
                                        <a data-toggle="collapse" data-parent="#seq_accordion" href="#collapse_seq_circ_${loop.count}" title="Show/Hide">
                                            stations
                                        </a>
                                    </div>
                                    <div id="collapse_seq_circ_${loop.count}" class="panel-collapse collapse">
                                        <div class="panel-body text-left">
                                            <ul> class="list-unstyled text-success">
                                                <c:forEach items="${route.sequenceNodes}" var="node">
                                                    <li><a href="/station/passport?id=${node.id}&mapid=${map.id}">"${node.name}"</a> [ID: ${node.id}] -></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <c:if test="${not empty map.pairsRoutes}">
            <h4>Coupled routes</h4>
            <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr class="bg-success">
                    <th class="text-center">ID</th>
                    <th class="text-center">Number</th>
                    <th class="text-center">Cost</th>
                    <th class="text-center">Stations</th>

                    <th class="text-center"></th>

                    <th class="text-center">ID</th>
                    <th class="text-center">Number</th>
                    <th class="text-center">Cost</th>
                    <th class="text-center">Stations</th>
                </tr>
                </thead>
                <c:forEach items="${map.pairsRoutes}" var="pair" varStatus="loop">
                    <tr>
                        <td class="text-center">${pair.forwardRoute.id}</td>
                        <td class="text-center" title="${pair.forwardRoute.toString()}">
                            <a href="/route/passport?id=${pair.forwardRoute.id}">${pair.forwardRoute.number}</a>
                        </td>
                        <td class="text-center">${pair.forwardRoute.cost} UAH</td>
                        <td class="text-center">
                            <div class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading text-left">
                                        <a data-toggle="collapse" data-parent="#seq_accordion" href="#collapse_seq_forw_${loop.count}" title="Show/Hide">
                                            stations
                                        </a>
                                    </div>
                                    <div id="collapse_seq_forw_${loop.count}" class="panel-collapse collapse">
                                        <div class="panel-body text-left ">
                                            <ul class="list-unstyled text-success">
                                                <c:forEach items="${pair.forwardRoute.sequenceNodes}" var="node">
                                                    <li><a href="/station/passport?id=${node.id}&mapid=${map.id}">"${node.name}</a>" [ID: ${node.id}] -></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>

                        <td class="text-center"><span class="glyphicon glyphicon-resize-horizontal"></span></td>

                        <td class="text-center">${pair.backRoute.id}</td>
                        <td class="text-center" title="${pair.backRoute.toString()}">
                            <a href="/route/passport?id=${pair.backRoute.id}">${pair.backRoute.number}</a>
                        </td>
                        <td class="text-center">${pair.backRoute.cost} UAH</td>
                        <td class="text-center">
                            <div class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading text-left">
                                        <a data-toggle="collapse" data-parent="#seq_accordion" href="#collapse_seq_back_${loop.count}" title="Show/Hide">
                                            stations
                                        </a>
                                    </div>
                                    <div id="collapse_seq_back_${loop.count}" class="panel-collapse collapse">
                                        <div class="panel-body text-left ">
                                            <ul class="list-unstyled text-success">
                                                <c:forEach items="${pair.backRoute.sequenceNodes}" var="node">
                                                    <li><a href="/station/passport?id=${node.id}&mapid=${map.id}">"${node.name}</a>" [ID: ${node.id}] -></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:if>
</div>
</body>
</html>
