<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <script type="text/javascript" src="<c:url value="../resources/js/map/mapEdit.js"/> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<c:if test="${not empty nameError}">
    <c:choose>
        <c:when test="${nameError eq 'nullMapNameError'}">
            <c:set var="markMapNameError" value="has-error" scope="page" />
        </c:when>
    </c:choose>
</c:if>
<div class="row">
    <div class="col-sm-9 col-sm-offset-2 col-md-10 col-md-offset-1 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Edit map "${mapName}"</h3>
            </div>
            <div class="panel-body">
                <form id="edit_form" class="form-horizontal" name="edit" action="/map/edit" method="POST">
                    <div class="row">
                        <div class="col-md-6">
                            <jsp:include page="subForms/mapEditSubForm.jsp"/>
                        </div>
                        <div class="col-md-6">
                            <div class="row">
                                <div class="col-md-12">
                                    <jsp:include page="subForms/stationAdd.jsp" />
                                </div>
                                <div class="col-md-12">
                                    <jsp:include page="subForms/arcAdd.jsp" />
                                </div>
                                <div class="col-md-12">
                                    <jsp:include page="subForms/routeAdd.jsp" />
                                </div>
                                <div class="col-md-12">
                                    <jsp:include page="subForms/routePairAdd.jsp"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
