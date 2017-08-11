package com.tasfe.framework.crud.api.dialect;

public abstract class Dialect {

	public static enum Type {
		MYSQL, ORACLE, SQLSERVER
	}

	public abstract String getLimitString(String querySqlString, int offset, int limit);

	public abstract String getCountString(String querySqlString);

}
