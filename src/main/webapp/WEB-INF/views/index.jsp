<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 <%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Αρχική</title>
<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body>
	<div class="container" style="padding-left: 10%">
		<br>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3><b>Καλώς ήρθατε στην Ηλεκτρονική Πλατφόρμα Διαχείρισης Επιτροπών!</b></h3></div>
		</div>
		<br>
		 <div class="panel-body"><h4>Στη πλατφόρμα αυτή μπορείτε να βρείτε όλες τις απαραίτητες πλήροφορίες σχετικά με τις επιτροπές στις οποίες είστε εγγεγραμμένοι.</h4></div>
	</div>
	
</body>
<script
	src="<c:url value="/resources/bootstrap/js/jquery-3.1.1.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</html>
