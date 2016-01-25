<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <link rel="stylesheet" href="<c:url value="../../resources/css/signin.css" />">
</head>
<body>
<jsp:include page="../fragments/menu.jsp"/>
<div class="row">
    <div class="container">
        <form class="form-signin" action="/login" method="post">

            <h2 class="form-signin-heading">Please sign in</h2>

            <label for="input-login" class="sr-only">Login</label>
            <input type="text" id="input-login" name="input-login" class="form-control" placeholder="Email/Login" value="${login}" pattern="[A-Za-z_0-9@.]{2,30}$" required autofocus>

            <label for="input-password" class="sr-only">Password</label>
            <input type="password" id="input-password" name="input-password" class="form-control" value="${password}" placeholder="Password" pattern="[A-Za-z_0-9]{4,16}" required>
            <div class="checkbox">
                <c:if test="${not empty remember}">
                    <c:set var="checked" value="checked" scope="page" />
                </c:if>
                <label><input type="checkbox" id="remember" name="remember" ${checked}> Remember me </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" id="signin-button" type="submit">LogIn</button>
            <a class="center-block" href="/signup">
                <span class="caption">Create a New Account</span>
            </a>

            <label class="col-sm-offset-2 label-error">${errorMessage}</label>
            <br />
            <c:if test="${not empty sameUser}">
                <label class="col-sm-offset-1">User: ${sameUser.userName}</label>
                <br />
                <label class="col-sm-offset-1">E-mail: ${sameUser.email}</label>
                <br />
                <label class="col-sm-offset-1">Password: ${sameUser.password}</label>
                <br />
            </c:if>
        </form>
    </div>
</div>
</body>
</html>
