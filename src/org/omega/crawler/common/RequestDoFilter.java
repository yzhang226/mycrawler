package org.omega.crawler.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestDoFilter implements Filter {
	
	private static Log log = LogFactory.getLog(RequestDoFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException { 
		
	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    
		String contextPath = req.getContextPath();
		String url = req.getRequestURI();
		url = url.substring(contextPath.length());
		
		HttpSession session = ((HttpServletRequest) request).getSession();
//		AccountBean account = (AccountBean) session.getAttribute(Constants.ATTR_LOGINED_ACCOUNT);
//		boolean isLogined = account != null && account.isLogined();
//		
//		if (isLogined) {
			if (url.toLowerCase().contains("show") || url.toLowerCase().contains("listpage") || url.toLowerCase().contains("resutil")) {// 
				Page params = new Page();
				params.setAutoCount(true);
				
				Integer pageNo = 1;
				try {
					if (Utils.isNotEmpty(request.getParameter("pageNo"))) {
						pageNo = Integer.valueOf(request.getParameter("pageNo"));
					}
				} catch (Exception e) {
					log.error("Parse pageNo of parameter error.", e);
				}
				String orderBy = (String) request.getParameter("orderBy");
				String order = (String) request.getParameter("order");
				
				if (pageNo != null && pageNo > 0) params.setPageNo(pageNo);
				if (Utils.isNotEmpty(orderBy)) params.setOrderBy(orderBy);
				if (Utils.isNotEmpty(order)) params.setOrder(order);
				
				request.setAttribute("params", params);
			}
			
			chain.doFilter(request,response);
//		} else {
//			
//			if (url.equals("/jsp/account/toLoginPage.do") 
//					|| url.equals("/jsp/account/login.do") 
//					|| url.equals("/jsp/account/logout.do")) {
//				chain.doFilter(request,response);
//			} else {
//				 res.sendRedirect(contextPath + "/jsp/account/toLoginPage.do");
//			}
//		}
		
	}

	public void destroy() { 
		
	}

}
