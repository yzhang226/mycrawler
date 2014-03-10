package org.omega.crawler.common;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.NumericBooleanType;

public class TcgMySQLDialect extends MySQL5Dialect {

	public TcgMySQLDialect() {
		super();
		registerFunction("regexp", new SQLFunctionTemplate(NumericBooleanType.INSTANCE, "?1 REGEXP ?2"));
	}

}
