<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->

<%@ include file="/jsp/styleTCG.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
	<meta charset="utf-8" />
	<title>Mobius | TCG</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	
</head>

<body class="page-header-fixed">
	<!-- BEGIN PAGE -->
	<div class="page-content">
		<!-- BEGIN PAGE CONTAINER-->        
		<div class="container-fluid">
			<!-- BEGIN PAGE HEADER-->
			<div class="row-fluid">
				<div class="span12">
					<!-- BEGIN PAGE TITLE & BREADCRUMB-->
					<h3 class="page-title">
						<img src="./assets/img/logo_admin.png">
					</h3>
						<ul class="breadcrumb">
						<li>
							<i class="icon-home"></i>
							<a href="<%=request.getContextPath() %>/jsp/route/show.do">Home</a> 
					  </li>
				  </ul>
					<!-- END PAGE TITLE & BREADCRUMB-->
				</div>
			</div>
			
			<!-- BEGIN PAGE CONTENT-->
			<div class="row-fluid">
				<div class="span12">
					<div class="portlet box red">
						<div class="portlet-title">
							<div class="caption"><i class="icon-bolt"></i>Error Page</div>
						</div>
						<div class="portlet-body">
						<div style="overflow:auto; scrollbar-base-color:#ff6600;">
							<h2>error happened...</h2>
	   	 					<h3>Message: ${exception.message}</h3>
	   	 					<%-- 
	   	 					<c:forEach items="${exception.stackTrace}" var="stackTrace"> 
								<h5>${stackTrace}</h5>
							</c:forEach>
							 --%>
						</div>
						</div>
					</div>
				</div>
			</div>
			
	    </div>
	</div>
	
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">
			2013 &copy; Mobius|TCG.
		</div>
		<div class="footer-tools">
			<span class="go-top">
			<i class="icon-angle-up"></i>
			</span>
		</div>
	</div>

</body>

</html>
