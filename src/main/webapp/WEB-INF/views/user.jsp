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
		<c:if test="${user.id == 0}" >
			 <h4>Εισαγωγή νέου χρήστη</h4> 
		</c:if>
		<c:if test="${user.id > 0}" >
			 <h4>Τροποποίηση χρήστη</h4> 
		</c:if>
	</div>
	<div class="alert alert-info">
	  <strong>Tip!</strong> Προσθέστε νέο χρήστη στην εφαρμογή, κάνοντας αναζήτηση με το username του, επιβεβαιώστε τα στοιχεία και πατήστε 'Προσθήκη'.
	</div>
	<br>
	<div class="row-fluid">
		<form:form method="POST" action="/committee/users/register" modelAttribute="user" name="userForm">
			<form:input path="id" type="text" hidden="hidden"/>
			<div class="col-sm-12">
				<div class="row">
					<div class="col-sm-6 form-group">
						<label>Όνομα</label>
						<form:input path="firstName" type="text" id="first_name"
							placeholder="Όνομα.." class="form-control" readonly="true"/>
					</div>
					<div class="col-sm-6 form-group">
						<label>Επίθετο</label> 
						<form:input path="LastName" type="text" id="last_name"
							placeholder="Επίθετο.." class="form-control" readonly="true"/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-8 form-group">
						<label>Email</label> 
						<c:if test="${user.id == 0}" >
							<form:input path="email" type="text" id="email"
							placeholder="Email.." class="form-control"/>
						</c:if>
						<c:if test="${user.id > 0}" >
							<form:input path="email" type="text" id="email"
							placeholder="Email.." class="form-control" readonly="true"/>
						</c:if>
					</div>
					<div class="col-sm-2 form-group">
						<c:if test="${user.id == 0}" >
							<label>Αναζήτηση σε LDAP</label> 
							<input type="button" class="btn btn-small btn-info form-control" name="search" id="Search" value="Αναζήτηση"/>
						</c:if>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6 form-group">
						<label>Username</label> 
							<c:if test="${user.id == 0}" >
								<form:input path="Username" type="text" id="user_name"
								placeholder="Username.." class="form-control" readonly="true"/>
							</c:if>
							<c:if test="${user.id > 0}" >
								<form:input path="Username" type="text" id="user_name"
								placeholder="Username.." class="form-control"/>
							</c:if>
					</div>
					<div class="col-sm-6 form-group">
						<label>Password</label> 
							<c:if test="${user.id == 0}" >
								<form:input path="password" type="password" id="passwd"
								placeholder="Password.." class="form-control" readonly="true"/>
							</c:if>
							<c:if test="${user.id > 0}" >
								<form:input path="password" type="password" id="passwd"
								placeholder="Password.." class="form-control"/>
							</c:if>
					</div>
				</div>
				<c:if test="${user.id == 0}" >
				 <input type="submit" class="btn btn-lg btn-success" value="Προσθήκη" id="add" disabled="true"/> 
				</c:if>
				<c:if test="${user.id > 0}" >
				 <input type="submit" class="btn btn-lg btn-success" value="Αποθήκευση"/> 
				</c:if>
				<input type="button" class="btn btn-lg btn-danger" onclick="location.href='/committee/users/all'" value="Ακύρωση"/>
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
													required : true//,
													//email : true
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
						
						$('#Search').on('click', function () {
							
							var email=document.getElementById('email').value;
							if((email===""))
							{
								//do nothing - no filter selected
							}
							else
							{
								
								var postdata={email : email};
								$.ajax({
							        type : 'GET',
							        url : 'searchldap',
							        dataType : 'json',
							        data : postdata,
							        success : function(result) {
							           showLDAPUser(result);
							        },
							        error : function() {
							            alert('error');
							        }
							    });
							}
							
						});
						
						function showLDAPUser(user)
						{
							var first_name = document.getElementById('first_name');
							var last_name = document.getElementById('last_name');
							var email = document.getElementById('email');
							var user_name = document.getElementById('user_name');
							var password = document.getElementById('passwd');
							
							oFormObject = document.forms['userForm'];

							oFormObject.elements["last_name"].value = user.lastName;
							oFormObject.elements["first_name"].value = user.firstName;
							oFormObject.elements["email"].value = user.email;
							oFormObject.elements["user_name"].value = user.username;
							oFormObject.elements["passwd"].value = user.password;
							//first_name.value=user.firstName;
							//last_name.value=user.lastName;
							//email.value=user.email;
							//user_name.value=user.username;
							//password.value=user.password;
							document.getElementById('add').disabled=false;
						}

					});
</script>
