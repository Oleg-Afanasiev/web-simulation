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
        <c:when test="${nameError eq 'nullNodeLeft'}">
            <c:set var="markNodeLeftError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'nullNodeRight'}">
            <c:set var="markNodeRightError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'equalNodes'}">
            <c:set var="markNodeLeftError" value="has-error" scope="page" />
            <c:set var="markNodeRightError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'nullDuration'}">
            <c:set var="markNodeDurError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'negativeDuration'}">
            <c:set var="markNodeDurError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'fmtDur'}">
            <c:set var="markNodeDurError" value="has-error" scope="page" />
        </c:when>
    </c:choose>
</c:if>
<div class="row">
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Add arc form</h3>
            </div>
            <div class="panel-body">
                <form id="add_arc_form" class="form-horizontal" name="addArc" action="/arc/add" method="POST">
                    <c:if test="${not empty msgError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <p class="text-danger">${msgError}</p>
                        </div>
                    </c:if>
                    <div class="form-group ${markNodeLeftError}">
                        <label for="node_left" class="col-sm-3 control-label">Node left</label>
                        <div class="col-sm-8">
                            <select id="node_left" class="form-control" name="nodeLeftId">
                                <option value="0">---Select left node---</option>
                                <c:forEach items="${nodes}" var="nodeLeft">
                                    <option value="${nodeLeft.id}" ${nodeLeftId == nodeLeft.id ? 'selected' : ''}>
                                            ${nodeLeft.name} [ID: ${nodeLeft.id}]
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ${markNodeRightError}">
                        <label for="node_right" class="col-sm-3 control-label">Node right</label>
                        <div class="col-sm-8">
                            <select id="node_right" class="form-control" name="nodeRightId">
                                <option value="0">---Select right node---</option>
                                <c:forEach items="${nodes}" var="nodeRight">
                                    <option value="${nodeRight.id}" ${nodeRightId == nodeRight.id ? 'selected' : ''}>
                                            ${nodeRight.name} [ID: ${nodeRight.id}]
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ${markNodeDurError}">
                        <label for="duration" class="col-sm-3 control-label">Duration</label>
                        <div class="col-sm-8">
                            <input id="duration" class="form-control" name="duration" type="text" value="${duration}">
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <input id="submit_button" class="btn btn-primary" type="submit" name="saveArc" value="Save">
                        <a href="/arc/list" class="btn btn-default">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
