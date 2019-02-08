<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js">
</script>
<title>Insert title here</title>
<link rel="shortcut-icon" href="/resources/images/favicon.jpg" type="image/x-icon">
<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<style>
.center-td {
	text-align: center;
}
.set-height {
	height: 55px;
}
</style>
</head>
<body>
	<div class="container" style="padding-left: 10%">
		<br>
		<div class="well">
				<div class="pull-left">
					<h4>Διαχείριση Χρηστών</h4>
				</div>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<div class="text-right">
						<form name='registerForm' action="<c:url value='/users/register' />">
							<button class="btn btn-large btn-success" type="submit" name="register"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Προσθήκη Χρήστη</button>
						</form>
					</div>
				</sec:authorize>
		</div>
		<div class="well">
			<form class="form-inline">
				<label>Όνομ/μο χρήστη ή Email :</label>
				<input type="text" name="username" id="Username" class="form-control"/>
				<div class="pull-right">
					<button type="button" class="btn btn-small btn-info" name="search" id="Search">
					   <span class="glyphicon glyphicon-search"></span> Αναζήτηση
					</button>
					<button type="button" class="btn btn-small btn-default" name="clear" id="Clear">
					   <span class="glyphicon glyphicon-remove"></span> Καθαρισμός
					</button>
				</div>
			</form>
			
		</div>
		<br>
		<div class="row-fluid">
			<c:set var="count" value="0"/>
			<c:set var="total" value="${total}"/>
			<c:set var="total_pages" value="${total_pages}"/>
			<c:set var="count_pages" value="0"/>
			<c:set var="users" value="${usersList}" />
			<span id="total_records" class="badge">Σύνολο : <c:out value="${total}" /></span>
			<br><br>
			<table class="table-striped table-condensed table-bordered"
				style="width: 100%;">
				<thead class="table-inverse">
					<tr>
						<th class="center-td">#</th>
						<th class="center-td">Επίθετο</th>
						<th class="center-td">Όνομα</th>
						<th class="center-td">Email</th>
						<!--<th class="center-td">Ρόλος</th>-->
						<th></th>
					</tr>
				</thead>
				<tbody id="tbodyid">
					<c:forEach items="${users}" var="user">
						<c:set var="count" value="${count + 1}"/>
						<tr>
							<td class="set-height"><c:out value="${count}" /></td>
							<td class="center-td set-height"><c:out value="${user.lastName}" /></td>
							<td class="center-td set-height"><c:out value="${user.firstName}" /></td>
							<td class="center-td set-height"><c:out value="${user.email}" /></td>
							<!-- <td class="center-td set-height"><c:out value="${user.roleName}" /></td>-->
							<td class="center-td set-height">
								<a
									href="<c:url value="/users/user/${user.id}"/>">
									<i class="glyphicon glyphicon-pencil"></i>
								</a>
								<c:if test="${user.inCommitte == false}" >
										&nbsp; 
									<a onclick='showModal(<c:out value="${user.id}" />)'>
										<i class="glyphicon glyphicon-trash" style="color: red;"></i>
									</a>
								</c:if>
							</td>
						</tr>
						
						<div id="myModal_<c:out value="${user.id}" />" class="modal fade firstModals" role="dialog">
						    <div class="modal-dialog modal-sm">
						        <div class="modal-content">
						            <div class="modal-header btn-warning" style="font-weight:bold;color:white;">
						                <button type="button" class="close" data-dismiss="modal">&times;</button>
						                <h5 class="modal-title modal-sm">Προσοχή!</h5>
						            </div>
						            <div class="modal-body">
						                <p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <b><strong>ΟΡΙΣΤΙΚΗ</strong></b> διαγραφή του χρήστη? <br> Πατήστε 'Ακύρωση' για να ακυρώσετε την ενέργεια.</p>
						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-danger DeleteBtnID" value="<c:url value="${user.id}"/>"  data-dismiss="modal">Διαγραφή</button>
						                <button type="button" class="btn btn-default" data-dismiss="modal">Ακύρωση</button>
						            </div>
						        </div>
						    </div>
						</div>
						
					</c:forEach>
				</tbody>
			</table>
			<center>
				<ul id="pagination" class="pagination">
					 <c:forEach var="i" begin="1" end="${total_pages}">
						<li><a onclick="loadPage(${i})"><c:out value="${i}" /></a></li>
					</c:forEach>
				</ul>
			</center>
		</div>
	</div>

</body>
<script>
	$(document).ready(function() {
		$('.menu-ul li.active').removeClass('active');
		$('#users').addClass('active');
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/users/delete/' + $(this).val();
			window.location = url;
		});
		
		$('#Clear').on('click', function () {
			var url = '/committee/users/all' + $(this).val();
			window.location = url;
		});
		
		$('#Search').on('click', function () {
			var username=document.getElementById('Username').value;
			if((username===""))
			{
				//do nothing - no filter selected
			}
			else
			{
				var postdata={username : username};
				$.ajax({
			        type : 'GET',
			        url : 'searchusers',
			        dataType : 'json',
			        data : postdata,
			        success : function(result) {
			           showUsersBySearch(result,1);
			        },
			        error : function() {
			            alert('error');
			        }
			    });
			}
			
		});
		
	});
	
	function loadPage(page)
	{
		var pg = page;
		var postdata = 'page=' + encodeURIComponent(pg); 
		
		$.ajax({
	        type : 'GET',
	        url : 'load',
	        dataType : 'json',
	        data : postdata,
	        success : function(result) {
	           showUsers(result,page);
	        },
	        error : function() {
	            alert('error');
	        }
	    });
	}
	
	function showModal(id)
	{
		var modal = '#myModal_' + id;
		$(modal).modal(); 
	}
	
	function showUsers(users,page)
	{
		$("#tbodyid").empty();
		$('.firstModals').remove();
		var count = (page -1) * 5;
		for ( var i = 0, len = users.length; i < len; ++i) 
		{
			
			var num = count + i + 1;
            var user = users[i];
            var str="";
            if(user.inCommitte === false)
           	{
           		str="&nbsp <a onclick='showModal(" + user.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a>";
           	}
            $('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
            			"<td class='center-td set-height'>" + user.lastName + "</td>" +
            			"<td class='center-td set-height'>" + user.firstName + "</td>" +
            			"<td class='center-td set-height'>" + user.email + "</td>" +
            			"<td class='center-td set-height'><a href='<c:url value='/users/user/" + user.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
            			str +
            			"</td>" + 
            			"</tr>"
            );
            //"<td class='center-td set-height'>" + user.roleName + "</td>" +
            
            var mod = "<div id='myModal_" + user.id + "' class='modal fade' role='dialog'>" +
				"<div class='modal-dialog modal-sm'>" +
					"<div class='modal-content'>" +
						"<div class='modal-header btn-warning' style='font-weight:bold;color:white;'>" +
							"<button type='button' class='close' data-dismiss='modal'></button>" +
							"<h5 class='modal-title modal-sm'>Προσοχή!</h5>" +
						"</div>" +
						"<div class='modal-body'>" +
							"<p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <strong>ΟΡΙΣΤΙΚΗ</strong> διαγραφή του χρήστη? Πατήστε Ακύρωση για να ακυρώσετε την ενέργεια.</p>" +
						"</div>" +
						"<div class='modal-footer'>" +
							"<button type='button' class='btn btn-danger DeleteBtnID' value='" + user.id + "' data-dismiss='modal'>Διαγραφή</button>" +
							"<button type='button' class='btn btn-default' data-dismiss='modal'>Ακύρωση</button>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>";
			
            $('#tbodyid').append(mod);
    	}
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/users/delete/' + $(this).val();
			window.location = url;
		});
	}
	
	function showUsersBySearch(users,page)
	{
		$("#tbodyid").empty();
		$('.firstModals').remove();
		var count = (page -1) * 5;
		
		if(users.length > 0)
		{
			document.getElementById('total_records').innerHTML = "Σύνολο : " + users[0].total;
		}
		else
		{
			document.getElementById('total_records').innerHTML = "Σύνολο : 0";
		}
		var element = document.getElementById('pagination');
		if (typeof(element) != 'undefined' && element != null)
		{
			document.getElementById('pagination').style.display="none";
		}
		
		
		for ( var i = 0, len = users.length; i < len; ++i) 
		{
			var num = count + i + 1;
            var user = users[i];
            var str="";
            if(user.inCommitte === false)
           	{
           		str="&nbsp <a onclick='showModal(" + user.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a>";
           	}
            $('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
            			"<td class='center-td set-height'>" + user.lastName + "</td>" +
            			"<td class='center-td set-height'>" + user.firstName + "</td>" +
            			"<td class='center-td set-height'>" + user.email + "</td>" +
            			"<td class='center-td set-height'><a href='<c:url value='/users/user/" + user.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
            			str +
            			"</td>" + 
            			"</tr>"
            );
            //"<td class='center-td set-height'>" + user.roleName + "</td>" +
            
            var mod = "<div id='myModal_" + user.id + "' class='modal fade' role='dialog'>" +
				"<div class='modal-dialog modal-sm'>" +
					"<div class='modal-content'>" +
						"<div class='modal-header btn-warning' style='font-weight:bold;color:white;'>" +
							"<button type='button' class='close' data-dismiss='modal'></button>" +
							"<h5 class='modal-title modal-sm'>Προσοχή!</h5>" +
						"</div>" +
						"<div class='modal-body'>" +
							"<p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <strong>ΟΡΙΣΤΙΚΗ</strong> διαγραφή του χρήστη? Πατήστε Ακύρωση για να ακυρώσετε την ενέργεια.</p>" +
						"</div>" +
						"<div class='modal-footer'>" +
							"<button type='button' class='btn btn-danger DeleteBtnID' value='" + user.id + "' data-dismiss='modal'>Διαγραφή</button>" +
							"<button type='button' class='btn btn-default' data-dismiss='modal'>Ακύρωση</button>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>";
			
            $('#tbodyid').append(mod);
    	}
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/users/delete/' + $(this).val();
			window.location = url;
		});
	}
	
</script>
<script
	src="<c:url value="/resources/bootstrap/js/jquery-3.1.1.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</html>