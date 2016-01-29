<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <script type="text/javascript" src="<c:url value="../resources/js/ParamUrl.js" />"></script>
    <script type="text/javascript" src="<c:url value="../resources/js/SortClickHandle.js" />"></script>
    <script type="text/javascript" src="<c:url value="../resources/js/station/stationList.js" />"></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="row">
    <div class="col-xs-8 col-sm-6  col-xs-offset-2 col-sm-offset-3 main">
        <h3><b>List of stations</b>
            <a href="/station/add" class="btn btn-info pull-right" id="station_add_button" title="Add station">Add station</a>
        </h3>
        <c:choose>
            <c:when test="${empty stations}">
                <h4>There are not any stations</h4>
            </c:when>
            <c:otherwise>
                <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                    <thead>
                    <tr class="bg-success">
                        <th id="head_id" class="text-center">ID</th>
                        <th id="head_name" class="text-center">Name</th>
                    </tr>
                    </thead>
                    <c:forEach items="${stations}" var="station">
                        <tr>
                            <td class="text-center">${station.id}</td>
                            <td>
                                <a class="link" href="/station/edit?id=${station.id}" name="stationEdit" title="Edit station">
                                        ${station.name}
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
