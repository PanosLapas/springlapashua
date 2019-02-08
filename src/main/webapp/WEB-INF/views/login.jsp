<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<title>Login Page</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 100%;
	padding: 12%;
}
</style>
<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body>
<!-- onload='document.loginForm.username.focus();' -->
	
	<div class="container" id="login-box" >
		<div class="row vertical-offset-100">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Σύνδεση</h3>
					</div>
					<div class="panel-body">
						<c:if test="${not empty error}">
							<div class="error">${error}</div>
						</c:if>
						<c:if test="${not empty msg}">
							<div class="msg">${msg}</div>
						</c:if>
						<form accept-charset="UTF-8" role="form" name='loginForm' action="<c:url value='/login' />" method='POST'>
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="Username" name="username"
										type="text">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Password"
										name="password" type="password" value="">
								</div>
								<!-- <div class="checkbox">
									<label> <input name="remember-me" type="checkbox"
										value="Remember Me"> Remember Me
									</label>
								</div>  -->
								<input class="btn btn-lg btn-success btn-block" type="submit" name="submit"
									value="Σύνδεση">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>