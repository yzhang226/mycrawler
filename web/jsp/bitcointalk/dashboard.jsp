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
		loadLastCoins(1, "lastPostTime", "desc", "lastCoinsDiv", 7, "");
		
		// $('#__grid2__').tcgtable({rowSelect : "single"});
		loadLastCoins(1, "publishDate", "desc", "watchedCoinsDiv", 8, "ann.status=11");
	});
	
	var txtFields = new Array("totalAmount", "launchTime", "publishDate");
	function loadLastCoins(targetPageNo, orderBy, order, divId, colspan, condition) {// lastCoinsDivTBody
		if (targetPageNo <= 0) {
			return;
		}
		var url = context_path + '/jsp/bitcointalk/showCoins4Board.do?pageNo='+targetPageNo+'&order=' + order + '&orderBy=' + orderBy;
		if (!isNullOrUndefined(condition)) url = url + '&condition=' + condition;
		
		$.ajax({
            url: url,  
            data: {  },
            dataType: 'json',
            success: function (param, status) {
            	var tbody = $("#"+divId + " table tbody" );
            	
                if(param) {
                	var paginationDiv = $("#"+divId + "Pagination");
                	
                	tbody.empty();
                	paginationDiv.empty();
                	
                	var row = new StringBuilder();
                	if (param.totalCount > 0) {
                		var alts = param.result;
                		
                		var fieldArr = getFieldArr(divId);
                		
                		// var fieldArr = new Array("name", "abbrName", "totalAmountTxt", "algo", "interestLevel", "launchTime", "publishDateTxt" );
                		
                    	$.each(alts, function(i, ann) {
                    		row.append(createRowContent(ann, fieldArr, divId));
                        });
                	} else {
                		row.append("<tr>");
                		row.append("<td colspan='"+colspan+"' class='no-data'>No Data</td>");
                		row.append("</tr>");
                	}
                	tbody.append(row.toString());
                    row.clear();
                    
                    var anotherParams = '"'+orderBy+'","'+order+'","'+divId+'","'+colspan+'","'+condition+'"';
                    var pagination = createPagination(param.pageNo, param.beginPageNo, param.endPageNo, param.totalPages, param.totalCount, "loadLastCoins", anotherParams);
                    paginationDiv.append(pagination);
                } else {
                	var row = new StringBuilder();
                	row.append("<tr>");
            		row.append("<td colspan='"+colspan+"' class='no-data'>error occur!</td>");
            		row.append("</tr>");
            		tbody.append(row.toString());
                    row.clear();
                }  
            }
        });
		
		// $('#lastCoinsGrid').tcgtable({rowSelect : "single", needPagination : "false"});
	}
	
	function createRowContent(ann, fieldArr, divId) {
		var row = new StringBuilder();
		row.append("<tr class='odd gradeX' objid='").append(ann.id).append("' title='").append(ann.topicId).append(" - ").append(ann.title).append("' onclick='showTopicTitle(this, \"" + divId + "\");'>");
		row.append("<td><a onclick='clickTopicLink(").append(ann.topicId).append(");' target='_blank'>").append(ann.name).append("</a></td>");
		for (var i=1; i<fieldArr.length; i++) {
			row.append("<td>");
			if (txtFields.contains(fieldArr[i])) {
				row.append(eval(new StringBuilder("ann.").append(fieldArr[i]).append("Txt").toString()));
			} else {
				row.append(eval(new StringBuilder("ann.").append(fieldArr[i]).toString()));
			}
			row.append("</td>");
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
        	row.append(createLiLink(1, "First Page", "icon-step-backward", clickFunctionName, anotherParams));
        	row.append(createLiLink(previousPageNo, "Previous Page", "icon-caret-left", clickFunctionName, anotherParams));
        	
			for (var i=beginPageNo; i<endPageNo; i++) {
				row.append("<li class='").append(pageNo == i ? "active" : "").append("'><a id='")
				.append(i)
				.append("' onclick='").append(clickFunctionName).append("(").append(i).append(",").append(anotherParams).append(")'>")
				.append(i).append("</a></li>");
			}
        	
			var nextPageNo = pageNo < totalPages ? pageNo+1 : pageNo;
        	row.append(createLiLink(nextPageNo, "Next Page", "icon-caret-right", clickFunctionName, anotherParams));
        	row.append(createLiLink(totalPages, "Last Page", "icon-step-forward", clickFunctionName, anotherParams));
        } else {
        	row.append(createLiLink(0, "First Page", "icon-step-backward", clickFunctionName, anotherParams));
        	row.append(createLiLink(0, "Last Page", "icon-step-forward", clickFunctionName, anotherParams));
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
	
	function showTopicTitle(rowTd, divId) {// lastCoinsDivRowTitle
		var info = '<strong>' + rowTd.getAttribute("title") + '</strong>';
		$('#'+divId+'RowTitle').html(info);
		
		var trs = $(rowTd).parent().children();
		 
		 for (var i=0; i<trs.length; i++) {
			// trs[i].style.backgroundColor = i % 2 == 0 ? even_color : odd_color;
			$(trs[i]).css('background-color', i % 2 == 0 ? even_color : odd_color);
		 }
		 $(rowTd).removeAttr("style");
		 $(rowTd).css('background-color', selected_color);
		// trs[i].style.backgroundColor = selected_color;
		return false;
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
			                <span id="lastCoinsDivRowTitle" style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "></span>
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
								<th >interest</th>
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
						<div class="caption">Watched Coins</div>
						<div class="tools"></div>
					</div>
					<div class="clearfix">
						<div class="alert" style="font-family: 'Segoe UI',Helvetica,Arial,sans-serif;">
			                <span id="watchedCoinsDivRowTitle" style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "></span>
	                  	</div>
					</div>
					<div style="overflow:auto; scrollbar-base-color:#ff6600;" id="watchedCoinsDiv">
						<table class="table table-bordered table-hover" id="watchedCoinsGrid">
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
								<th >interest</th>
								<th >replies</th>
								<th >views</th>
								<th >publishDate</th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
						</table>
					</div>
					<div class="pagination pagination-centered" id="watchedCoinsDivPagination">
						
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