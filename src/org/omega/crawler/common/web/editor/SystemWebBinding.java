package org.omega.crawler.common.web.editor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class SystemWebBinding implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
//		dateFormat.setLenient(false);
		
//		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		datetimeFormat.setLenient(false);

//		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
//		binder.registerCustomEditor(java.sql.Timestamp.class, new CustomTimestampEditor(datetimeFormat, true));
		System.out.println(" SystemWebBinding initBinder");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		dateFormat.setLenient(true);
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
