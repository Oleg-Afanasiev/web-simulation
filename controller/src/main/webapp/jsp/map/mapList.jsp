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
    <script type="text/javascript" src="<c:url value="../resources/js/map/mapList.js" /> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="col-xs-8 col-sm-6 col-xs-offset-2 col-sm-offset-3 main">
    <h3><b>List of Maps</b>
        <a href="/map/add" class="btn btn-info pull-right" id="arc_add_button" title="Add map">Add map</a>
    </h3>
    <c:choose>
        <c:when test="${empty maps}">
            <h4>There are not any maps</h4>
        </c:when>
        <c:otherwise>
            <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr class="bg-success">
                    <th id="head_id" class="text-center">ID</th>
                    <th id="head_name" class="text-center">Name</th>
                    <th id="head_circ_count" class="text-center">Circular route</th>
                    <th id="head_simple_count" class="text-center">Simple routes</th>
                </tr>
                </thead>
                <c:forEach items="${maps}" var="map">
                    <tr>
                        <td class="text-center">${map.id}</td>
                        <td class="text-center">
                            <a class="text-center link" href="/map/edit?id=${map.id}" name="mapId" title="edit map">
                                    ${map.name}
                            </a>
                        </td>
                        <td class="text-center">0</td>
                        <td class="text-center">0</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
