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
		// var mode = editable ? "multiple" : "single";
		var mode = "multiple";
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
		
		$('#searchValue').keypress(function(e) {
		    if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)){ 
		    	searchByField(); 
		    	return false; 
		    } else {
		    	return true;
		    }
		});
		
		$('#searchValue').focus();
	});
	
	function searchByField() {
		var searchValue = $('#searchValue').val();
		
		if (isNullOrUndefined(searchValue) || searchValue.trim() == '') {
			// alert('Please input search value!');
		}
		var url = combineFullUrlDefault(null);
		
		window.location.href = url;
	}
	
	var fields = new Array("interest", "name", "abbrName", "totalAmount", "algo", "preMined", "minedPercentage", 
							"proof", "blockReward", "blockTime", "halfBlocks", "halfDays", "powDays", "powHeight", "powAmount", 
							"difficultyAdjust", "launchTime");
	var formatedFields = new Array("totalAmount","blockReward","halfBlocks","preMined","powHeight","powAmount");
	var formatedDayFields = new Array("halfDays","powDays");
	function updateSelecedtInfo() {
    	var ids = $('#clickedObjId').val();
		
		if (isNullOrUndefined(ids)) {
			ids = $('#lastRowId').val();// 
			ids = $('#' + ids).attr('objid');
			if (isNullOrUndefined(ids)) {
				alert("Please select Alt Coin to process!");
				return;
			}
		}
		
		var idsArr = ids.split(',');
		// alert(ids + "    -----   " + idsArr);
		var sIds = new Array();
		var sValues = new Array();
		var f, fv;
		for (var i=0; i < idsArr.length ; i++) {
			var id = idsArr[i];
			if (!isNullOrUndefined(id)) {
				sIds.push(id);
				
				for (var j=0; j<fields.length; j++) {
					f = $('#'+id+"_" + fields[j]);
					fv = f.attr('tValue');
					if (isNullOrUndefined(fv)) {
						fv = f.val();
					}
					if (!isNullOrUndefined(fv)) {
						if (formatedFields.contains(fields[j])) {
							fv = formatNumber(fv);
						} else if (formatedDayFields.contains(fields[j])) {
							fv = formateDay(fv);
						}
					} else {
						fv = "";
					}
					
					
					sValues.push(" " + fv);
				}
			}
		}
		
		var url = getFullUrl('/jsp/bitcointalk/updatealtcoins.do?altIds=' + sIds.join() + '&altValues=' + sValues.join() + '&attrs=' + fields.join());
		
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
	
	function formatNumber(num) {
		var n = null;
		if (!isNullOrUndefined(num) && num.trim() != '') {
			num = num.toLowerCase();
			var regex = /(\d+\.*\d*)\s*([k|m|b|t])/g;
			var matched = regex.exec(num);
			n = matched != null ? matched[1] * getTimes(matched[2]) : num;
		}
		return n;
	}
	
	function getTimes(c) {
		var times = 1;
		if (c == "k") {
			times = 1000;
		} else if (c == "m") {
			times = 1000000;
		} else if (c == "b") {
			times = 1000000000;
		} else if (c == "t") {
			times = 1000000000000;
		} 
		
		return times;
	}
	
	function formateDay(num) {
		var n = null;
		if (!isNullOrUndefined(num) && num.trim() != '') {
			num = num.toLowerCase();
			var regex = /(\d+\.*\d*)\s*([d|w|m|y])/g;
			var matched = regex.exec(num);
			n = matched != null ? matched[1] * getDayTimes(matched[2]) : num;
		}
		return n;
	}
	
	function getDayTimes(c) {
		var times = 1;
		if (c == "w") {
			times = 7;
		} else if (c == "m") {
			times = 30;
		} else if (c == "y") {
			times = 365;
		}
		
		return times;
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
			box.attr('tvalue', 0);
		} else if (curr == 0) {
			box.attr('class', 'icon-check');
			box.attr('tvalue', 1);
		}
	}
	
	function checkIsShow(id, curr) {
		var box = $('#' + id);
		if (curr == 1) {
			box.attr('class', 'icon-check-empty');
			box.attr('tvalue', 0);
		} else if (curr == 0) {
			box.attr('class', 'icon-check');
			box.attr('tvalue', 1);
		}
	}
	
	var baseSeedUrl = "https://bitcointalk.org/index.php?board=159.";
	// var baseSeedUrl = "https://bitcointalk.org/index.php?board=67.";
	var startgroup = 0;
	var endgroup = 4;
	
	function reInitAnnCoins() {
		var url = getFullUrl("/jsp/bitcointalk/initannboard.do");
		url = url + "?baseSeedUrl=" + baseSeedUrl;// + "&startgroup=" + startgroup + "&endgroup=" + endgroup;
		
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
	
	function updateCoins() {
		var url = getFullUrl("/jsp/bitcointalk/updateallcoins.do");
		url = url + "?baseSeedUrl=" + baseSeedUrl;// + "&startgroup=" + startgroup + "&endgroup=" + endgroup;
		
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
	
	function changeStatus(status) {
		var ids = $('#clickedObjId').val();
		
		if (isNullOrUndefined(ids)) {
			ids = $('#lastRowId').val();// 
			ids = $('#' + ids).attr('objid');
			if (isNullOrUndefined(ids)) {
				alert("Please select Alt Coin to process!");
				return;
			}
		}
		
		/* if (!confirm("Are you sure want to change ID[" + ids + "]'s status?")) {
			return;
		} */
		
		var url = getFullUrl("/jsp/bitcointalk/changeCoinStatus.do");
		url = url + "?altIds=" + ids + "&targetStatus=" + status;
		
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
	
	function showTopicTitle(rowTd) {
		var info = '<strong>' + rowTd.getAttribute("title") + '</strong>';
		$('#rowTitle').html(info);
		return false;
	}
	
	</script>

	
</head>

<body class="page-header-fixed">
		<input type="hidden" id="editable" name="editable" value=${editable }>
		<input type="hidden" id="_required_params_" value="searchField,searchValue,editable">
		
		
		<div class="page-content">
			<div class="container-fluid">
			
				<div class="row-fluid">
					<div class="span12">
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption"><i class="icon-phone"></i> View SIP Statistics </div>
								<div class="tools" style="font-size: 16px">
									<a class="icon-edit" href="#" onclick="changeEditable(${ editable ? 1 : 0 }); "> 
										<c:if test="${editable }">ReadOnly</c:if>
										<c:if test="${!editable }">Editable</c:if>
									 </a>
								</div>
							</div>
							<div class="portlet-body">
							<div class="clearfix">
								 <div class="btn-group caption">
									<div class="controls" style="font-family: 'Segoe UI',Helvetica,Arial,sans-serif;line-height: 20px;font-size: 14px;  margin-bottom: 0px;">
						                <select id="searchField" name="searchField" class="span5 m-wrap" data-placeholder="Choose a Category"  style="width: 190px;vertical-align: middle; margin-bottom: 0px;" tabindex="1">
											<option value="title" ${searchField == 'title' ? "selected" : "" }>Title</option>
											<option value="name" ${searchField == 'name' ? "selected" : "" }>Name</option>
											<option value="abbrName" ${searchField == 'abbrName' ? "selected" : "" }>Abbr Name</option>
											<option value="totalAmount" ${searchField == 'totalAmount' ? "selected" : "" }>Total Amount</option>
											
											<option value="publishContent" ${searchField == 'publishContent' ? "selected" : "" }>Content</option>
									  	</select>
									  	<span style="font-size: 16px;vertical-align: middle; margin-bottom: 0px;"> : </span>
									  	<input type="text" id="searchValue" name="searchValue" value="${searchValue }" 
									  	class="span5 m-wrap medium" style="width: 100px;vertical-align: middle; margin-bottom: 0px;" />
									  	&nbsp; &nbsp;
									  	<button type="button" onclick="searchByField();" style="width: 70px; vertical-align: top;vertical-align: middle; margin-bottom: 0px;" class="btn blue m-wrap">Search</button>
				                  	</div>
				           		</div>			
				           		
								
								<!--  -->
								<div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls">
					                  	<button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="changeStatus(11);">Watch</button>
				                  	</div>
					           	</div>
					            <div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls">
										<span style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; ">
					                  	<button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="reInitAnnCoins();">Seek</button>
					                  	</span>
				                  	</div>
					            </div>
							    <div class="btn-group pull-right ">
									<div class="controls" >
										<!-- <span style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "> -->
										<button type="button"  ${editable ? "class='btn blue' " : "class='btn blue'  disabled='disabled'" } onclick="updateSelecedtInfo();" style="vertical-align: top;" >Update</button>
										<!-- </span> -->
									</div>
								</div>
								<div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls" >
										<span style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; ">
					                  	<button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="changeStatus(1)">Deactivate</button>
					                  	</span>
				                  	</div>
					           	</div>
					            <!-- <div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls">
										<span style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; ">
										   <button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="updateCoins();">Update All</button>
										</span>
				                  	</div>
					            </div> -->
					           
							</div>
							
							<div class="clearfix">
								<div class="alert" style="font-family: 'Segoe UI',Helvetica,Arial,sans-serif;">
					                <span id="rowTitle" style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "></span>
			                  	</div>
							</div>

							<div style="overflow:auto; scrollbar-base-color:#ff6600;">
								<table class="table table-bordered table-hover" id="__grid__">
									<thead>
										<tr id="_table_head_">
											<th id='_un00_'>Link</th>
											<th >Name</th>
											<th >Abbr</th>
											
											<th >Total</th>
											<th >Algo</th>
											<th >Proof</th>
											
											<th >PoW Amount</th>
											<th >PoW Days</th>
											<th >PoW Height</th>
											
											<th >Premined</th>
											<th >Percentage</th>
											
											<th >BReward</th>
											<th >BTime</th>
											<th >HBlocks</th>
											<th >HDays</th>
											<th >Difficulty</th>
											
											<th >Interest</th>
											
											<th >Launch</th>
											<th >Publish</th>
										</tr>
										
										<tr id="_head_fields_" style="display: none;">
											<th ></th>
											<th >name</th>
											<th >abbrName</th>
											
											<th >totalAmount</th>
											<th >algo</th>
											<th >proof</th>
											
											<th >powAmount</th>
											<th >powDays</th>
											<th >powHeight</th>
											
											<th >preMined</th>
											<th >minedPercentage</th>
											
											<th >blockReward</th>
											<th >blockTime</th>
											<th >halfBlocks</th>
											<th >halfDays</th>
											<th >difficultyAdjust</th>
											
											<th >interest</th>
											
											<th >launchTime</th>
											<th >publishDate</th>
										</tr>
									</thead>
									
									<tbody>
										<c:forEach var="ann" items="${anns }">
											<tr class="odd gradeX" objid="${ann.id }" title="TID[${ann.topicId }] ${ann.title }" onclick="showTopicTitle(this);">
	                                         	
	                                         	<td ><a onclick="clickTopicLink(${ann.topicId });" target="_blank" > Ann </a></td>
	                                         	
	                                         	<c:if test="${editable }">
	                                         		<td> <input type="text" id="${ann.id }_name" value="${ann.name }" maxlength="15" style="width: 85px" > </td>
													<td> <input type="text" id="${ann.id }_abbrName" value="${ann.abbrName }" maxlength="5" style="width: 25px"> </td>
													
													<td> <input type="text" id="${ann.id }_totalAmount" value="${ann.totalAmountTxt }" style="width: 65px" > </td>
													<td> <input type="text" id="${ann.id }_algo" value="${ann.algo }" style="width: 65px"> </td>
													<td> <input type="text" id="${ann.id }_proof" value="${ann.proof }" style="width: 45px"> </td>
													
													<td> <input type="text" id="${ann.id }_powAmount" value="${ann.powAmountTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_powDays" value="${ann.powDaysTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_powHeight" value="${ann.powHeightTxt }" style="width: 45px" > </td>
													
													<td> <input type="text" id="${ann.id }_preMined" value="${ann.preMinedTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_minedPercentage" value="${ann.minedPercentageTxt }" style="width: 45px" > </td>
													
													<td> <input type="text" id="${ann.id }_blockReward" value="${ann.blockRewardTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_blockTime" value="${ann.blockTime }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_halfBlocks" value="${ann.halfBlocksTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_halfDays" value="${ann.halfDaysTxt }" style="width: 45px" > </td>
													<td> <input type="text" id="${ann.id }_difficultyAdjust" value="${ann.difficultyAdjust }" style="width: 65px" > </td>
													
													<td> <input type="text" id="${ann.id }_interest" value="${ann.interest }" style="width: 18px"> </td>
													
													<td> <input type="text" id="${ann.id }_launchTime" value="${ann.launchTimeTxt }" style="width: 65px" > </td>
	                                         	</c:if>
	                                         	<c:if test="${!editable }">
													<td>${ann.name }</td>
													<td>${ann.abbrName }</td>
													
													<td>${ann.totalAmountTxt }</td>
		                                         	<td>${ann.algo }</td>
		                                         	<td>${ann.proof }</td>
		                                         	
		                                         	<td>${ann.powAmountTxt }</td>
													<td>${ann.powDaysTxt }</td>
													<td>${ann.powHeightTxt }</td>
													
		                                         	<td>${ann.preMinedTxt }</td>
													<td>${ann.minedPercentageTxt }</td>
													
													<td>${ann.blockRewardTxt }</td>
													<td>${ann.blockTime }</td>
													<td>${ann.halfBlocksTxt }</td>
													<td>${ann.halfDaysTxt }</td>
													<td>${ann.difficultyAdjust }</td>
													
													<td>${ann.interest }</td>
													
													<td>${ann.launchTimeTxt }</td>
	                                         	</c:if>
	                                         	
	                                         	<td>${ann.publishDateTxt }</td>
											</tr>
										</c:forEach>
										<c:if test="${empty anns }">
											<tr>
												<td colspan="19" class="no-data">No Data</td>
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