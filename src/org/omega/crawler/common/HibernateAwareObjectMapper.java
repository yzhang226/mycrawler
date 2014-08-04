package org.omega.crawler.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {
	
	private static final long serialVersionUID = -2484746504188099444L;

	public HibernateAwareObjectMapper() {
		Hibernate4Module hm = new Hibernate4Module();
		hm.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
		hm.enable(Hibernate4Module.Feature.REQUIRE_EXPLICIT_LAZY_LOADING_MARKER);
		registerModule(hm);
		disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

//	public void setPrettyPrint(boolean prettyPrint) {
//		configure(SerializationFeature.INDENT_OUTPUT, prettyPrint);
//	}
}
