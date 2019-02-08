<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>

<P>  The time on the server is ${serverTime}. </P>

<sec:authorize access="isAuthenticated()">
	<a href="<c:url value="/users/all"/>">See All Users</a>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a href="<c:url value="/users/register"/>">Register a new User</a>
	</sec:authorize>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
<a href="<c:url value="/login"/>">Login Here</a>
</sec:authorize>

</body>
</html>
