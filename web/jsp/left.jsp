<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>

	<!-- css-->
	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/jsp/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/jsp/assets/css/style.css" rel="stylesheet" type="text/css"/>
	
	<link href="<%=request.getContextPath() %>/jsp/assets/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
	
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/jquery-1.10.1.min.js" type="text/javascript"></script> 
	<script src="<%=request.getContextPath() %>/jsp/assets/scripts/app.js"></script> 
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		
		jQuery(document).ready(function() {
		   	// initiate layout and plugins
		  	App.init();
		   	
		   	var sipTopLink = $('#sipTopLink');
		   	sipTopLink.click();
		   	
		});
		
	</script>
	
	<meta charset="utf-8" />
	<title>Mobius | TCG</title>
</head>
<body>
	<div class="page-container row-fluid">
		<div class="page-sidebar ">
			<ul class="page-sidebar-menu">
				
				<li class="">
					<a href="<%=request.getContextPath() %>/jsp/bitcointalk/showanncoins.do" target="rightFrame"> <i class="icon-globe"></i> <span class="title">Ann Management</span> <span class="arrow "></span> </a>
					<ul class="sub-menu">
						<li>
							<a href="<%=request.getContextPath() %>/jsp/route/toAddRoutePage.do" target="rightFrame"> <i class="icon-plus"></i> Add New Route</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/jsp/route/toEditRouteListPage.do" target="rightFrame"> <i class="icon-edit"></i> Edit Routes</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/jsp/route/showByNotStatus.do?statusId=1&targetPage=activate_routes_list" target="rightFrame"> <i class="icon-key"></i> Activate Routes</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/jsp/route/showByNotStatus.do?statusId=2&targetPage=deactivate_routes_list" target="rightFrame"> <i class="icon-lock"></i>  Deactivate Routes</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/jsp/route/showByNotStatus.do?statusId=3&targetPage=archive_routes_list" target="rightFrame"> <i class="icon-tag"></i> Archive Routes</a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="<%=request.getContextPath() %>//jsp/bitcointalk/showtalktopics.do" target="rightFrame"> <i class="icon-bell"></i> <span class="title">Talk Management</span> <span class="arrow "></span> </a>
					<ul class="sub-menu">
						<li >
							<a href="<%=request.getContextPath() %>/jsp/cdr/showByDay.do" target="rightFrame"> <i class="icon-bullhorn"></i> Calls Made per Day </a>
						</li>
						<li >
							<a href="<%=request.getContextPath() %>/jsp/cdr/showCallCtrls.do" target="rightFrame"> <i class="icon-leaf"></i> Call Manager Control </a>
						</li>
					</ul>
				</li>
				<li class="">
					<a href="<%=request.getContextPath() %>/jsp/resutil/tosiputil.do" target="rightFrame"> <i class="icon-cloud"></i> <span class="title"> Resource Utilization </span> <span class="arrow "></span> </a>
					<ul class="sub-menu">
						<li >
							<a href="<%=request.getContextPath() %>/jsp/resutil/tosiputil.do" target="rightFrame"> <i class="icon-bullhorn"></i> SIP Utilization </a>
						</li>
						<%-- 
						<li >
							<a href="" target="rightFrame"> <i class="icon-leaf"></i> Calling Card Utilization </a>
						</li> --%>
					</ul>
				</li>
				
				<li class="">
					<a href="<%=request.getContextPath() %>/jsp/account/toSettingPage.do" target="rightFrame"> <i class="icon-asterisk"></i> <span class="title">TCG Setting</span> </a>
					<ul class="sub-menu">
						
					</ul>
				</li>
				
			</ul>
		</div>
	</div>
	</body>
</html>