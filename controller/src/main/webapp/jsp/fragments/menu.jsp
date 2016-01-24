<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/">Simulate system</a>
    </div>
  <ul class="nav navbar-nav">
    <li data="dropdown">
      <a data-toggle="dropdown" class="dropdown-toggle" href="/net/list">Сети маршрутов <b class="caret"></b></a>
      <ul role="menu" class="dropdown-menu">
        <li><a href="/station/list">Остановки</a></li>
        <li><a href="/arc/list">Перегоны</a></li>
        <li><a href="/route/list">Маршруты</a></li>
        <li><a href="/net/list">Сети</a></li>
      </ul>
    </li>

    <li class="dropdown">
      <a data-toggle="dropdown" class="dropdown-toggle" href="/station/list">Правила <b class="caret"></b></a>
      <ul role="menu" class="dropdown-menu">
        <li><a href="/rule/busrun">Движения автобусов</a></li>
        <li><a href="/rule/passgeneration">Генерации пассажиров</a></li>
        <%--<li><a href="/rule/passgeneration">Другие</a></li>--%>
      </ul>
    </li>
    <li><a href="/simulation">Симуляция</a></li>
  </ul>
    <ul class="nav navbar-nav navbar-right">
      <c:choose>
        <c:when  test="${empty activeUser}">
          <li><a href="/login">Войти</a></li>
        </c:when>
        <c:otherwise>
          <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle" href="#">${activeUser.userName} <b class="caret"></b></a>
            <ul role="menu" class="dropdown-menu">
              <li><a href="/logout">Выйти</a></li>
            </ul>
          </li>
        </c:otherwise>
      </c:choose>
    </ul>
  </div>
</nav>
