<%--
  Created by IntelliJ IDEA.
  User: oleg
  Date: 1/20/16
  Time: 12:46 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
      <div class="panel panel-info text-center">
        <div class="panel-heading">
          <h3 class="panel-title">Add station form</h3>
        </div>
        <div class="panel-body">
          <form id="add_station_form" class="form-horizontal" name="addStation" action="/station/add" method="POST">
            <c:if test="${not empty msgError}">
              <div class="col-xs-10 col-xs-offset-1">
                <p class="text-danger">${msgError}</p>
              </div>
              <c:set var="mrkError" value="has-error" />
            </c:if>
            <div class="form-group ${mrkError}">
              <label for="station_name" class="col-sm-3 control-label">Name</label>
              <div class="col-sm-8">
                <input id="station_name" class="form-control" name="stationName" type="text" value="${name}">
              </div>
            </div>
            <div class="form-group text-center">
              <input id="save_button" class="btn btn-primary" type="submit" name="saveStation" value="Save">
              <a href="/station/list" class="btn btn-default">Cancel</a>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
