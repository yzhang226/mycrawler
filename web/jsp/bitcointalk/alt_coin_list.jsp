<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->

<%@ include file="/jsp/styleTCG.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<meta charset="utf-8" />
	<title>Mobius | TCG</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	
	<script type="text/javascript">
	
	var editable = <%=request.getAttribute("editable") %>;
	
	jQuery(document).ready(function() {
		
		// single multiple
		// alert("editable is " + editable);
		var mode = editable ? "multiple" : "single";
		// alert("mode is " + mode);
		
		$('#__grid__').tcgtable({rowSelect : mode});
		
		$("#date_picker input").datepicker({
		   beforeShow: function(input) {
				AddClearButton(input);
		   },
		   onSelect: function(dateText, inst) { 
			   	
		   },
		   onChangeMonthYear:function( year, month, inst ) {
			   AddClearButton(inst.input);
		   },
		   dateFormat: "yy-mm-dd",
		   showButtonPanel: true,
		   buttonText: "Date",
		   closeText : "Close"
		});
		
		//  : "icon-check-empty]'
		// var ms = $("td[minable='1']");
		
		 $("td[minable='1']").unbind('click').click(function() {
			// alert("test" + $(this).children()[0]);
			var min = $($(this).children()[0]);
			checkMinable(min.attr('id'), min.attr('value'));
		});
		
	});
	
	function searchByField() {
		var searchValue = $('#searchValue').val();
		
		if (isNullOrUndefined(searchValue) || searchValue.trim() == '') {
			// alert('Please input search value!');
		}
		var url = combineFullUrlDefault(null);
		
		window.location.href = url;
	}
	
	function updateSelecedtInfo() {
    	var ids = $('#clickedObjId').val();
		
		if (isNullOrUndefined(ids)) {
			alert("Please select Alt Coin to process!");
			return;
		}
		
		var idsArr = ids.split(',');
		alert(ids + "    -----   " + idsArr);
		var sIds = new Array();
		var sValues = new Array();
		
		for (var i=0; i < idsArr.length ; i++) {
			var id = idsArr[i];
			if (!isNullOrUndefined(id)) {
				sIds.push(id);
				sValues.push($('#'+id+"_algo").val());
				sValues.push($('#'+id+"_countDown").val());
				sValues.push($('#'+id+"_name").val());
				sValues.push($('#'+id+"_abbrName").val());
				sValues.push($('#'+id+"_interestLevel").val());
				
				sValues.push($('#'+id+"_proof").val());
				sValues.push($('#'+id+"_cpuMinable").attr('value'));
				sValues.push($('#'+id+"_gpuMinable").attr('value'));
				sValues.push($('#'+id+"_asicMinable").attr('value'));
			}
		}
		
		var url = getFullUrl('/jsp/bitcointalk/updatealtcoins.do?altIds=' + sIds.join() + '&altValues=' + sValues.join());
		
		alert("updateSelecedtInfo url is " + url)
		
		$.ajax({
				url : url,
				success : function(data) {
					var resp = $.parseJSON(data);
					alert(resp.message);
					
					if (resp.success == 'true') {
						$('#clickedObjId').val('');
						
						var url = combineFullUrlDefault(null);
						window.location.href=url;
					}
					
				}
		});
		
	}
	
	function changeEditable(curr) {
		// alert("changeEditable");
		if (curr == 1) $('#editable').val('false');
		else if (curr == 0) $('#editable').val('true');
		
		var url = combineFullUrlDefault(null);
		window.location.href=url;
		
		return false;
	}
	
	function checkMinable(id, curr) {
		var box = $('#' + id);
		if (curr == 1) {
			box.attr('class', 'icon-check-empty');
			box.attr('value', 0);
		} else if (curr == 0) {
			box.attr('class', 'icon-check');
			box.attr('value', 1);
		}
	}
	
	function reInitAnnCoins() {
		var url = getFullUrl("/jsp/bitcointalk/initannboard.do");
		
		var baseSeedUrl = "https://bitcointalk.org/index.php?board=159.";
		// var baseSeedUrl = "https://bitcointalk.org/index.php?board=67.";
		var startgroup = 0;
		var endgroup = 1;
		
		url = url + "?baseSeedUrl=" + baseSeedUrl + "&startgroup=" + startgroup + "&endgroup=" + endgroup;
		
		alert('url is ' + url);
		$.ajax({
				url : url,
				success : function(data) {
					var resp = $.parseJSON(data);
					alert(resp.message);
					
					if (resp.success == 'true') {
						
						var url = combineFullUrlDefault(null);
						window.location.href=url;
					}
				}
		});
	}

	</script>

	
</head>

<body class="page-header-fixed">
		<input type="hidden" id="editable" name="editable" value=${editable }>
		<input type="hidden" id="_required_params_" value="searchField,searchValue,editable">
		
		
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE HEADER-->
				<div class="row-fluid">
					<div class="span12">
						<h3 class="page-title">
							<img src="../assets/img/logo_admin.png">
						</h3>
							<ul class="breadcrumb">
							<li>
								<i class="icon-home"></i>
								<a href="<%=request.getContextPath() %>/jsp/route/show.do">Home</a> 
								<span class="icon-angle-right"></span>
						    </li>
							<li>
								<a href="<%=request.getContextPath() %>/jsp/resutil/tosiputil.do">SIP Utilization</a>
							</li>
					  </ul>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12">
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption"><i class="icon-phone"></i> View SIP Statistics </div>
								<div class="tools">
									<a href="#" onclick="changeEditable(${ editable ? 1 : 0 }); "> 
										<c:if test="${editable }">ReadOnly</c:if>
										<c:if test="${!editable }">Editable</c:if>
									 </a>
								</div>
							</div>
							<div class="portlet-body">
							<div class="clearfix">
								
					           <div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls">
					                  	<button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="reInitAnnCoins();">ReInit AnnCoins</button>
				                  	</div>
					           </div>
					           
					           <div class="control-group">
									<div class="controls" >
						                <select id="searchField" name="searchField" class="span5 m-wrap" data-placeholder="Choose a Category"  style="width: 190px" tabindex="1">
											<option value="title" ${searchField == 'title' ? "selected" : "" }>Title</option>
											<option value="publishContent" ${searchField == 'publishContent' ? "selected" : "" }>Content</option>
									  	</select>
									  	<span style="font-size: 16px;"> : </span>
									  	<input type="text" id="searchValue" name="searchValue" value="${searchValue }" class="span5 m-wrap medium" style="width: 100px" title="CAN use asterisk(*) match any chars "  />
									  	&nbsp; &nbsp;
									  	<button type="button" onclick="searchByField();" style="width: 70px; vertical-align: top;" class="btn blue m-wrap">Search</button>
				                  	</div>
				           		</div>							
								
								
							</div>

							<div style="overflow:auto; scrollbar-base-color:#ff6600;">
								<table class="table table-bordered table-hover" id="__grid__">
									<thead>
										<tr id="_table_head_">
											<th>Publish Date</th>
											
											<th >Algo</th>
											<th >Count Down</th>
											<th >Name</th>
											<th >Abbr</th>
											<th >Interest</th>
											<th >Proof</th>
											<th >CPU Minable</th>
											<th >GPU Minable</th>
											<th >ASIC Minable</th>
											
											<th>Title</th>
											<th>Author</th>
											<%-- 
											<th>Replies</th>
											<th>Views</th>
											 --%>
										</tr>
										<tr id="_head_fields_" style="display: none;">
											<th >publishDate</th>
											
											<th >algo</th>
											<th >countDown</th>
											<th >name</th>
											<th >abbrName</th>
											<th >interestLevel</th>
											<th >proof</th>
											<th >cpuMinable</th>
											<th >gpuMinable</th>
											<th >asicMinable</th>
											
											<th >title</th>
											<th >author</th>
											<%-- 
											<th >replies</th>
											<th >views</th>
											 --%>
										</tr>
									</thead>
									
									<tbody>
										<c:forEach var="ann" items="${anns }">
											<tr class="odd gradeX" objid="${ann.id }">
	                                         	<td>${ann.publishDate }</td>
	                                         	
	                                         	<c:if test="${editable }">
	                                         		<td>
		                                         		<select id="${ann.id }_algo" style="width: 90px" class="span5 m-wrap" data-placeholder="Choose a Category" tabindex="1">
		                                         			<option value="scrypt" ${ann.algo == 'scrypt' ? "selected" : "" }>scrypt</option>
		                                         			<option value="sha256" ${ann.algo == 'sha256' ? "selected" : "" }>sha-256</option>
		                                         			<option value="sha3" ${ann.algo == 'sha3' ? "selected" : "" }>sha-3</option>
		                                         			<option value="blake256" ${ann.algo == 'sha3' ? "selected" : "" }>blake-256</option>
		                                         		</select> 
	                                         		</td>
													<td>
														<div style="width: 100px;">
															<div id="date_picker" style="vertical-align: middle; margin-bottom: 0px; width: 90px;">
																<input type="text" id="${ann.id }_countDown"  value="${ann.countDown }" style="width: 90px" /> 
															</div>
														</div>
													</td>
													<td> 
														<input type="text" id="${ann.id }_name" value="${ann.name }" maxlength="15" style="width: 65px" > 
													</td>
													<td> <input type="text" id="${ann.id }_abbrName" value="${ann.abbrName }" maxlength="5" style="width: 25px"> </td>
													<td> 
														<select id="${ann.id }_interestLevel" style="width: 50px" class="span5 m-wrap" data-placeholder="Choose a Category" tabindex="1">
														<c:forEach begin="1" end="10" var="i">
															<option value="${i }" ${i == ann.interestLevel ? "selected" : "" }>${i }</option>
														</c:forEach>
		                                         		</select> 
													</td>
													
													<td> 
														<select id="${ann.id }_proof" style="width: 90px" class="span5 m-wrap" data-placeholder="Choose a Category" tabindex="1">
															<option value="PoW" ${"PoW" == ann.proof ? "selected" : "" }>PoW</option>
															<option value="PoS" ${"PoS" == ann.proof ? "selected" : "" }>PoS</option>
															<option value="PoWPoS" ${"PoWPoS" == ann.proof ? "selected" : "" }>PoW/PoS</option>
		                                         		</select> 
													</td>
													
													<td onclick="checkMinable(${ann.cpuMinable ? 1 : 0 });" minable='1'>
														<i id="${ann.id }_cpuMinable" class='${ann.cpuMinable ? "icon-check" : "icon-check-empty" }' value='${ann.cpuMinable ? 1 : 0 }'></i>
													</td>
													<td onclick="checkMinable(${ann.gpuMinable ? 1 : 0 });" minable='1'>
														<i id="${ann.id }_gpuMinable" class='${ann.gpuMinable ? "icon-check" : "icon-check-empty" }' value='${ann.gpuMinable ? 1 : 0 }'></i>
													</td>
													<td onclick="checkMinable(${ann.asicMinable ? 1 : 0 });" minable='1'>
														<i id="${ann.id }_asicMinable" class='${ann.asicMinable ? "icon-check" : "icon-check-empty" }' value='${ann.asicMinable ? 1 : 0 }'></i>
													</td>
													
	                                         	</c:if>
	                                         	<c:if test="${!editable }">
		                                         	<td>${ann.algo }</td>
													<td>${ann.countDown }</td>
													<td>${ann.name }</td>
													<td>${ann.abbrName }</td>
													<td>${ann.interestLevel }</td>
													<td>${ann.proof }</td>
													
													<td><i class='${ann.cpuMinable ? "icon-check" : "icon-check-empty" }' ></i></td>
													<td><i class='${ann.gpuMinable ? "icon-check" : "icon-check-empty" }' ></i></td>
													<td><i class='${ann.asicMinable ? "icon-check" : "icon-check-empty" }' ></i></td>
	                                         	</c:if>
	                                         	
	                                         	<td> <a href="${ann.link }" target="_blank"> ${ann.title } </a> </td>
	                                         	<td>${ann.author }</td>
	                                         	
	                                         	<%-- 
												<td>${ann.replies }</td>
												<td>${ann.views }</td>
												 --%>
											</tr>
										</c:forEach>
										<c:if test="${empty anns }">
											<tr>
												<td colspan="12" class="no-data">No Data</td>
											</tr>
										</c:if>
									</tbody>
								</table>
								</div>
								
                                <div class="pagination pagination-centered">
									<ul>
										<c:if var="pageNotEmpty" test="${totalPages > 0 }">
											<li><a id="1" title="First Page"><i class="icon-step-backward"></i></a></li>
											<li><a id="${pageNo == 1 ? 1 : pageNo - 1 }" title="Previous Page"><i class="icon-caret-left"></i></a></li>
											
											<c:forEach var="idx" begin="${beginPageNo }" end="${endPageNo }">
												<li class=${pageNo == idx ? 'active' : ''}><a id="${idx }">${idx }</a></li>
											</c:forEach>
											 
											<li><a id="${pageNo < totalPages ? pageNo+1 : pageNo }" title="Next Page"><i class="icon-caret-right"></i></a></li>
											<li><a id="${totalPages }" title="Last Page"><i class="icon-step-forward"></i></a></li>
										</c:if>
										<c:if test="${!pageNotEmpty }">
											<li><a id="0"  title="First Page"><i class="icon-step-backward"></i></a></li>
											<li><a id="0" title="Last Page"><i class="icon-step-forward"></i></a></li>
										</c:if>
										
										<li><a>Total ${totalCount } Records</a></li>
									</ul>
								</div>
								
								<div class="form-actions">
									<button type="button" onclick="updateSelecedtInfo();" class="btn blue">Update Info</button>
								</div>
                                
							</div>
						</div>
						<!-- END EXAMPLE TABLE PORTLET-->
					</div>
				</div>
				
				<!-- END PAGE CONTENT-->
			</div>
			<!-- END PAGE CONTAINER-->
		</div>
		<!-- END PAGE -->
	
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">
			2013 &copy; COOK|Crawler.
		</div>
		<div class="footer-tools">
			<span class="go-top">
			<i class="icon-angle-up"></i>
			</span>
		</div>
	</div>

	
</html>