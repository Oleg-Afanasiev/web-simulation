<%--
  Created by IntelliJ IDEA.
  User: oleg
  Date: 1/19/16
  Time: 6:47 PM
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
<c:if test="${not empty paramNameErr}">
    <c:choose>
        <c:when test="${paramNameErr eq 'userNameErr'}">
            <c:set var="markUserNameError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'firstNameErr'}">
            <c:set var="markFirstNameError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'lastNameErr'}">
            <c:set var="markLastNameError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'emailErr'}">
            <c:set var="markEmailError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'passwErr'}">
            <c:set var="markPasswError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'passwRepeatErr'}">
            <c:set var="markPasswRepeatError" value="has-error" scope="page" />
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'passwNotEqErr'}">
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
        <c:when test="${paramNameErr eq 'existUser'}">
            <c:set var="msgError" value="${msgErr}" scope="page" />
        </c:when>
    </c:choose>
</c:if>
<div class="row">
    <div class="col-xs-10 col-sm-6  col-xs-offset-1 col-sm-offset-3 main">
        <div class="panel panel-info text-center">
            <div class="panel-heading">
                <h3 class="panel-title">Fill form for your account</h3>
            </div>
            <div class="panel-body">
                <form id="sign_up_form" class="form-horizontal" name="signUp" action="/signup" method="POST">

                    <div class="col-xs-10 col-xs-offset-1">
                        <p class="text-danger">${msgError}</p>
                    </div>
                    <div class="form-group ${markUserNameError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="user_name" class="form-control" name="userName" type="text" value="${userName}" placeholder="User name">
                        </div>
                    </div>
                    <div class="form-group ${markFirstNameError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="first_name" class="form-control" name="firstName" type="text" value="${firstName}" placeholder="First name">
                        </div>
                    </div>
                    <div class="form-group ${markLastNameError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="last_name" class="form-control" name="lastName" type="text" value="${lastName}" placeholder="Last name">
                        </div>
                    </div>
                    <div class="form-group ${markEmailError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="email" class="form-control" name="email" type="text" value="${email}" placeholder="Email">
                        </div>
                    </div>
                    <div class="form-group ${markPasswError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="password" class="form-control" name="password" type="password" value="${password}" placeholder="Password">
                        </div>
                    </div>
                    <div class="form-group ${markPasswRepeatError}">
                        <div class="col-xs-10 col-xs-offset-1">
                            <input id="password_repeat" class="form-control" name="passwordRepeat" type="password" value="${passwordRepeat}" placeholder="Repeat password">
                        </div>
                    </div>
                    <div class="form-group last border-top">
                        <div class="col-xs-6 col-xs-offset-3">
                            <input class="form-control btn btn-info" type="submit" value="Create Account">
                        </div>
                    </div>
                </form>
                <h5 class="login">
                    Already have an account?
                    <a href="/login">Log In</a>
                </h5>
            </div>
        </div>
    </div>
</div>
</body>
</html>
