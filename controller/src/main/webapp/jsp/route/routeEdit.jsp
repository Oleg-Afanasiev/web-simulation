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
    <script type="text/javascript" src="<c:url value="../resources/js/route/routeEdit.js"/> "></script>
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<c:if test="${not empty nameError}">
    <c:choose>
        <c:when test="${nameError eq 'nullNumberErr'}">
            <c:set var="nullNumberError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'costFmtErr'}">
            <c:set var="markCostError" value="has-error" scope="page" />
        </c:when>
        <c:when test="${nameError eq 'negativeCostErr'}">
            <c:set var="markCostError" value="has-error" scope="page" />
        </c:when>
    </c:choose>
</c:if>
<div class="row">
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Route edit form</h3>
            </div>
            <div class="panel-body">
                <form id="edit_form" class="form-horizontal" name="edit" action="/route/edit" method="POST">
                    <c:if test="${not empty arcSeq}">
                        <c:forEach items="${arcSeq}" var="arc" varStatus="loop">
                            <input type="hidden" name="arcSeq${loop.index}" value="${arc.id}">
                        </c:forEach>
                    </c:if>
                    <c:if test="${not empty msgError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <p class="text-danger">${msgError}</p>
                        </div>
                        <c:set var="mrkError" value="has-error" />
                    </c:if>
                    <div class="form-group">
                        <div class="col-sm-8 col-sm-offset-3">
                            <div id="seq_accordion" class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading text-left">
                                        <h6 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#seq_accordion" href="#collapse_seq" title="Show/Hide">
                                                Stations.  Count: ${countSeqNodes}. &nbsp;
                                                <c:if test="${not empty firstNode}">
                                                    "${firstNode.name}"
                                                </c:if>
                                                <c:if test="${not empty lastNode}">
                                                    - ... - "${lastNode.name}"
                                                </c:if>
                                            </a>
                                        </h6>
                                    </div>
                                    <div id="collapse_seq" class="panel-collapse collapse in">
                                        <div class="panel-body text-left ">
                                            <c:if test="${not empty nodeSeq}">
                                                <ul class="list-unstyled text-success">
                                                    <c:forEach items="${nodeSeq}" var="node">
                                                        <li>"${node.name}" [ID: ${node.id}] -></li>
                                                    </c:forEach>
                                                </ul>
                                                <%--<input id="undock_arc_btn" class="btn btn-xs btn-info" name="undock" type="submit" value="Undock">--%>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="route_id" class="col-sm-3 control-label">ID</label>
                        <div class="col-sm-8">
                            <input id="route_id" class="form-control disabled" type="text" name="id" readonly="readonly" value="${id}">
                        </div>
                    </div>
                    <div class="form-group ${nullNumberError}">
                        <label for="route_number" class="col-sm-3 control-label">Number</label>
                        <div class="col-sm-8">
                            <input id="route_number" class="form-control" type="text" name="number" value="${number}">
                        </div>
                    </div>
                    <div class="form-group ${markCostError}">
                        <label for="route_cost" class="col-sm-3 control-label">Cost</label>
                        <div class="col-sm-8">
                            <input id="route_cost" class="form-control" type="text" name="cost" value="${cost}">
                        </div>
                    </div>
                    <div class="form-group ${markDescriptionError}">
                        <label for="description" class="col-sm-3 control-label">Description</label>
                        <div class="col-sm-8">
                            <input id="description" class="form-control" name="description" type="text" value="${description}">
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <input id="edit_btn" class="btn btn-primary" type="submit" name="save" value="Save">
                        <input id="del_btn" class="btn btn-danger" type="submit" name="delete" value="Delete">
                        <a href="/route/list" class="btn btn-default">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
