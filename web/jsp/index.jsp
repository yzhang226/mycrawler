

<HTML>
<HEAD>
<TITLE>Mobius TCG</TITLE>
<!-- <META http-equiv=Content-Type content="text/html; charset=utf-8"> -->
</HEAD>

<frameset id="mainframes" framespacing="0" rows="40,*" frameborder="0" scrolling="no">
	<frame noresize="noresize" name="top" scrolling="no" src="<%=request.getContextPath() %>/jsp/top.jsp">
	<%-- 225,* --%>
	<frameset id="bottomframes" framespacing="0" cols="225,*" frameborder="0" scrolling="no">
		<frame noresize="noresize" name="left" scrolling="no" marginwidth="0" marginheight="0" frameborder="0" src="<%=request.getContextPath() %>/jsp/left.jsp" >
	
		<frame noresize="noresize" id="rightFrame" name="rightFrame" frameborder="0"  src="<%=request.getContextPath() %>/jsp/bitcointalk/showanncoins.do" >
	</frameset>

</frameset>

 
<noframes>
<body >
<p>This page uses frames, but your browser doesn't support them.</p>
</body>
</noframes>

</HTML>
