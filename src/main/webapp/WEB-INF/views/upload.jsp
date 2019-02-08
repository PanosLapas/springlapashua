<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<html>
<head>
<title>Upload File Request Page</title>
</head>
<body>
	<form:form method="POST" action="/first/committe/upload" enctype="multipart/form-data">
		Filename: <input type="text" name="name">
		File to upload: <input type="file" name="file">
		<input type="submit" value="Upload"> Press here to upload the file!
	</form:form>	
</body>
</html>