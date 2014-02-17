package org.omega.crawler.common;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InitSessionListener implements HttpSessionListener {

	private static final Log log = LogFactory.getLog(InitSessionListener.class);
	
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
//		log.info("Init session up, session_id is " + session.getId());
		
		session.setAttribute("now", new Date());
//		session.setAttribute("statusItems", session.getServletContext().getAttribute("statusItems"));
	}

	public void sessionDestroyed(HttpSessionEvent se) {
//		log.info("Destory session end, session_id is " + se.getSession().getId());
	}

}
