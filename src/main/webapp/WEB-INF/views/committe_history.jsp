<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8" charset="utf8" >
	<style>
		.history-menu li, .history-menu li a {
		    white-space: normal;
		    float: left;
		    width: 100%;
		    height: auto;
		    word-wrap: break-word;
		}
	</style>
</head>
<div class="container" style="padding-left: 10%">
	<br>
	<div class="well">
		<h4>Αναζήτηση Ιστορικού Επιτροπών</h4>
	</div>
	<div class="alert alert-info">
	  <strong>Tip!</strong> Δείτε τις ενέργειες κάθε επιτροπής κάνοντας αναζήτηση με βάση τον τίτλο της επιτροπής, ημερομηνία ή το ονομ/μο-email του χρήστη.
	</div>
	<br>
	<div class="well">
		<form class="form-inline">
			<label>Ημ/νία : </label>
			<div class="input-group input-append date">
				<input id="datePicker" type="text"
					class="form-control" name="ActionDate"/>
			</div>
			<input type="hidden" name="startdate" id="TodayDate"/>
			<label>Τίτλος :</label>
			<input type="text" name="Title" id="Title" class="form-control"/>
			<label>Ονομ/μο χρήστη ή email :</label>
			<input type="text" name="Username" id="Username" class="form-control"/>
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
	<div class="row-fluid">
		<ul class="list-group history-menu" id="listHistory">
		  
		</ul>
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
						$('#history').addClass('active');
						
						$('#Clear').on('click', function () {
							var url = '/committee/committe/history' + $(this).val();
							window.location = url;
						});
						
						$('#Search').on('click', function () {
							
							var date_=document.getElementById('datePicker').value;
							var title=document.getElementById('Title').value;
							var username=document.getElementById('Username').value;
							if((title==="") && (date_==="") && (username===""))
							{
								//do nothing - no filter selected
							}
							else
							{
								
								//var postdata = 'date_from=' + encodeURIComponent(date_from) + "&date_to=" + encodeURIComponent(date_to) + "&title=" + encodeURIComponent(title); 
								var postdata={date_ : date_, title : title, username : username};
								$.ajax({
							        type : 'GET',
							        url : 'search_history',
							        dataType : 'json',
							        data : postdata,
							        success : function(result) {
							           showHistory(result);
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
						
						function getDate()
						{
						    var today = new Date();
						    var dd = today.getDate();
						    var mm = today.getMonth()+1; //January is 0!
						    var yyyy = today.getFullYear();
						    if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm}
						    today = dd+"/"+mm+"/"+yyyy;
						}

						//call getDate() when loading the page
						getDate();
					});
	
	function showHistory(historyList)
	{
		$("#listHistory").empty();
		
		if(historyList.length > 0)
		{
			for ( var i = 0, len = historyList.length; i < len; ++i) 
			{
	            var history = historyList[i];
	            date = new Date(history.creationDate);
	            var month = (date.getMonth()+1);
	            if(month < 10)
	            	month = "0"+month;
	            var new_date=date.getDate() + "/" + month + "/" + date.getFullYear();  
	            
	            $('#listHistory').append("<li class='list-group-item'>" + history.action + "<span class='badge'>" + new_date + "</span></li>");
			}
		}
		else
		{
			 $('#listHistory').append("<li class='list-group-item'>Δε βρέθηκαν αποτελέσματα..</li>");
		}
		
	}
	
</script>
