<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Route add form</legend>
    <c:if test="${nameError eq 'routeError'}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-danger">${msgError}</p>
        </div>
    </c:if>
    <c:if test="${not empty saveRouteSuccess}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-success">Route was saved</p>
        </div>
    </c:if>
    <c:if test="${not empty arcSequencesForRoute}">
        <c:forEach items="${arcSequencesForRoute}" var="arc" varStatus="loop">
            <input type="hidden" name="arcSequencesForRoute${loop.index}" value="${arc.id}">
        </c:forEach>
    </c:if>
    <div class="form-group">
        <div class="col-sm-8 col-sm-offset-3">
            <div id="seq_nodes_accordion" class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading text-left">
                        <h6 class="panel-title">
                            <a data-toggle="collapse" data-parent="#seq_nodes_accordion" href="#collapse_seq_nodes" title="Show/Hide">
                                Stations: ${countSeqNodes}
                                <c:if test="${not empty firstNode}">
                                    "${firstNode.name}"
                                </c:if>
                                <c:if test="${not empty lastNode}">
                                    - ... - "${lastNode.name}"
                                </c:if>
                            </a>
                        </h6>
                    </div>
                    <div id="collapse_seq_nodes" class="panel-collapse collapse in">
                        <div class="panel-body text-left ">
                            <c:if test="${not empty nodeSequencesForRoute}">
                                <ul class="list-unstyled text-success">
                                    <c:forEach items="${nodeSequencesForRoute}" var="node">
                                        <li>"${node.name}" [ID: ${node.id}] -></li>
                                    </c:forEach>
                                </ul>
                                <input id="undock_arc_btn" class="btn btn-xs btn-info" name="undockArc" type="submit" value="Undock">
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group ${markNumberError}">
        <label for="number" class="col-sm-3 control-label">Number</label>
        <div class="col-sm-8">
            <input id="number" class="form-control" name="number" type="text" value="${number}">
        </div>
    </div>
    <div class="form-group ${markCostError}">
        <label for="cost" class="col-sm-3 control-label">Cost</label>
        <div class="col-sm-8">
            <input id="cost" class="form-control" name="cost" type="text" value="${cost}">
        </div>
    </div>
    <div class="form-group ${markFirstNodeError}">
        <label for="first_node" class="col-sm-3 control-label">First node</label>
        <div class="col-sm-8">
            <select id="first_node" class="form-control" name="selectFirstNodeId" onchange="this.form.submit()">
                <option value="0">---Select first Station---</option>
                <c:forEach items="${nodes}" var="firstNode">
                    <option value="${firstNode.id}" ${selectFirstNodeId == firstNode.id ? 'selected' : ''}>
                            ${firstNode.name} [ID: ${firstNode.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group ${markArcError}">
        <label for="arc" class="col-sm-3 control-label">Next arc</label>
        <div class="col-sm-8">
            <select id="arc" class="form-control" name="arcId">
                <option value="0">---Select next Arc---</option>
                <c:forEach items="${nextArcsForRoute}" var="arc">
                    <option value="${arc.id}" ${arcId == arc.id ? 'selected' : ''}>
                            ${arc.nodeLeft.name} [ID: ${arc.nodeLeft.id}] <-> ${arc.nodeRight.name} [ID: ${arc.nodeRight.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${not empty nextArcsForRoute}">
            <div class="col-sm-8 col-sm-offset-3 text-left">
                <input id="attach_arc_btn" class="btn btn-xs btn-info" name="attachArc" type="submit" value="Dock">
            </div>
        </c:if>
    </div>
    <div class="form-group">
        <label for="describe" class="col-sm-3 control-label">Description</label>
        <div class="col-sm-8">
            <textarea id="describe" class="form-control" name="routeDescribe" rows="3" cols="50">${routeDescribe}</textarea>
        </div>
    </div>
    <div class="form-group text-center">
        <input id="submit_route_button" class="btn btn-primary" type="submit" name="saveRoute" value="Save">
        <%--<a href="/route/list" class="btn btn-default">Cancel</a>--%>
    </div>
</fieldset>
