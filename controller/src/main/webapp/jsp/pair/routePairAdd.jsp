<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<c:if test="${not empty nameError}">
    <c:choose>
        <c:when test="${nameError eq 'nullRouteForw'}">
            <c:set var="markRouteForwError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'nullRouteBack'}">
            <c:set var="markRouteBackError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'equalNodes'}">
            <c:set var="markRouteForwError" value="has-error" scope="page" />
            <c:set var="markRouteBackError" value="has-error" scope="page" />
        </c:when>
    </c:choose>
</c:if>
<div class="row">
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Add coupled routes form</h3>
            </div>
            <div class="panel-body">
                <form id="add_form" class="form-horizontal" name="add" action="/pair/add" method="POST">
                    <c:if test="${not empty msgError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <p class="text-danger">${msgError}</p>
                        </div>
                    </c:if>
                    <div class="form-group ${markRouteForwError}">
                        <label for="route_forw" class="col-sm-3 control-label">Route forward</label>
                        <div class="col-sm-8">
                            <select id="route_forw" class="form-control" name="routeForwId">
                                <option value="0">---Select route forward---</option>
                                <c:forEach items="${routes}" var="route">
                                    <option value="${route.id}" ${routeForwId == route.id ? 'selected' : ''}>
                                            ${route.number} [ID: ${route.id}]
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ${markRouteBackError}">
                        <label for="route_back" class="col-sm-3 control-label">Node right</label>
                        <div class="col-sm-8">
                            <select id="route_back" class="form-control" name="routeBackId">
                                <option value="0">---Select route back---</option>
                                <c:forEach items="${routes}" var="route">
                                    <option value="${route.id}" ${routeBackId == route.id ? 'selected' : ''}>
                                            ${route.number} [ID: ${route.id}]
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <input id="submit_button" class="btn btn-primary" type="submit" name="save" value="Save">
                        <a href="/pair/list" class="btn btn-default">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
