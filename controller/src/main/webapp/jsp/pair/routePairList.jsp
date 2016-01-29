<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <script type="text/javascript" src="<c:url value="../resources/js/ParamUrl.js" /> "></script>
    <script type="text/javascript" src="<c:url value="../resources/js/SortClickHandle.js" /> "></script>
    <script type="text/javascript" src="<c:url value="../resources/js/route/pairList.js" /> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="col-xs-8 col-sm-6 col-xs-offset-2 col-sm-offset-3 main">
    <h3><b>List of Pairs</b>
        <a href="/pair/add" class="btn btn-info pull-right" id="pair_add_button" title="Add pair">Add pair</a>
    </h3>
    <c:choose>
        <c:when test="${empty pairs}">
            <h4>There are not any pairs</h4>
        </c:when>
        <c:otherwise>
            <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr class="bg-success">
                    <th id="head_forw" class="text-center">Forward route</th>
                    <th id="head_back" class="text-center">Back route</th>
                    <th id="head_del" class="text-center">Delete</th>
                </tr>
                </thead>
                <c:forEach items="${pairs}" var="pair">
                    <tr>
                        <td class="text-center" title="${pair.forwardRoute.toString()}">
                                '${pair.forwardRoute.number}' [ID: ${pair.forwardRoute.id}]
                        </td>
                        <td class="text-center" title="${pair.backRoute.toString()}">
                            '${pair.backRoute.number}' [ID: ${pair.backRoute.id}]
                        </td>
                        <td class="text-center">
                            <a class="btn btn-xs btn-danger" href="/pair/edit?forwid=${pair.forwardRoute.id}&backid=${pair.backRoute.id}">delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
