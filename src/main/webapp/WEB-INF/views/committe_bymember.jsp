<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet"
	href="<c:url value="/resources/bootstrap/css/datepicker.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />">
<div class="container" style="padding-left: 20%">
	<br>
	<div class="well">
		<c:if test="${committe.isCommitteAdmin == true}" >
				 <h4>Τροποποίηση επιτροπής</h4>
		</c:if>
		<c:if test="${committe.isCommitteAdmin == false}" >
				 <h4>Στοιχεία επιτροπής</h4> 
		</c:if>
	</div>
	<c:if test="${committe.isCommitteAdmin == true}" >
		<div class="alert alert-info">
		  <strong>Tip!</strong> Προσθέστε μία νέα επιτροπή, εισάγοντας τα απαραίτητα στοιχεία (*) καθώς και επιπλέον πληροφορίες.
		</div>
	</c:if>
	<br>
	<div class="row-fluid" id="dvContainer">
		<form:form method="POST" action="/committee/committe/update_bymember" id="createComForm" name="myform" 
			modelAttribute="committe" enctype="multipart/form-data">
			<form:input path="id" type="text" hidden="hidden"/>
			<div class="col-sm-12">
				<div class="form-group">
					<label>Τίτλος*</label>
					<c:if test="${committe.isCommitteAdmin == true}" >
						<form:input path="Title" type="text" placeholder="Τίτλος.."
						name="title" id="title" class="form-control" />
					</c:if>
					<c:if test="${committe.isCommitteAdmin == false}" >
						<form:input path="Title" type="text" placeholder="Τίτλος.."
						name="title" id="title" class="form-control" disabled="true"/>
					</c:if>
				</div>
				<div class="form-group">
					<label>Γενικές Πληροφορίες</label>
					<c:if test="${committe.isCommitteAdmin == true}" >
						<form:textarea rows="4" path="GeneralInfo" type="text" name="info"
						id="info" placeholder="Πληροφορίες.." class="form-control" />
					</c:if>
					<c:if test="${committe.isCommitteAdmin == false}" >
						<form:textarea rows="4" path="GeneralInfo" type="text" name="info"
						id="info" placeholder="Πληροφορίες.." class="form-control" disabled="true"/>
					</c:if>
				</div>
				<div class="form-group">
					<label>Ημερομηνία Συνάντησης*</label>
					<div class="input-group input-append date">
						<c:if test="${committe.isCommitteAdmin == true}" >
							<form:input path="MeetingDate" id="datePicker" type="text"
							class="form-control meetingDate" name="MeetingDate"/>
						</c:if>
						<c:if test="${committe.isCommitteAdmin == false}" >
							<form:input path="MeetingDate" id="datePicker" type="text"
							class="form-control meetingDate" name="MeetingDate" disabled="true"/>
						</c:if>
					</div>
					<input type="hidden" name="startdate" id="TodayDate"/>
				</div>
				<br>
				<div class="form-group">
					<c:if test="${committe.id > 0}" >
						<label>Μέλη Επιτροπής*</label><br>
						 <c:choose>
						  <c:when test="${members.size() >= 1}">
						  	<c:forEach items="${members}" var="member">
							   <div class="well" style="margin-bottom:10px; padding-top:8px; padding-bottom:8px;">
							   		<label><c:out value="${member.lastName} ${member.firstName} - " /><i><c:out value="${member.role}" /></i></label> <!--  ${member.isCommittePresident}  -->
									<c:if test="${committe.isCommitteAdmin == true}" >
										 <c:if test="${member.isCommittePresident == false}" >
											<a class="pull-right" onclick='showMemberModal(<c:out value="${member.id}" />)'>
												<i class="glyphicon glyphicon-trash" style="color: red;"></i>
											</a>
										</c:if>
									</c:if>
							   </div>
							   <div id="Member_myModal_<c:out value="${member.id}" />" class="modal fade firstModals" role="dialog">
								    <div class="modal-dialog modal-sm">
								        <div class="modal-content">
								            <div class="modal-header btn-warning" style="font-weight:bold;color:white;">
								                <button type="button" class="close" data-dismiss="modal">&times;</button>
								                <h5 class="modal-title modal-sm">Προσοχή!</h5>
								            </div>
								            <div class="modal-body">
								                <p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <b><strong>ΟΡΙΣΤΙΚΗ</strong></b> διαγραφή αυτού του μέλους από την επιτροπή? <br> Πατήστε 'Ακύρωση' για να ακυρώσετε την ενέργεια.</p>
								            </div>
								            <div class="modal-footer">
								                <button type="button" class="btn btn-danger DeleteMemberBtnID" value="<c:url value="${member.id}&${committe.id}"/>"  data-dismiss="modal">Διαγραφή</button>
								                <button type="button" class="btn btn-default" data-dismiss="modal">Ακύρωση</button>
								            </div>
								        </div>
								    </div>
								</div>
							</c:forEach>
						  </c:when>
						  <c:otherwise>
						  		<b style="color:red;"><i>Δεν υπάρχουν μέλη σε αυτή την επιτροπή.</i></b>
						  </c:otherwise>
						</c:choose> 
						<br>
					</c:if>
					<c:if test="${committe.id > 0}" >
						<c:if test="${committe.isCommitteAdmin == true}" >
							<div class="alert alert-warning">
							<strong>Tip!</strong> Προσθέστε νέα μέλη στην επιτροπή επιλέγοντας το username του χρήστη και στην συνέχεια επιλέξτε τον ρόλο του.<br>
												  Τα νέα μέλη θα ειδοποιηθούν με email για την προσθήκη τους σε αυτή την επιτροπή.
								<span class="btn btn-primary pull-right"  onclick='showMEMModal(<c:out value="${committe.id}" />)'>Προσθήκη</span>
							</div>
						</c:if>
					</c:if>
					<div id="MEMmyModal_<c:out value="${committe.id}" />" class="modal fade firstModals" role="dialog">
					    <div class="modal-dialog">
					        <div class="modal-content">
					            <div class="modal-header btn-info" style="font-weight:bold;color:white;">
					                <button type="button" class="close" data-dismiss="modal">&times;</button>
					                <h5 class="modal-title">Προσθήκη μέλους</h5>
					            </div>
					            <div class="modal-body">
					            	<form:select path = "Member" class="form-control" id="userList">
				                      <form:option value = "NONE" label = "Επιλέξτε χρήστη.."/>
				                      <form:options items = "${emails}" />
				                  	</form:select>
					                <!--<form:input path="Member" type="text" id="member"
										placeholder="Username or Email.." class="form-control"/> -->
									<br>
									<c:if test="${committe.hasPresident == false}" >
										Είναι Πρόεδρος της Επιτροπής? <form:checkbox path="IsCommitteAdmin" id="isAdmin"/>
									</c:if> 
									<div class="modal-footer">
					                	<button type="button" class="btn btn-success AddBtnID" value="<c:url value="${committe.id}"/>"  data-dismiss="modal">Προσθήκη</button>
					                	<button type="button" class="btn btn-danger" data-dismiss="modal">Ακύρωση</button>
					            	</div>
					            </div>
					            
					        </div>
					    </div>
					</div>
				</div>
				<br>
				<c:if test="${committe.id > 0}" >
					<label>Συνδεδεμένα αρχεία</label><br>
					 <c:choose>
					  <c:when test="${docs.size() >= 1}">
					  	<c:forEach items="${docs}" var="doc">
						   <div class="well" style="margin-bottom:10px; padding-top:8px; padding-bottom:8px;">
						   		<a href="<c:url value="/committe/download/${doc.id}" />"><c:out value="${doc.title}" /></a>
						   		<%-- <a class="pull-right"
						   			onclick='showModal(<c:out value="${user.id}" />)'
								href="<c:url value="/committe/deleteDoc/${doc.id}&${committe.id}"/>"><i
									class="glyphicon glyphicon-trash" style="color: red;"></i></a> --%>
								<c:if test="${committe.isCommitteAdmin == true}" >
									<a class="pull-right" onclick='showModal(<c:out value="${doc.id}" />)'>
										<i class="glyphicon glyphicon-trash" style="color: red;"></i>
									</a>
								</c:if>
						   </div>
						   <div id="DOCmyModal_<c:out value="${doc.id}" />" class="modal fade firstModals" role="dialog">
							    <div class="modal-dialog modal-sm">
							        <div class="modal-content">
							            <div class="modal-header btn-warning" style="font-weight:bold;color:white;">
							                <button type="button" class="close" data-dismiss="modal">&times;</button>
							                <h5 class="modal-title modal-sm">Προσοχή!</h5>
							            </div>
							            <div class="modal-body">
							                <p>Είστε σίγουροι ότι θέλετε να προχωρήσετε σε <b><strong>ΟΡΙΣΤΙΚΗ</strong></b> διαγραφή αυτού του αρχείου (${doc.title})? <br> Πατήστε 'Ακύρωση' για να ακυρώσετε την ενέργεια.</p>
							            </div>
							            <div class="modal-footer">
							                <button type="button" class="btn btn-danger DeleteBtnID" value="<c:url value="${doc.id}&${committe.id}"/>"  data-dismiss="modal">Διαγραφή</button>
							                <button type="button" class="btn btn-default" data-dismiss="modal">Ακύρωση</button>
							            </div>
							        </div>
							    </div>
							</div>
						</c:forEach>
					  </c:when>
					  <c:otherwise>
					  		<b style="color:red;"><i>Δεν υπάρχουν αρχεία για αυτή την επιτροπή.</i></b>
					  </c:otherwise>
					</c:choose> 
					<br>
				</c:if>
				<c:if test="${committe.isCommitteAdmin == true}" >
					<div class="form-group">
						  <span class="input-group-btn">
						    <span class="btn btn-primary pull-right" onclick="$(this).parent().find('input[type=file]').click();">Επιλογή Αρχείων</span>
						    <input id="file_input" name="files" multiple style="display: none;" type="file">
						 	 <textarea class="form-control" id="filelist" readonly rows="3" style="resize:none;"></textarea>
						  </span>
					</div> 
					<!--  <div>
			            <input class="btn btn-lg btn-primary btn-block" type="button" value="Export to pdf" id="btnPrint" />
			        </div>-->
			        <br>
					<input type="submit" class="btn btn-lg btn-success" value="Αποθήκευση" />
					<input type="button" class="btn btn-lg btn-danger" onclick="location.href='/committee/committe/all_bymember'" value="Ακύρωση"/>
				</c:if>
				<c:if test="${committe.isCommitteAdmin == false}" >
					<!-- <div>
			            <input class="btn btn-lg btn-primary btn-block" type="button" value="Export to pdf" id="btnPrint" />
			        </div>
			        <br>-->
					<input type="button" class="btn btn-lg btn-success" onclick="location.href='/committee/committe/all_bymember'" value="Επιστροφή"/>
				</c:if>
				<br><br><br>
			</div>
		</form:form>
	</div>
</div>
<script
	src="<c:url value="/resources/bootstrap/js/jquery-3.1.1.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/jquery.validate.js" />"></script>
<script
	src="<c:url value="/resources/bootstrap/js/bootstrap-datepicker.js" />"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						
						$('.menu-ul li.active').removeClass('active');
						$('#committes_members').addClass('active');
						//$('#isAdmin').attr('checked', false);
						
						$('.DeleteBtnID').on('click', function () {
							var url = '/committee/committe/deleteDoc_bymember/' + $(this).val();
							window.location = url;
						});
						
						$('.DeleteMemberBtnID').on('click', function () {
							var url = '/committee/committe/deleteMember_bymember/' + $(this).val();
							window.location = url;
						});
						
						$('.AddBtnID').on('click', function () {
							//var member = document.getElementById('member').value;
							var element = document.getElementById('isAdmin');
							if (typeof(element) != 'undefined' && element != null)
							{
								var isAdmin = document.getElementById('isAdmin').checked;
							}
							else
							{
								var isAdmin = false;
							}
							
							var e = document.getElementById("userList");
							var member = e.options[e.selectedIndex].value;
							var url = '/committee/committe/addMember_bymember/' + $(this).val() + '&' + member + '&' + isAdmin; 
							window.location = url;
						});
						
						/*$("body").on("click", "#btnPrint", function () {
							 var divContents = $("#dvContainer").html();
					            var printWindow = window.open('', '', 'height=400,width=800');
					            printWindow.document.write('<html><head><title>DIV Contents</title>');
					            printWindow.document.write('</head><body >');
					            printWindow.document.write(divContents);
					            printWindow.document.write('</body></html>');
					            printWindow.document.close();
					            printWindow.print();
					    });*/
						
						$('input#file_input').change(function () {
					    	  var list = document.getElementById('filelist');
					    	  list.innerHTML = '';
					    	  var flag = false;
					    	  for(var i = 0 ; i < this.files.length; i++)
				    		  {
					    		  if(this.files.length == 1)
					    	 	  {
					    			  list.innerHTML += this.files[i].name;
					    		  }
					    		  else
					    		  {
					    			  if(flag == false)
				    				  {
					    				  list.innerHTML += "(" + this.files.length + " αρχεία) : " ;
					    				  flag = true;
				    				  } 
					    		  }
					    		  if((i == this.files.length - 1) && this.files.length != 1)
				    			  {
					    			  list.innerHTML += this.files[i].name;
				    			  }
					    		  else if(this.files.length != 1)
			    				  {
					    			  list.innerHTML += this.files[i].name + " , " ;
			    				  }
				    		  }
						});
						
						$('#datePicker').datepicker({
							format : 'dd/mm/yyyy'
						});
						
						$("form[name='myform']").submit(function () {
					        if ($(this).valid()) {
					            $(this).find('input[type=submit]').attr('disabled', 'disabled');
					        }
					    });
						
						jQuery.validator.addMethod("greaterThan", 
							function(value, element, params) {
								
								var target = $(params).val();
						        var isValueNumeric = !isNaN(parseFloat(value)) && isFinite(value);
						        var isTargetNumeric = !isNaN(parseFloat(target)) && isFinite(target);
						        if (isValueNumeric && isTargetNumeric) {
						            return Number(value) > Number(target);
						        }
								
						        var from = value.split("/");
						        var to = target.split("/");
						        var dt1 = new Date(from[2], from[1] - 1, from[0]);
						        var dt2 = new Date(to[2], to[1] - 1, to[0]);
						        
						        if (dt1.getFullYear() >= dt2.getFullYear()) 
						        {
						        	if(dt1.getMonth() > dt2.getMonth())
					        		{
						        		return true;
					        		}
						        	if(dt1.getMonth() == dt2.getMonth())
				        			{
					        			if(dt1.getDate() > dt2.getDate())
				        				{
				        					return true;
				        				}
				        			}
						        }
	
						        return false;

							},'Πρέπει η ημερομηνία να είναι μεγαλύτερη από τη σημερινή!');
						
						function getDate()
						{
						    var today = new Date();
						    var dd = today.getDate();
						    var mm = today.getMonth()+1; //January is 0!
						    var yyyy = today.getFullYear();
						    if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm}
						    today = dd+"/"+mm+"/"+yyyy;

						    document.getElementById("TodayDate").value = today;
						}

						//call getDate() when loading the page
						getDate();
						
						$("form[name='myform']")
								.validate(
										{
											// Specify validation rules
											rules : {
												// The key name on the left side is the name attribute
												// of an input field. Validation rules are defined
												// on the right side
												Title : {
													required : true
												},
												/* email : {
													required : true,
													// Specify that email should be validated
													// by the built-in "email" rule
													email : true
												}, */
												MeetingDate : {
													required : true,
													greaterThan: "#TodayDate"
												}
											},
											// Specify validation error messages
											messages : {
												Title : {
													required : "Παρακαλώ δώστε έναν τίτλο!"
												},
												MeetingDate : {
													required : "Παρακαλώ ορίστε μία ημ/νια συνάντησης!"
												}/* ,
												email : "Please enter a valid email address" */
											},
											// Make sure the form is submitted to the destination defined
											// in the "action" attribute of the form when valid
											submitHandler : function(form) {
												form.submit();
											}
										});

					});
	
	function showModal(id)
	{
		var modal = '#DOCmyModal_' + id;
		$(modal).modal(); 
	}
	
	function showMEMModal(id)
	{
		var modal = '#MEMmyModal_' + id;
		$(modal).modal(); 
	}
	
	function showMemberModal(id)
	{
		var modal = '#Member_myModal_' + id;
		$(modal).modal(); 
	}
	
</script>
