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
		
		var mode = editable ? "multiple" : "single";
		
		$('#__grid__').tcgtable({rowSelect : mode});
		
	});
	
	
	
	</script>

	
</head>

<body class="page-header-fixed">
	<input type="hidden" id="editable" name="editable" value=${editable }>
	<input type="hidden" id="_required_params_" value="searchField,searchValue,interest,editable">

	<div class="page-content">
		<div class="container-fluid">

			<div class="row-fluid">
				<div class="span6">
					<div class="portlet-title">
						<div class="caption">coins in last 30 days</div>
						<div class="tools"></div>
					</div>
					<div style="overflow:auto; scrollbar-base-color:#ff6600;">
						<table class="table table-bordered table-hover" id="__grid__">
						<thead>
							<tr id="_table_head_">
								<th >Name</th>
								<th >Abbr</th>
								<th >Total</th>
								<th >Algo</th>
								<th >Interest</th>
								
								<th>Launch</th>
								<th>Publish Date</th>
							</tr>
							
							<tr id="_head_fields_" style="display: none;">
								<th >name</th>
								<th >abbrName</th>
								<th >totalAmount</th>
								<th >algo</th>
								<th >interestLevel</th>
								
								<th >launchTime</th>
								<th >publishDate</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="ann" items="${alts }">
								<tr class="odd gradeX" objid="${ann.id }">
									<td> <a href='${ann.link }' target='_blank'>${ann.name }</a> </td>
									<td>${ann.abbrName }</td>
									<td>${ann.totalAmountTxt }</td>
									<td>${ann.algo }</td>
									<td>${ann.interestLevel }</td>
									
									<td>${ann.launchTime }</td>
	                                <td>${ann.publishDate }</td>
								</tr>
							</c:forEach>
							
							<c:if test="${empty alts }">
								<tr>
									<td colspan="7" class="no-data">No Data</td>
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
				<div class="span6"></div>
			</div>
			<div class="row-fluid">
				<div class="span6"></div>
				<div class="span6"></div>
			</div>
			
		</div>
	</div>

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