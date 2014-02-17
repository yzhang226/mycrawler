<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>


<form  action="<%=request.getContextPath() %>/jsp/account/login.do?method=login" id="loginForm" method="post">
						<input type="text" placeholder="Username" name="username"/>
						<input type="password" placeholder="Password" name="password"/>
				<button type="submit" > Submit </button>
		</form>

</body>
</html>