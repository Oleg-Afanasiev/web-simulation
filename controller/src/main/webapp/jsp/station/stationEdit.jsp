<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <jsp:include page="../fragments/headTag.jsp"/>
  <script type="text/javascript" src="<c:url value="../resources/js/station/stationEdit.js"/> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="row">
  <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
    <div class="panel panel-info text-center">
      <div class="panel-heading">
        <h3 class="panel-title">Station edit form</h3>
      </div>
      <div class="panel-body">
        <form id="edit_form" class="form-horizontal" name="editStation" action="/station/edit" method="POST">
          <c:if test="${not empty msgError}">
            <div class="col-xs-10 col-xs-offset-1">
              <p class="text-danger">${msgError}</p>
            </div>
            <c:set var="mrkError" value="has-error" />
          </c:if>
          <div class="form-group">
            <label for="station_id" class="col-sm-3 control-label">ID</label>
            <div class="col-sm-8">
              <input id="station_id" class="form-control disabled" type="text" name="id" readonly="readonly" value="${id}">
            </div>
          </div>
          <div class="form-group ${mrkError}">
            <label for="station_name" class="col-sm-3 control-label">Name</label>
            <div class="col-sm-8">
              <input id="station_name" class="form-control" name="name" type="text" value="${name}">
            </div>
          </div>
          <div class="form-group text-center">
            <input id="edit_btn" class="btn btn-primary" type="submit" name="saveStation" value="Save">
            <input id="del_btn" class="btn btn-danger" type="submit" name="deleteStation" value="Delete">
            <a href="/station/list" class="btn btn-default">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
</body>
</html>
