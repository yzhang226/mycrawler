package org.omega.crawler.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.omega.crawler.bean.Account;

public class UrlSessionFilter implements Filter {

	public static final String[] OUT_PRIV_ARRAY = {"/jsp/sysmgt/login.do?method=login","/jsp/login.jsp","/jsp/relogin.jsp","/jsp/system/desktop/desktop.jsp","/jsp/noPrivilege.jsp","/"};
	public static final List<String> outPrivileges = Arrays.asList("");
	
	public static final String URL_LOGIN = "/jsp/account/login.jsp";
	
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String url = req.getRequestURI();
		String methodUrl = url;
		String queryUrl = url;
//		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>> url is:" + url + "  ContextPath:" + req.getContextPath());
		if (req.getParameter("method") != null) {
			methodUrl = url + "?method=" + req.getParameter("method");
		}
		if (req.getQueryString() != null) {
			queryUrl = url + "?" + req.getQueryString();
		}
		
		if (outPrivileges.contains(methodUrl)) {
			chain.doFilter(request, response);
			return;
		}
		
//		HttpSession session = req.getSession(true);
//		Account account = (Account) session.getAttribute(Constants.ATTR_LOGINED_ACCOUNT);
//		
//		if (account == null) {
//			if (url.contains("login.do")) {
//				chain.doFilter(request, response);
//			} else {
//				System.out.println("need to redirect...");
////				req.getRequestDispatcher(URL_LOGIN).forward(request, response);
//				res.sendRedirect(req.getContextPath() + URL_LOGIN);
//			}
//		} else if (account.isLogined()) {
//			chain.doFilter(request, response);
//		} else {
//			res.sendRedirect(req.getContextPath() + URL_LOGIN);
//		} 
		
		
		
		
	}

	public void destroy() {

	}

}
