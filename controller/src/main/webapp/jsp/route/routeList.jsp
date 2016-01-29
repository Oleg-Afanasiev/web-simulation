<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <jsp:include page="../fragments/headTag.jsp"/>
  <script type="text/javascript" src="<c:url value="../resources/js/ParamUrl.js" /> "></script>
  <script type="text/javascript" src="<c:url value="../resources/js/SortClickHandle.js" /> "></script>
  <script type="text/javascript" src="<c:url value="../resources/js/route/routeList.js" /> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="col-xs-8 col-sm-6 col-xs-offset-2 col-sm-offset-3 main">
  <h3><b>List of Routes</b>
    <a href="/route/add" class="btn btn-info pull-right" id="route_add_button" title="Add route">Add route</a>
  </h3>
  <c:choose>
    <c:when test="${empty routes}">
      <h4>There are not any routes</h4>
    </c:when>
    <c:otherwise>
      <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr class="bg-success">
          <th id="head_id" class="text-center">ID</th>
          <th id="head_number" class="text-center">Number</th>
          <th id="head_cost" class="text-center">Cost</th>
          <th id="head_first_node" class="text-center">First station</th>
          <th id="head_last_node" class="text-center">Last station</th>
        </tr>
        </thead>
        <c:forEach items="${routes}" var="route">
          <tr title="${route.toString()}">
            <td class="text-center">
                ${route.id}
            </td>
            <td class="text-center">
              <a class="text-center link" href="/route/edit?id=${route.id}" name="routeId" title="Route number">${route.number}</a>
            </td>
            <td class="text-center">${route.cost}</td>
            <td>${route.firstNode.name}</td>
            <td>${route.lastNode.name}</td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
