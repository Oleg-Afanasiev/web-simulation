<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="add_form" class="form-horizontal" name="search" action="/map/passport" method="POST">
    <input type="hidden" name="id" value="${id}">
    <div class="form-group">
        <div class="col-md-5">
            <div class="input-group">
                <span class="input-group-addon">From:</span>
                <select id="from" class="form-control" name="fromNode">
                    <option value="0">---Select from---</option>
                    <c:forEach items="${nodes}" var="nodeFrom">
                        <option value="${nodeFrom.id}" ${nodeFromId == nodeFrom.id ? 'selected' : ''}>
                                ${nodeFrom.name} [ID: ${nodeFrom.id}]
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="col-md-5">
            <div class="input-group">
                <span class="input-group-addon">To:</span>
                <select id="to" class="form-control" name="toNode">
                    <option value="0">---Select to---</option>
                    <c:forEach items="${nodes}" var="nodeTo">
                        <option value="${nodeTo.id}" ${nodeToId == nodeTo.id ? 'selected' : ''}>
                                ${nodeTo.name} [ID: ${nodeTo.id}]
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="col-md-2">
            <input class="btn btn-default" type="submit" name="search" value="Search" />
        </div>
    </div>
</form>

<c:if test="${not empty searchRes}">
    <div class="text text-info">
        <c:forEach items="${searchRes}" var="node">
            <span>"${node.name} [ID: ${node.id}]"</span>
            <span class="glyphicon glyphicon-arrow-right"></span>
        </c:forEach>
        <span>total: ${time} min</span>
    </div>
</c:if>
