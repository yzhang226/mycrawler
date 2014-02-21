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
	
	jQuery(document).ready(function() {
		$('#__grid__').tcgtable();
		
	});
	
	function searchByField() {
		var searchValue = $('#searchValue').val();
		
		if (isNullOrUndefined(searchValue) || searchValue.trim() == '') {
			// alert('Please input search value!');
		}
		var url = combineFullUrlDefault(null);
		
		window.location.href = url;
	}
	
	function reInitTopics() {
		
		var url = getFullUrl("/jsp/bitcointalk/crawlertopicboard.do");
		
		// var baseSeedUrl = "https://bitcointalk.org/index.php?board=159.";
		var baseSeedUrl = "https://bitcointalk.org/index.php?board=67.";
		var startgroup = 20;
		var endgroup = 30;
		
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
		<input type="hidden" id="_required_params_" value="searchField,searchValue">
		
		<!-- BEGIN PAGE -->
		<div class="page-content">
		
			<!-- BEGIN PAGE CONTAINER-->        
			<div class="container-fluid">
				<!-- BEGIN PAGE HEADER-->
				<div class="row-fluid">
					<div class="span12">
				  
						<!-- BEGIN PAGE TITLE & BREADCRUMB-->
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
						<!-- END PAGE TITLE & BREADCRUMB-->
					</div>
				</div>
				<!-- END PAGE HEADER-->
				<!-- BEGIN PAGE CONTENT-->
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN EXAMPLE TABLE PORTLET-->
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption"><i class="icon-phone"></i> View SIP Statistics </div>
								<div class="tools">
								</div>
							</div>
							<div class="portlet-body">
							<div class="clearfix">
								
					           <div class="btn-group pull-right" style="vertical-align: top;">
									<div class="controls">
					                  	<button class="btn blue m-wrap" type="button" style="vertical-align: middle;" onclick="reInitTopics();">ReInit Topics</button>
				                  	</div>
					           </div>
					           
					           <div class="control-group">
									<div class="controls" >
						                <select id="searchField" name="searchField" class="span5 m-wrap" data-placeholder="Choose a Category"  style="width: 190px" tabindex="1">
											<option value="title" ${searchField == 'title' ? "selected" : "" }>Title</option>
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
											<th>Title</th>
											<th>Author</th>
											<th>Replies</th>
											<th>Views</th>
											
										</tr>
										<tr id="_head_fields_" style="display: none;">
											<th >publishDate</th>
											<th >title</th>
											<th >author</th>
											<th >replies</th>
											<th >views</th>
										</tr>
									</thead>
									
									<tbody>
										<c:forEach var="t" items="${topics }">
											<tr class="odd gradeX">
	                                         	<td>${t.publishDate }</td>
	                                         	<td> <a href="${t.link }" target="_blank"> ${t.title } </a> </td>
	                                         	<td>${t.author }</td>
												<td>${t.replies }</td>
												<td>${t.views }</td>
											</tr>
										</c:forEach>
										<c:if test="${empty topics }">
											<tr>
												<td colspan="5" class="no-data">No Data</td>
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