<%--
  Created by IntelliJ IDEA.
  User: oleg
  Date: 1/19/16
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <jsp:include page="../fragments/headTag.jsp"/>
  <script type="text/javascript" src="<c:url value="../resources/js/ParamUrl.js" /> "></script>
  <script type="text/javascript" src="<c:url value="../resources/js/SortClickHandle.js" /> "></script>
  <script type="text/javascript" src="<c:url value="../resources/js/arc/arcList.js" /> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="col-xs-8 col-sm-6 col-xs-offset-2 col-sm-offset-3 main">
  <h3><b>List of Arcs</b>
      <a href="/arc/add" class="btn btn-info pull-right" id="arc_add_button" title="Add arc">Add arc</a>
  </h3>
  <c:choose>
    <c:when test="${empty arcs}">
      <h4>There are not any arcs</h4>
    </c:when>
    <c:otherwise>
      <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
        <thead>
          <tr class="bg-success">
            <th id="head_id" class="text-center">ID</th>
            <th id="head_node_left" class="text-center">Node left</th>
            <th id="head_node_right" class="text-center">Node right</th>
            <th id="head_duration" class="text-center">Duration</th>
          </tr>
        </thead>
        <c:forEach items="${arcs}" var="arc">
          <tr>
            <td class="text-center">
              <a class="text-center link" href="/arc/edit?id=${arc.id}" name="arcId" title="Arc ID">${arc.id}</a>
            </td>
            <td>${arc.nodeLeft.name} [ID: ${arc.nodeLeft.id}]</td>
            <td>${arc.nodeRight.name} [ID: ${arc.nodeRight.id}]</td>
            <td class="text-center">${arc.duration}</td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
