<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
</head>
<body>
<div class="container" style="padding-left: 10%">
		<br>
		<div class="well">
					<div class="pull-left">
						<h4>Διαχείριση Επιτροπών</h4>
					</div>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<div class="text-right">
						<form name='registerForm' action="<c:url value='/committe/create' />">
							<button class="btn btn-large btn-success" type="submit" name="register"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Προσθήκη Επιτροπής</button>
						</form>
					</div>
				</sec:authorize>
		</div>
		<div class="well">
			<form class="form-inline">
				<label>Ημ/νία Συνάντησης Από: </label>
				<div class="input-group input-append date">
					<input id="datePicker" type="text"
						class="form-control meetingDate" name="MeetingDate"/>
				</div>
				<input type="hidden" name="startdate" id="TodayDate"/>
				<label>Έως: </label>
				<div class="input-group input-append date">
					<input id="datePicker_2" type="text"
						class="form-control meetingDate" name="MeetingDate_2"/>
				</div>
				<input type="hidden" name="startdate" id="TodayDate_2"/>
				<label>Τίτλος :</label>
				<input type="text" name="Title" id="Title" class="form-control"/>
			</form>
			<br>
			<div class="pull-right">
				<button type="button" class="btn btn-small btn-info" name="search" id="Search">
				   <span class="glyphicon glyphicon-search"></span> Αναζήτηση
				</button>
				<button type="button" class="btn btn-small btn-default" name="clear" id="Clear">
				   <span class="glyphicon glyphicon-remove"></span> Καθαρισμός
				</button>
			</div>
			<br>
		</div>
		<br>
		<div class="row-fluid">
			<c:set var="count" value="0"/>
			<c:set var="total" value="${total}"/>
			<c:set var="total_pages" value="${total_pages}"/>
			<c:set var="count_pages" value="0"/>
			<c:set var="comms" value="${committesList}" />
			<span id="total_records" class="badge">Σύνολο : <c:out value="${total}" /></span>
			<table class="table-striped table-condensed table-bordered"
				style="width: 100%;">
				<thead class="table-inverse">
					<tr>
						<th class="center-td">#</th>
						<th class="center-td">Τίτλος</th>
						<th class="center-td">Ημ/νία Συνάντησης</th>
						<th class="center-td">Αρχεία</th>
						<!-- <th class="center-td">Διαχειριστής</th> -->
						<th></th>
					</tr>
				</thead>
				<tbody id="tbodyid">
					<c:forEach items="${comms}" var="comm">
						<c:set var="count" value="${count + 1}"/>
						<tr>
							<td><c:out value="${count}" /></td>
							<td class="center-td"><c:out value="${comm.committe.title}" /></td>
							<td class="center-td"><fmt:formatDate value="${comm.committe.meetingDate}" pattern="dd/MM/yyyy" /></td>
							<td class="center-td">
								<c:choose>
								  <c:when test="${comm.documents.size() <= 1}">
								  	<c:forEach items="${comm.documents}" var="doc">
									   <a href="<c:url value="/committe/download/${doc.id}" />"><c:out value="${doc.title}" />&nbsp;<i class='glyphicon glyphicon-download-alt'></i></a>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								  		<a href="<c:url value="/committe/downloadAll/${comm.committe.id}" />"><c:out value="${comm.documents.size()} αρχεία" />&nbsp;<i class='glyphicon glyphicon-download-alt'></i></a>
								  </c:otherwise>
								</c:choose>
							</td>
							<td class="center-td"><a
								href="<c:url value="/committe/modify/${comm.committe.id}"/>"><i
									class="glyphicon glyphicon-pencil"></i></a>&nbsp; 
								<a class="pull-right" onclick='showModal(<c:out value="${comm.committe.id}" />)'>
									<i class="glyphicon glyphicon-trash" style="color: red;"></i>
								</a>
							</td>
						</tr>
						<div id="myModal_<c:out value="${comm.committe.id}" />" class="modal fade firstModals" role="dialog">
						    <div class="modal-dialog modal-sm">
						        <div class="modal-content">
						            <div class="modal-header btn-warning" style="font-weight:bold;color:white;">
						                <button type="button" class="close" data-dismiss="modal">&times;</button>
						                <h5 class="modal-title modal-sm">Προσοχή!</h5>
						            </div>
						            <div class="modal-body">
						                <p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <strong>ΟΡΙΣΤΙΚΗ</strong> διαγραφή αυτής της επιτροπής (<b><i>${comm.committe.title}</i></b>)? <br> Πατήστε 'Ακύρωση' για να ακυρώσετε την ενέργεια.</p>
						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-danger DeleteBtnID" value="<c:url value="${comm.committe.id}"/>"  data-dismiss="modal">Διαγραφή</button>
						                <button type="button" class="btn btn-default" data-dismiss="modal">Ακύρωση</button>
						            </div>
						        </div>
						    </div>
						</div>
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${total > 5}" >
				<center>
					<ul class="pagination" id="pagination">
						 <c:forEach var="i" begin="1" end="${total_pages}">
							<li><a onclick="loadPage(${i})"><c:out value="${i}" /></a></li>
						</c:forEach>
					</ul>
				</center>
			</c:if>
		</div>
	</div>
</body>
<script>
	$(document).ready(function() {
		$('.menu-ul li.active').removeClass('active');
		$('#committes').addClass('active');
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/committe/delete/' + $(this).val();
			window.location = url;
		});
		
		$('#Clear').on('click', function () {
			var url = '/committee/committe/all' + $(this).val();
			window.location = url;
		});
		
		$('#Search').on('click', function () {
			
			var date_from=document.getElementById('datePicker').value;
			var date_to=document.getElementById('datePicker_2').value;
			var title=document.getElementById('Title').value;
			if((title==="") && (date_from==="") && (date_to===""))
			{
				//do nothing - no filter selected
			}
			else
			{
				
				//var postdata = 'date_from=' + encodeURIComponent(date_from) + "&date_to=" + encodeURIComponent(date_to) + "&title=" + encodeURIComponent(title); 
				var postdata={date_from : date_from, date_to: date_to, title : title};
				$.ajax({
			        type : 'GET',
			        url : 'searchcom',
			        dataType : 'json',
			        data : postdata,
			        success : function(result) {
			           showCommittesBySearch(result,1);
			        },
			        error : function() {
			            alert('error');
			        }
			    });
			}
			
		});
		
		$('#datePicker').datepicker({
			format : 'dd/mm/yyyy'
		});
		
		$('#datePicker_2').datepicker({
			format : 'dd/mm/yyyy'
		});
		
		function getDate()
		{
		    var today = new Date();
		    var dd = today.getDate();
		    var mm = today.getMonth()+1; //January is 0!
		    var yyyy = today.getFullYear();
		    if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm}
		    today = dd+"/"+mm+"/"+yyyy;

		    //document.getElementById("TodayDate").value = today;
		    //document.getElementById("TodayDate_2").value = today;
		}

		//call getDate() when loading the page
		getDate();
		
	});
	
	function loadPage(page)
	{
		var pg = page;
		var postdata = {page : page}; //'page=' + encodeURIComponent(pg);
		
		$.ajax({
	        type : 'GET',
	        url : 'loadcom',
	        dataType : 'json',
	        data : postdata,
	        success : function(result) {
	           showCommittes(result,page);
	        },
	        error : function() {
	            alert('error!');
	        }
	    });
	}
	
	function showCommittes(committes,page)
	{
		$("#tbodyid").empty();
		$('.firstModals').remove();
		var count = (page -1) * 5;
		
		for ( var i = 0, len = committes.length; i < len; ++i) 
		{
			var num = count + i + 1;
            var comm = committes[i];
            date = new Date(comm.committe.meetingDate);
            var month = (date.getMonth()+1);
            if(month < 10)
            	month = "0"+month;
            var new_date=date.getDate() + "/" + month + "/" + date.getFullYear();  
            
            if(comm.documents.length == 0)
           	{
            	 $('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'>-</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            else if(comm.documents.length == 1)
           	{
            	$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/download/" + comm.documents[0].id + "'/>'>" + comm.documents[0].title +"&nbsp<i class='glyphicon glyphicon-download-alt'></i></a></td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            else if(comm.documents.length > 1)
           	{
            	$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/downloadAll/" + comm.committe.id + "'/>'>" + comm.documents.length + " αρχεία &nbsp<i class='glyphicon glyphicon-download-alt'></i></a></td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            
            /*$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
            			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
            			"<td class='center-td set-height'>" + new_date + "</td>" +
            			"<td class='center-td set-height'>" + comm.committe.id + "</td>" +
            			"<td class='center-td set-height'><a href='<c:url value='/users/user/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
            			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
            			"</tr>"
            );*/
            
            var mod = "<div id='myModal_" + comm.committe.id + "' class='modal fade' role='dialog'>" +
				"<div class='modal-dialog modal-sm'>" +
					"<div class='modal-content'>" +
						"<div class='modal-header btn-warning' style='font-weight:bold;color:white;'>" +
							"<button type='button' class='close' data-dismiss='modal'></button>" +
							"<h5 class='modal-title modal-sm'>Προσοχή!</h5>" +
						"</div>" +
						"<div class='modal-body'>" +
							"<p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <strong>ΟΡΙΣΤΙΚΗ</strong> διαγραφή της επιτροπής?<p><br> Πατήστε Ακύρωση για να ακυρώσετε την ενέργεια." +
						"</div>" +
						"<div class='modal-footer'>" +
							"<button type='button' class='btn btn-danger DeleteBtnID' value='" + comm.committe.id + "' data-dismiss='modal'>Διαγραφή</button>" +
							"<button type='button' class='btn btn-default' data-dismiss='modal'>Ακύρωση</button>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>";
			
            $('#tbodyid').append(mod);
    	}
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/committe/delete/' + $(this).val();
			window.location = url;
		});
	}
	
	function showCommittesBySearch(committes,page)
	{
		$("#tbodyid").empty();
		$('.firstModals').remove();
		var count = (page -1) * 5;
		
		if(committes.length > 0)
		{
			document.getElementById('total_records').innerHTML = "Σύνολο : " + committes[0].committe.total;
			var element = document.getElementById('pagination');
			if (typeof(element) != 'undefined' && element != null)
			{
				document.getElementById('pagination').style.display="none";
			}
		}
		
		for ( var i = 0, len = committes.length; i < len; ++i) 
		{
			var num = count + i + 1;
            var comm = committes[i];
            date = new Date(comm.committe.meetingDate);
            var month = (date.getMonth()+1);
            if(month < 10)
            	month = "0"+month;
            var new_date=date.getDate() + "/" + month + "/" + date.getFullYear();  
            
            if(comm.documents.length == 0)
           	{
            	 $('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'>-</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            else if(comm.documents.length == 1)
           	{
            	$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/download/" + comm.documents[0].id + "'/>'>" + comm.documents[0].title +"&nbsp<i class='glyphicon glyphicon-download-alt'></i></a></td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            else if(comm.documents.length > 1)
           	{
            	$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
             			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
             			"<td class='center-td set-height'>" + new_date + "</td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/downloadAll/" + comm.committe.id + "'/>'>" + comm.documents.length + " αρχεία &nbsp<i class='glyphicon glyphicon-download-alt'></i></a></td>" +
             			"<td class='center-td set-height'><a href='<c:url value='/committe/modify/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
             			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
             			"</tr>"
             	 );
           	}
            
            /*$('#tbodyid').append("<tr><td class='set-height'>" + num +"</td>" +
            			"<td class='center-td set-height'>" + comm.committe.title + "</td>" +
            			"<td class='center-td set-height'>" + new_date + "</td>" +
            			"<td class='center-td set-height'>" + comm.committe.id + "</td>" +
            			"<td class='center-td set-height'><a href='<c:url value='/users/user/" + comm.committe.id + "'/>'><i class='glyphicon glyphicon-pencil'></i></a>" + 
            			"&nbsp <a onclick='showModal(" + comm.committe.id + ")' ><i class='glyphicon glyphicon-trash' style='color: red;'></i></a></td>" + 
            			"</tr>"
            );*/
            
            var mod = "<div id='myModal_" + comm.committe.id + "' class='modal fade' role='dialog'>" +
				"<div class='modal-dialog modal-sm'>" +
					"<div class='modal-content'>" +
						"<div class='modal-header btn-warning' style='font-weight:bold;color:white;'>" +
							"<button type='button' class='close' data-dismiss='modal'></button>" +
							"<h5 class='modal-title modal-sm'>Προσοχή!</h5>" +
						"</div>" +
						"<div class='modal-body'>" +
							"<p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <strong>ΟΡΙΣΤΙΚΗ</strong> διαγραφή της επιτροπής?<p><br> Πατήστε Ακύρωση για να ακυρώσετε την ενέργεια." +
						"</div>" +
						"<div class='modal-footer'>" +
							"<button type='button' class='btn btn-danger DeleteBtnID' value='" + comm.committe.id + "' data-dismiss='modal'>Διαγραφή</button>" +
							"<button type='button' class='btn btn-default' data-dismiss='modal'>Ακύρωση</button>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>";
			
            $('#tbodyid').append(mod);
    	}
		
		$('.DeleteBtnID').on('click', function () {
			var url = '/committee/committe/delete/' + $(this).val();
			window.location = url;
		});
	}
	
	function showModal(id)
	{
		var modal = '#myModal_' + id;
		$(modal).modal(); 
	}
	
</script>
<script
	src="<c:url value="/resources/bootstrap/js/jquery-3.1.1.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap-datepicker.js" />"></script>
</html>