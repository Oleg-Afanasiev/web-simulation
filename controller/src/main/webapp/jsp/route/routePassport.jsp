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
        <h3>
            <b>Passport of routes</b>
        </h3>
        <c:choose>
            <c:when test="${empty route}">
                <h4>There are not data about route</h4>
            </c:when>
            <c:otherwise>
                <h6>${route.description}</h6>
                <h6><b>List of stations</b></h6>

                <c:forEach items="${route.sequenceNodes}" var="node">
                    <div>"${node.name}" [ID: ${node.id}]</div>
                    <div class="glyphicon glyphicon-arrow-right">
                    </div>
                </c:forEach>
                <div class="row">
                    <div class="col-md-6">
                        <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                            <thead>
                            <tr class="bg-success">
                                <th id="head_id" class="text-center col-sm-2">ID</th>
                                <th id="head_number" class="text-center col-sm-4">Number</th>
                                <th id="head_cost" class="text-center col-sm-4">Cost</th>
                                <th id="head_stations" class="text-center col-sm-4">Stations</th>
                            </tr>
                            </thead>
                            <tr>
                                <td class="text-center">${route.id}</td>
                                <td class="text-center">"${route.number}"</td>
                                <td class="text-center">${route.cost} UAH</td>
                                <td class="text-center">${route.sequenceNodes.size()}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
