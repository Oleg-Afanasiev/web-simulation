<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <script type="text/javascript" src="<c:url value="../resources/js/route/pairEdit.js"/> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="row">
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Route pair edit form</h3>
            </div>
            <div class="panel-body">
                <form id="edit_form" class="form-horizontal" name="editPair" action="/pair/edit" method="POST">
                    <c:if test="${not empty msgError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <p class="text-danger">${msgError}</p>
                        </div>
                        <c:set var="mrkError" value="has-error" />
                    </c:if>
                    <table class="table table-hover table-bordered table-striped" border="1" cellpadding="8" cellspacing="0">
                        <thead>
                        <tr class="bg-success">
                            <th id="head_forw" class="text-center">Forward route</th>
                            <th id="head_back" class="text-center">Back route</th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="text-center" title="${pair.forwardRoute.toString()}">
                                <input type="hidden" name="forwid" value="${pair.forwardRoute.id}">
                                '${pair.forwardRoute.number}' [ID: ${pair.forwardRoute.id}]
                            </td>
                            <td class="text-center" title="${pair.backRoute.toString()}">
                                <input type="hidden" name="backid" value="${pair.backRoute.id}">
                                '${pair.backRoute.number}' [ID: ${pair.backRoute.id}]
                            </td>
                        </tr>
                    </table>
                    <div class="form-group text-center">
                        <input id="del_btn" class="btn btn-danger" type="submit" name="delete" value="Delete">
                        <a href="/pair/list" class="btn btn-default">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
