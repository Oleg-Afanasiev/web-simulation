<%--
  Created by IntelliJ IDEA.
  User: oleg
  Date: 1/25/16
  Time: 7:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Route add form</legend>
    <div class="form-group">
        <div class="col-sm-8 col-sm-offset-3">
            <div id="seq_nodes_accordion" class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading text-left">
                        <h6 class="panel-title">
                            <a data-toggle="collapse" data-parent="#seq_nodes_accordion" href="#collapse_seq_nodes" title="Show/Hide">
                                Stations: ${countSeqNodes}. &nbsp;
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
                            <c:if test="${not empty nodeSeq}">
                                <ul class="list-unstyled text-success">
                                    <c:forEach items="${nodeSeq}" var="node">
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
            <select id="first_node" class="form-control" name="firstNodeId" onchange="this.form.submit()">
                <option value="0">---Select first Station---</option>
                <c:forEach items="${nodes}" var="firstNode">
                    <option value="${firstNode.id}" ${firstNodeId == firstNode.id ? 'selected' : ''}>
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
                <c:forEach items="${arcs}" var="arc">
                    <option value="${arc.id}" ${arcId == arc.id ? 'selected' : ''}>
                            ${arc.nodeLeft.name} [ID: ${arc.nodeLeft.id}] <-> ${arc.nodeRight.name} [ID: ${arc.nodeRight.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${not empty arcs}">
            <div class="col-sm-8 col-sm-offset-3 text-left">
                <input id="attach_arc_btn" class="btn btn-xs btn-info" name="attachArc" type="submit" value="Dock">
            </div>
        </c:if>
    </div>
    <div class="form-group">
        <label for="describe" class="col-sm-3 control-label">Description</label>
        <div class="col-sm-8">
            <textarea id="describe" class="form-control" name="describe" rows="3" cols="50">${describe}</textarea>
        </div>
    </div>
    <div class="form-group text-center">
        <input id="submit_button" class="btn btn-primary" type="submit" name="save" value="Save">
        <a href="/route/list" class="btn btn-default">Cancel</a>
    </div>
</fieldset>
