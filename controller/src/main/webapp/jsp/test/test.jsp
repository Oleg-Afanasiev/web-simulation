<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  <title>test</title>
</head>
<body>
<h2>test</h2>
<c:forEach items="${maps}" var="map">
   <div>Map: '${map.name}' - ${map.describe} </div>
</c:forEach>

</body>
</html>
