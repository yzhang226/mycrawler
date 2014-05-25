package org.omega.crawler.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SystemWarmupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(SystemWarmupServlet.class);

	@SuppressWarnings("unchecked")
	public void init() throws ServletException {
		log.info("Warmup System init ...");
		
		ServletContext servletContext = getServletContext();
		
//		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
//		StatusItemService statusItemService = (StatusItemService) context.getBean("statusItemService");
//		Session hibernateSession = statusItemService.openSession();
		
//		List<StatusItem> statusItemsDb = hibernateSession.createCriteria(StatusItem.class).list();
//		
//		StatusItemPool.addAllItem(statusItemsDb);
//		
//		List<StatusItem> statusItems4Filter = new ArrayList<StatusItem>(statusItemsDb.size()+1);
//		statusItems4Filter.add(StatusItemPool.ALL_ITEM);
//		statusItems4Filter.addAll(statusItemsDb);
//		
//		servletContext.setAttribute("statusItems",  statusItemsDb);
//		servletContext.setAttribute("statusItems4Filter",  statusItems4Filter);
//		servletContext.setAttribute("expiredStatus",  StatusItemPool.EXPIRED_ITEM);
//		servletContext.setAttribute("allStatus",  StatusItemPool.ALL_ITEM);
		
//		hibernateSession.close();
		
//		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
//		BitcointalkController bitctrl = (BitcointalkController) context.getBean("bitcointalkController");
//		String baseSeedUrl = "https://bitcointalk.org/index.php?board=159.";
//		int startgroup = 0;
//		int endgroup = 4;
//		
//		bitctrl.initAnnBoard(null, null, baseSeedUrl, startgroup, endgroup);
		
		Utils.setWebDeployPath(servletContext.getRealPath("/"));
		servletContext.setAttribute("image_path",  Utils.getCardImagePath());
		
		
//		ConvertUtils.deregister(Long.class);
//		Converter longcon = new LongConverter(null);
//		ConvertUtils.register(longcon, Long.class);
		
		BeanUtilsBean.getInstance().getConvertUtils().register(false, true, 2);
		
		
		
	}
	
}
