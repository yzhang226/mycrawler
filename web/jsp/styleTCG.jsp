<%@ page contentType="text/html; charset=UTF-8" %>
<html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- <%@ include file="left.jsp" %> --%>

<head>
	
	
	
	<!-- css-->
	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/jsp/assets/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/jsp/assets/css/tcg.css" rel="stylesheet" type="text/css"/>
	
	<link href="<%=request.getContextPath() %>/jsp/assets/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
	
	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.css" rel="stylesheet" type="text/css"/>
	
<%-- 	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css" /> --%>
	
	
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-1.10.1.min.js" type="text/javascript"></script> 
	<script src="<%=request.getContextPath() %>/jsp/assets/scripts/app.js"></script> 
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	
		<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-form/jquery.form.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script> 
	<script src="<%=request.getContextPath() %>/jsp/assets/scripts/ui-jqueryui.js"></script>
	
<%-- 	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/datetimepicker/jquery.datetimepicker.js"></script> --%>
	
	
	<script src="<%=request.getContextPath() %>/jsp/assets/scripts/tcg-plugins.js"></script>
	
	<script type="text/javascript">
		
		var selected_color = '#fff8dc';// '#f5f5f5'
		var even_color = "#f5f5f5";
		var odd_color = "";
		var context_path = "<%=request.getContextPath() %>";
		var img_path = context_path + '/jsp/assets/img';
		

		function AddClearButton(input) {
		    setTimeout(function() {
		        var buttonPane = $(input).datepicker("widget").find(".ui-datepicker-buttonpane");

		        var btn = $('<button id="customClearButton" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" type="button">Clear</button>');
		        btn.unbind("click").bind("click", function() {
		        	$.datepicker._clearDate(input);
		        });

		        // Check if buttonPane has that button
		        if (buttonPane.has('#customClearButton').length == 0) btn.appendTo(buttonPane);
		    }, 1);
		}
		
		jQuery(document).ready(function() {
		   	// initiate layout and plugins
		  	App.init();
		  	UIJQueryUI.init();
		  	
		  	// $('#__grid__').tcgtable();
		  	
		});
		
		
		function changeSipStatus(statusId, targetPage) {
			var sipId = $('#clickedObjId').val();
			
			if (isNullOrUndefined(sipId)) {
				alert("Please select SIP Account to process!");
				return;
			}
			
			var url = getFullUrl('/jsp/sip/changeSipStatus.do?statusId=' + statusId + '&sipId=' + sipId);
			
			$.ajax({
					url : url,
					success : function(data) {
						var resp = $.parseJSON(data);
						alert(resp.message);
						
						if (resp.success == 'true') {
							$('#clickedObjId').val('');
							
							// var url = context_path + '/jsp/sip/showByNotStatus.do?statusId=' + statusId + '&targetPage=' + targetPage + '&pageNo=' + $('#pageNo').val();
							var url = combineFullUrlDefault(null);
							window.location.href=url;
						}
						
					}
			});
		}
		
		function changeCardStatus(statusId, targetPage) {
			var cardId = $('#clickedObjId').val();
			
			if (isNullOrUndefined(cardId)) {
				alert("Please select Calling Card to process!");
				return;
			}
			
			var url = getFullUrl("/jsp/cards/changeCardStatus.do?statusId=" + statusId + "&cardId=" + cardId);
			
			$.ajax({
					url : url,
					success : function(data) {
						var resp = $.parseJSON(data);
						alert(resp.message);
						
						if (resp.success == 'true') {
							$('#clickedObjId').val('');
							
							// var url = getFullUrl('/jsp/cards/showByNotStatus.do?statusId=' + statusId + '&targetPage=' + targetPage + '&pageNo=' + $('#pageNo').val());
							var url = combineFullUrlDefault(null);
							window.location.href=url;
						}
					}
			});
		}
		
		/** check obj is null or empty or undefined*/
		function isNullOrUndefined(obj) {
			return typeof(obj) == 'undefined' || obj == null || obj == '';
		}
		
// 		function openPageAtCenter(url, title, w, h) {
// 			var l = (screen.width - w) / 2;
// 			var t = (screen.height - h) / 2;
// 			var addtionalInfo = 'width=' + w + ', height=' + h + ', top=' + t + ', left=' + l;
// 			addtionalInfo += ', toolbar=no, scrollbars=yes, menubar=no, location=no, resizable=yes, status=no';

// 			window.open(url, title, addtionalInfo);
// 		}
		
		// var popupWin;
		function openPageAtCenter(url, title, width, height) {
			title = "TCG";
			
			var leftPosition, topPosition;
			leftPosition = (window.screen.width / 2) - ((width / 2) + 10);
			topPosition = (window.screen.height / 2) - ((height / 2) + 50);

			var addtionalInfo = "status=no,height=" + height + ",width=" + width + ",resizable=yes,left=" + leftPosition
								+ ",top=" + topPosition + ",screenX=" + leftPosition + ",screenY=" + topPosition 
								+ ",toolbar=no,menubar=no,scrollbars=yes,location=no,directories=no";
			var popupWin = window.open("", title, addtionalInfo);
			
			if (popupWin.location.href === "about:blank") {// do not exist 
				popupWin = window.open(url, title, addtionalInfo);
			// p.location='checkOrder.action?orderId='+data;
			} else { // exist 
				alert("You already opened one popup Edit Page, please close opened page first!");
				popupWin.focus();
			}
			
		}

		function changeRouteStatus(statusId, targetPage) {
			var routeId = $('#clickedObjId').val();

			if (isNullOrUndefined(routeId)) {
				alert("Please select Call Route to process!");
				return;
			}

			var url = getFullUrl("/jsp/route/changeRouteStatus.do?statusId="
					+ statusId + "&routeId=" + routeId);

			$.ajax({
				url : url,
				success : function(data) {
					var resp = $.parseJSON(data);
					alert(resp.message);

					if (resp.success == 'true') {
						$('#clickedObjId').val('');

						// var url = getFullUrl('/jsp/route/showByNotStatus.do?statusId=' + statusId + '&targetPage=' + targetPage + '&pageNo=' + $('#pageNo').val());
						var url = combineFullUrlDefault(null);
						window.location.href = url;
					}
				}
			});
		}

		function getFullUrl(url) {
			return context_path + url;
		}

		function sortByField(order) {
			var tdId = $('#clickedTdId').val();
			var field = $('#field' + tdId).text();

			$('#order').val(order);
			$('#orderBy').val(field);

			// url = url + predefinedChar + 'order=' + order + '&orderBy=' + field + '&pageNo=' + $('#pageNo').val() + '&statusId=' + $('#statusId').val(); 
			url = combineFullUrl(null, order, field, $('#pageNo').val(), $(
					'#statusId').val());

			window.location.href = url;
		}

		function combineFullUrl(url, order, orderby, pageNo, statusId) {
			if (isNullOrUndefined(url)) {
				url = window.location.href;
			}
			if (url.contains('?')) {
				var markIdx = url.indexOf('?');

				url = url.substring(0, markIdx);
			}

			url = url + '?order=' + order + '&orderBy=' + orderby + '&pageNo='
					+ pageNo + '&statusId=' + statusId;

			var required = $('#_required_params_').val();
			if (!isNullOrUndefined(required)) {
				var params = required.split(',');
				for ( var i = 0; i < params.length; i++) {
					url = url + '&' + params[i] + '='
							+ $('#' + params[i]).val();
				}
			}

			return url;
		}

		function combineFullUrlDefault(url) {
			return combineFullUrl(url, $('#order').val(), $('#orderBy').val(),
					$('#pageNo').val(), $('#statusId').val());
		}
		function recombineFullUrl(url) {
			return combineFullUrl(url, '', '', '', $('#statusId').val());
		}

		function isSuccess(resp) {
			return resp == 'true';
		}
		
		var bitcointalk_base_url = "https://bitcointalk.org/index.php?topic=";
		function clickTopicLink(topic_id) {
			var turl = bitcointalk_base_url + topic_id + ".0";
			window.open(turl, "_blank");
			window.focus();
			return false;
		}
		
		function showTopicTitle(rowTd, showJqId) {
			var info = '<strong>' + rowTd.getAttribute("title") + '</strong>';
			$('#'+showJqId).html(info);
			
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

<body>
	
	<c:if test="${not empty params }">
		<c:set var="beginPageNo" value="${params.beginPageNo }"></c:set>
		<c:set var="endPageNo" value="${params.endPageNo }"></c:set>
		<c:set var="pageNo" value="${params.pageNo }"></c:set>
		<c:set var="totalPages" value="${params.totalPages }"></c:set>
		<c:set var="totalCount" value="${params.totalCount }"></c:set>
		<c:set var="filterStyle" value="style='background: #54C571'"></c:set>
	</c:if>
	
	

	<input type="hidden" id="lastRowId" value="">
	<input type="hidden" id="lastColor" value="">
	<input type="hidden" id="clickedObjId" name="clickedObjId" value="">
	
	<input type="hidden" id="clickedTdId" name="clickedTdId" value="">
	
	<input type="hidden" id='order' name='order' value='${params.order }'>
	<input type="hidden" id='orderBy' name='orderBy' value='${params.orderBy }'>
	<input type="hidden" id='pageNo' name='pageNo' value='${pageNo }'>
	
	<input type="hidden" id='statusId' name='statusId' value='${statusId }'>
	
	<input type="hidden" id='targetPage' name='targetPage' value='${targetPage }'>
	
	
	<div id="pop-up">
		<div >
			<div class="popup-left" style="width: 20%">
				<img alt="" src="<%=request.getContextPath() %>/jsp/assets/img/hmenu-asc.gif">
			</div>
			<div class="popup-right" style="width: 80%">
				<a id="_sortAscLink" style="cursor: pointer;"><span>Sort Ascending</span></a>
			</div>
		</div>
		<div id="nextline1" style="clear: left;"></div>

		<div>
			<div class="popup-left" style="width: 20%">
				<img alt="" src="<%=request.getContextPath() %>/jsp/assets/img/hmenu-desc.gif">
			</div>
			<div class="popup-right" style="width: 80%">
				<a id="_sortDescLink" style="cursor: pointer;"><span>Sort Descending</span></a>
			</div>
		</div>
		<div id="nextline2" style="clear: left; border: solid 1px #E0E0E0"></div>

		<div id="pop_up_cols">
			<div class="popup-left" style="width: 20%">
				<img alt="" src="<%=request.getContextPath() %>/jsp/assets/img/columns.gif"> 
			</div>
			<div class="popup-left" style="width: 55%">
				<span>Columns</span> 
			</div>
			<div class="popup-right" style="width: 20%">
				<img alt="" src="<%=request.getContextPath() %>/jsp/assets/img/page-next.gif">
			</div>
			
		</div>
		<div id="nextline3" style="clear: left;"></div>

	</div>

	<div id="__columns" ></div>
	
<!-- 	<div class="page-content"> -->
<!-- 		<div class="container-fluid"> -->

			<div class="row-fluid">
				<div class="span12">
					<ul class="breadcrumb">
						<li><i class="icon-home"></i> <a id="bread" href="<%=request.getContextPath()%>/jsp/bitcointalk/showdashboard.do">Dashboard</a> /</li>
						<li><a id="bread" href="<%=request.getContextPath()%>/jsp/bitcointalk/showinterestcoins.do">Interest Coins</a> /</li>
						<li><a id="bread" href="<%=request.getContextPath()%>/jsp/bitcointalk/showanncoins.do">Alt Coins</a> /</li>
<%-- 						<li><a id="bread" href="<%=request.getContextPath()%>/jsp/bitcointalk/showtalktopics.do">Alt Coins Topic</a></li> --%>
						<li><a id="bread" href="<%=request.getContextPath()%>/jsp/bitcointalk/toDiagramPage.do">Alt Coins Topic</a></li>
					</ul>
				</div>
			</div>

<!-- 		</div> -->
<!-- 	</div> -->
	
</body>
</html>