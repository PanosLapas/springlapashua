<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8" charset="utf8" >
</head>
<div class="container" style="padding-left: 10%">
	<br>
	<div class="well">
			 <h4>Τροποποίηση λογαριασμού</h4> 
	</div>
	<br>
	<div class="row-fluid">
		<form:form method="POST" action="/committee/users/profile" modelAttribute="user" name="userForm">
			<form:input path="id" type="text" hidden="hidden"/>
			<div class="col-sm-12">
				<div class="row">
					<div class="col-sm-6 form-group">
						<label>Όνομα</label>
						<form:input path="firstName" type="text"
							placeholder="Όνομα.." class="form-control"/>
					</div>
					<div class="col-sm-6 form-group">
						<label>Επίθετο</label> 
						<form:input path="LastName" type="text"
							placeholder="Επίθετο.." class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label>Email</label> 
					<form:input path="email" type="text" readonly="true"
						placeholder="Email.." class="form-control"/>
				</div>
				<div class="row">
					<div class="col-sm-6 form-group">
						<label>Username</label> <form:input path="Username" type="text"
							placeholder="Username.." class="form-control"/>
					</div>
					<div class="col-sm-6 form-group">
						<label>Password</label> <form:input path="password" type="password"
							placeholder="Password.." class="form-control"/>
					</div>
				</div>
				<c:if test="${user.id == 0}" >
				 <input type="submit" class="btn btn-lg btn-success" value="Προσθήκη"/> 
				</c:if>
				<c:if test="${user.id > 0}" >
				 <input type="submit" class="btn btn-lg btn-success" value="Αποθήκευση"/> 
				</c:if>
				<input type="button" class="btn btn-lg btn-danger" onclick="location.href='/committee'" value="Ακύρωση"/>
			</div>
		</form:form>
	</div>
</div>
<script
	src="<c:url value="/resources/bootstrap/js/jquery-3.1.1.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/jquery.validate.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap-datepicker.js" />"></script>
<script type="text/javascript">
	$(document).ready(
					function() {
						
						$('.menu-ul li.active').removeClass('active');
						$('#account').addClass('active');
						
						$("form[name='userForm']")
								.validate(
										{
											// Specify validation rules
											rules : {
												// The key name on the left side is the name attribute
												// of an input field. Validation rules are defined
												// on the right side
												firstName : {
													required : true
												},
												LastName : {
													required : true
												},
												Username : {
													required : true
												},
												password : {
													required : true,
													minlength : 5
												},
												email : {
													required : true,
													email : true
												}
											},
											// Specify validation error messages
											messages : {
												firstName : {
													required : "Παρακαλώ δώστε όνομα!"
												},
												LastName : {
													required : "Παρακαλώ δώστε επίθετο!"
												},
												Username : {
													required : "Παρακαλώ δώστε κωδικό χρηστή!"
												},
												password: {
											        required: "Παρακαλώ δώστε ένα κωδικό προσβάσης!",
											        minlength: "Ο κωδικός πρέπει να έχει τουλάχιστον 5 χαρακτήρες!"
											      },
											    email: "Παρακαλώ δώστε ένα έγκυρο email!"
											},
											// Make sure the form is submitted to the destination defined
											// in the "action" attribute of the form when valid
											submitHandler : function(form) {
												form.submit();
											}
										});

					});
</script>
