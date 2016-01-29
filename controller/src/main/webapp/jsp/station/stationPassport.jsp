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

<div class="row">
    <div class="col-xs-8 col-sm-6  col-xs-offset-2 col-sm-offset-3 main">
        <c:choose>
            <c:when test="${empty node}">
                <h4>There are not data about station</h4>
            </c:when>
            <c:otherwise>
                <h3>
                    <b>"${node.name}" [ID: ${node.id}]</b>
                </h3>

                <h4 class="text text-info"><b>Routes across this station:</b> </h4>
                <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                    <thead>
                    <tr class="text-center bg-success">
                        <th>ID</td>
                        <th>Number</th>
                        <th>Cost</th>
                        <th>First node</th>
                        <th><span class="glyphicon glyphicon-arrow-right"></span></th>
                        <th>Last node</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${routes}" var="route">
                        <tr class="text-center" title="${route.toString()}">
                            <td>${route.id}</td>
                            <td>"${route.number}"</td>
                            <td>${route.cost}</td>
                            <td>"${route.firstNode.name}"</td>
                            <td>...</td>
                            <td>"${route.lastNode.name}"</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
