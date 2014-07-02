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
		loadLastCoins(1, "lastPostTime", "desc", "lastCoinsDiv");
		
		$('#__grid2__').tcgtable({rowSelect : "single"});
		
	});
	
	function loadLastCoins(targetPageNo, orderBy, order, divId) {// lastCoinsDivTBody
		if (targetPageNo <= 0) {
			return;
		}
		var url = context_path + '/jsp/bitcointalk/showCoins4Board.do?pageNo='+targetPageNo+'&order=' + order + '&orderBy=' + orderBy;
		
		$.ajax({
            url: url,  
            data: {  },
            dataType: 'json',
            success: function (param, status) {
                if(param) {
                	
                	$("#lastCoinsDivTBody" ).empty();
                	$("#lastCoinsDivPagination").empty();
                	
                	var row = new StringBuilder();
                	if (param.totalCount > 0) {
                		var alts = param.result;
                		
                		// var fieldArr = getFieldArr(divId);
                		
                		var fieldArr = new Array("name", "abbrName", "totalAmountTxt", "algo", "interestLevel", "launchTime", "publishDateTxt" );
                		
                    	$.each(alts, function(i, ann) {
                    		row.append(createRowContent(ann, fieldArr));
                    		
                        });
                	} else {
                		row.append("<tr>");
                		row.append("<td colspan='7' class='no-data'>No Data</td>");
                		row.append("</tr>");
                	}
                	$("#lastCoinsDivTBody").append(row.toString());
                    row.clear();
                    
                    var anotherParams = '"'+orderBy+'","'+order+'","'+divId+'"';
                    var pagination = createPagination(param.pageNo, param.beginPageNo, param.endPageNo, param.totalPages, param.totalCount, "loadLastCoins", anotherParams);
                    $("#lastCoinsDivPagination").append(pagination);
                } else {
                	var row = new StringBuilder();
                	row.append("<tr>");
            		row.append("<td colspan='7' class='no-data'>error occur!</td>");
            		row.append("</tr>");
            		$("#lastCoinsDivTBody").append(row.toString());
                    row.clear();
                }  
            }
        });
		
		// $('#lastCoinsGrid').tcgtable({rowSelect : "single", needPagination : "false"});
	}
	
	function createRowContent(ann, fieldArr) {
		var row = new StringBuilder();
		row.append("<tr class='odd gradeX' objid='").append(ann.id).append("' title='").append(ann.title).append("' onclick='showTopicTitle(this, \"rowTitle\");'>");
		row.append("<td><a onclick='clickTopicLink(").append(ann.topicid).append(");' target='_blank'>").append(ann.name).append("</a></td>");
		for (var i=1; i<fieldArr.length; i++) {
			row.append("<td>").append(eval(new StringBuilder("ann.").append(fieldArr[i]).toString())).append("</td>");
		}
		row.append("</tr>");
		
		return row.toString();
	}
	
	function createPagination(pageNo, beginPageNo, endPageNo, totalPages, totalCount, clickFunctionName, anotherParams) {
		var row = new StringBuilder();
		row.append("<ul>");
        if (totalPages > 0) {
        	// var pageNo = param.pageNo;
        	var previousPageNo = pageNo == 1 ? 1 : pageNo - 1;
        	row.append(createLiLink(1, "First Page", "icon-step-backward"));
        	row.append(createLiLink(previousPageNo, "Previous Page", "icon-caret-left"));
        	
			for (var i=beginPageNo; i<endPageNo; i++) {
				row.append("<li class='").append(pageNo == i ? "active" : "").append("'><a id='")
				.append(i)
				.append("' onclick='").append(clickFunctionName).append("(").append(i).append(",").append(anotherParams).append(")'>")
				.append(i).append("</a></li>");
			}
        	
			var nextPageNo = pageNo < totalPages ? pageNo+1 : pageNo;
        	row.append(createLiLink(nextPageNo, "Next Page", "icon-caret-right"));
        	row.append(createLiLink(totalPages, "Last Page", "icon-step-forward"));
        } else {
        	row.append(createLiLink(0, "First Page", "icon-step-backward"));
        	row.append(createLiLink(0, "Last Page", "icon-step-forward"));
        }
        row.append("<li><a>Total ").append(totalCount).append(" Records</a></li>"); //${totalCount }
        row.append("</ul>");
        return row.toString();
	}
	function createLiLink(id, title, iconClz, clickFunctionName, anotherParams) {
		var row = new StringBuilder();
		row.append("<li >");
		row.append("<a id='").append(id).append("'").append(" title='").append(title).append("'");
		row.append(" onclick='").append(clickFunctionName).append("(").append(id).append(",").append(anotherParams).append(")'>");
		row.append("<i class='").append(iconClz).append("'></i>");
		row.append("</a>");
		row.append("</li>");
		return row.toString();
	}
	
	function getFieldArr(divId) {
		var fieldThs = $("#"+divId+" table thead").children("#_head_fields_").first().children();
		var fieldsArr = new Array();
		for ( var i = 0; i < fieldThs.length; i++) {
			fieldsArr.push($.trim($(fieldThs[i]).text()));
	    }
		return fieldsArr;
	}
	
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
					<div class="clearfix">
						<div class="alert" style="font-family: 'Segoe UI',Helvetica,Arial,sans-serif;">
			                <span id="rowTitle" style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "></span>
	                  	</div>
					</div>
					
					<div style="overflow:auto; scrollbar-base-color:#ff6600;" id="lastCoinsDiv">
						<table class='table table-bordered table-hover' id='lastCoinsGrid'>
						<thead>
							<tr id='_table_head_'>
								<th >Name</th>
								<th >Abbr</th>
								<th >Total</th>
								<th >Algo</th>
								<th >Interest</th>
								<th>Launch</th>
								<th>Publish Date</th>
							</tr>
							<tr id='_head_fields_' style='display: none;'>
								<th >name</th>
								<th >abbrName</th>
								<th >totalAmount</th>
								<th >algo</th>
								<th >interestLevel</th>
								<th >launchTime</th>
								<th >publishDate</th>
							</tr>
						</thead>
						<tbody id="lastCoinsDivTBody">
							
						</tbody>
						</table>
					</div>
					<div class="pagination pagination-centered" id="lastCoinsDivPagination">
						
					</div>
				</div>
				<div class="span6">
					<div class="portlet-title">
						<div class="caption">Most replied Coins</div>
						<div class="tools"></div>
					</div>
					<div style="overflow:auto; scrollbar-base-color:#ff6600;">
						<table class="table table-bordered table-hover" id="__grid2__">
						<thead>
							<tr id="_table_head_">
								<th >Name</th>
								<th >Abbr</th>
								<th >Total</th>
								<th >Algo</th>
								<th >Interest</th>
								<th >Replies</th>
								<th >Views</th>
								<th>Publish Date</th>
							</tr>
							<tr id="_head_fields_" style="display: none;">
								<th >name</th>
								<th >abbrName</th>
								<th >totalAmount</th>
								<th >algo</th>
								<th >interestLevel</th>
								<th >replies</th>
								<th >views</th>
								<th >publishDate</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="ann" items="${altsByReply }">
								<tr class="odd gradeX" objid="${ann.id }" title="${ann.title }">
									<td> <a href='${ann.link }' target='_blank'>${ann.name }</a> </td>
									<td>${ann.abbrName }</td>
									<td>${ann.totalAmountTxt }</td>
									<td>${ann.algo }</td>
									<td>${ann.interestLevel }</td>
									<td>${ann.replies }</td>
									<td>${ann.views }</td>
	                                <td>${ann.publishDateTxt }</td>
								</tr>
							</c:forEach>
							
							<c:if test="${empty alts }">
								<tr>
									<td colspan="8" class="no-data">No Data</td>
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