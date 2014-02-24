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
		   	
		   	// var sipTopLink = $('#sipTopLink');
		   	// sipTopLink.click();
		   	
// 		   	$('#bread').unbind('click').click(function() {
// 				$('#bread').attr('class', '');
// 				$(this).attr('class', 'active');
// 			});
		   	
		});
		
	</script>
	
	<meta charset="utf-8" />
	<title>Mobius | TCG</title>
</head>
<body>
	<%-- 
	<ol class="breadcrumb">
		<li><a id="bread" href="<%=request.getContextPath() %>/jsp/bitcointalk/showanncoins.do" class="active">Alt Coins</a></li>
		<li><a id="bread" href="<%=request.getContextPath() %>/jsp/bitcointalk/showtalktopics.do">Alt Coins Topic</a></li>
	</ol>
 	--%>
	
</body>
</html>