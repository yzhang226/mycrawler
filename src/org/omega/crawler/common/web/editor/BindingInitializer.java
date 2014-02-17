package org.omega.crawler.common.web.editor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 */
public class BindingInitializer implements WebBindingInitializer, ApplicationContextAware {

//	private ApplicationContext applicationContext;

	// 注册属性编辑器，将com.justonetech.ipromis.domain下所有类都注册
	public void initBinder(WebDataBinder binder, WebRequest webRequest) {
//		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//		for (String beanDefinitionName : beanDefinitionNames) {
//			Object bean = applicationContext.getBean(beanDefinitionName);
//			String className = bean.getClass().getSimpleName();
//
//			// log.debug(",,,,,,,,,,,,className" + className);
//            binder.registerCustomEditor(byte[].class,
//                    new ByteArrayMultipartFileEditor());
//            
//			String packageName = bean.getClass().getPackage().getName();
//			String servicePackageName = "com.hp.proctool.daoservice";
//			String beanPackageName = "com.hp.proctool.bean";
//			if (packageName.equals(servicePackageName)) {
//				try {
//					className = className.substring(0, className
//							.indexOf("DAOManager"));
//					Class<?> aClass = forName(beanPackageName + "." + className);
//					binder.registerCustomEditor(aClass, null, new CommonPropertyEditor(bean, true));
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}

//		binder.registerCustomEditor(java.sql.Date.class, new SqlDatePropertyEditor(true));
//		binder.registerCustomEditor(Timestamp.class, new TimestampPropertyEditor(true));

		// binder.initBeanPropertyAccess();
		// binder.registerCustomEditor(String.class, new
		// StringTrimmerEditor(null,true));
//		System.out.println("BindingInitializer initBinder .. ");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
		dateFormat.setLenient(true);
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
		// log.debug("setApplicationContext<<<<<<<<<<<<<<<<<<<");
	}
}