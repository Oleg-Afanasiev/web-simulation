<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
    <legend>Arc add form</legend>
    <c:if test="${nameError eq 'arcError'}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-danger">${msgError}</p>
        </div>
    </c:if>
    <c:if test="${not empty saveArcSuccess}">
        <div class="col-xs-10 col-xs-offset-1">
            <p class="text-success">Arc was saved</p>
        </div>
    </c:if>
    <div class="form-group ${markNodeLeftError}">
        <label for="node_left" class="col-sm-3 control-label">Node left</label>
        <div class="col-sm-8">
            <select id="node_left" class="form-control" name="nodeLeftId">
                <option value="0">---Select left node---</option>
                <c:forEach items="${nodes}" var="nodeLeft">
                    <option value="${nodeLeft.id}" ${nodeLeftId == nodeLeft.id ? 'selected' : ''}>
                            ${nodeLeft.name} [ID: ${nodeLeft.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group ${markNodeRightError}">
        <label for="node_right" class="col-sm-3 control-label">Node right</label>
        <div class="col-sm-8">
            <select id="node_right" class="form-control" name="nodeRightId">
                <option value="0">---Select right node---</option>
                <c:forEach items="${nodes}" var="nodeRight">
                    <option value="${nodeRight.id}" ${nodeRightId == nodeRight.id ? 'selected' : ''}>
                            ${nodeRight.name} [ID: ${nodeRight.id}]
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group ${markNodeDurError}">
        <label for="duration" class="col-sm-3 control-label">Duration</label>
        <div class="col-sm-8">
            <input id="duration" class="form-control" name="duration" type="text" value="${duration}">
        </div>
    </div>
    <div class="form-group text-center">
        <input id="submit_save_arc_button" class="btn btn-primary" type="submit" name="saveArc" value="Save">
    </div>
</fieldset>

