package com.tasfe.framework.crud.mysql.impls.mybatis.provider;

import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@SuppressWarnings("all")
public class BaseProvider<T> {
    /*protected final Logger logger = LoggerFactory.getLogger(getClass());
	private String tableName;
	private Class<?> modelClass;
	private static ThreadLocal<Class<?>> threadModelClass = new ThreadLocal<Class<?>>();
	private static final String OPERATOR_EQUAL = " = ";
	private static final String OPERATOR_LIKE = " like ";
	private static String SERVER_IP =  "";

	static{
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			SERVER_IP = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
		}
	}


	private void initFromThreadLocal() {
		modelClass = BaseProvider.threadModelClass.get();
		tableName = modelClass.getAnnotation(Table.class).name();
	}

	public static void setModelClass(Class<?> modelClass) {
		BaseProvider.threadModelClass.set(modelClass);
	}

	public static void threadLocalRemove(){
		if (BaseProvider.threadModelClass != null) BaseProvider.threadModelClass.remove();
	}

	public String getAll() {
		initFromThreadLocal();
		SQL sql = SELECT_FROM();
		sql = ORDER(null, sql);
		return sql.toString();
	}

	public String getById() {
		initFromThreadLocal();
		SQL sql = SELECT_FROM().WHERE("ID = #{id}");
		return sql.toString();
	}

	public String count(T findParams) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		SQL sql = new SQL() {
			{
				SELECT("COUNT(ID)");
				FROM(tableName);
			}
		};
		sql = WHERE(findParams, sql, OPERATOR_EQUAL);
		return sql.toString();
	}

	public String countLike(T findParams) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		SQL sql = new SQL() {
			{
				SELECT("COUNT(ID)");
				FROM(tableName);
			}
		};
		sql = WHERE(findParams, sql, OPERATOR_LIKE);
		return sql.toString();
	}

	public String countQuery(Map<String, Object> customQueryParams) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		SQL sql = new SQL() {
			{
				SELECT("COUNT(ID)");
				FROM(tableName);
			}
		};

        sql = WHERE_CUSTOM(customQueryParams, sql);
		return sql.toString();
	}

    public String query(Map<String, Object> dataMap) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        List<Sort> sortList = (List<Sort>) dataMap.get("sortList");

		initFromThreadLocal();
		SQL sql = SELECT_FROM();
        sql = WHERE_CUSTOM(dataMap, sql);
		sql = ORDER(sortList, sql);
		return sql.toString();
	}

	public String find(T findParams) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		SQL sql = SELECT_FROM();
		sql = WHERE(findParams, sql, OPERATOR_LIKE);
		sql = ORDER(null, sql);
        return sql.toString();
	}

	public String get(T findParams) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		SQL sql = SELECT_FROM();
		sql = WHERE(findParams, sql, OPERATOR_EQUAL);
		sql = ORDER(null, sql);
		return sql.toString();
	}

	public String insert(final T t) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		initFromThreadLocal();
		// 设置默认值
		Date now = Calendar.getInstance().getTime();

		Class clazz = t.getClass();

	*//*	try {
			Method method = clazz.getMethod("setServerIp",String.class);
			if (method != null) {
				method.setAccessible(true);
				method.invoke(t,SERVER_IP);
			}
		} catch (Exception e) {
			logger.warn("=======>core update class:{},not found setServerIp ",clazz);
		}*//*


		SQL sql = new SQL() {
			{
				INSERT_INTO(tableName);

				Map<String, Property> properties = ModelUtils.getProperties(t, ColumnTarget.INSERT);
				for (Property property : properties.values()) {
					// 过滤不允许更新的字段
					if (property.isId() || property.isNullValue(t)) {
						continue;
					}
					VALUES(property.getColumnName(), "#{" + property.getName() + "}");
				}
			}
		};

		return sql.toString();
	}

	public String insertBatch(final Map<String,List<T>> param) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		initFromThreadLocal();
		// 设置默认值
		Date now = Calendar.getInstance().getTime();
		LoginUserDto user = SessionUtils.getUser();
		final List<T> list = param.get("list");
//		logger.debug("list info:{}",list);

		T cla = list.get(0);
		Class classType = cla.getClass();

		for(Object obj : list) {
//			logger.debug("obj info:{}",obj);
			T t = (T) obj;

			if (t.getCreateTime() == null) {
				t.setCreateTime(now);
				t.setUpdateTime(now);
			}

			if (user != null) {
				t.setCreateUser(user.getUserName());
				t.setUpdateUser(user.getUserName());

				if(StringUtils.isNotBlank(user.getLoginIp())) {
					try {
						Method method = classType.getMethod("setRemoteIp",String.class);
						if (method != null) {
							method.setAccessible(true);
							method.invoke(t,user.getLoginIp());
						}
					} catch (Exception e) {
						logger.warn("=======>core update class:{},not found setRemoteIp ", classType);
					}
				}

			} else {
				t.setCreateUser("system");
				t.setUpdateUser("system");
			}


			try {
				Method method = classType.getMethod("setServerIp",String.class);
				if (method != null) {
					method.setAccessible(true);
					method.invoke(t,SERVER_IP);
				}
			} catch (Exception e) {
				logger.warn("=======>core update class:{},not found setServerIp ", classType);
			}

		}

		final Map<String, Property> properties = ModelUtils.getProperties(cla, null);
		SQL sql = new SQL() {
			{
				INSERT_INTO(tableName);
				for (Property property : properties.values()) {
					VALUES(property.getColumnName(), "#{" + property.getName() + "}");
				}

			}
		};



		Method[] methods = classType.getMethods();


		Map<String,Method> methodMap = new HashMap<String,Method>();
		for(Method method :methods){
//			logger.debug("method name:{}",method.getName());
			methodMap.put(method.getName(),method);
		}
//		Field[] declaredFields=classType.getDeclaredFields();

		Map<String,Class> typeMap = new HashMap<String,Class>();
		Map<String,Field> fieldMap = new HashMap<String,Field>();
		for(Class<?> clazz = classType ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
			try {
				Field[] declaredFields=clazz.getDeclaredFields();
				for(Field field:declaredFields){
					if(!fieldMap.containsKey(field.getName())){
						fieldMap.put(field.getName(),field);
						typeMap.put(field.getName(),field.getType());
					}
				}
			} catch (Exception e) {
				//这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				//如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}
		String sqlStr = sql.toString();
		int index = StringUtils.lastIndexOf(sqlStr, "(");
		String prefixSql = StringUtils.substring(sqlStr,0,index);
		StringBuffer sqlBuffer = new StringBuffer(prefixSql);
		for(int i =0;i<list.size();i++) {
			T t = list.get(i);
			if(i==0){
				sqlBuffer.append("(");
			}else{
				sqlBuffer.append(",(");
			}



			Iterator<Property> iterator = properties.values().iterator();
			while (iterator.hasNext()) {
				Property property = iterator.next();

				String fieldName=property.getName();
				String methodName = null;
				if(fieldName.length() == 1) {
					methodName = fieldName.substring(0, 1).toUpperCase();
				} else {
					methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
				}
				methodName = "get" + methodName;
				Method method = methodMap.get(methodName);
				method.setAccessible(true);
				Object val = method.invoke(t);
				Class type =typeMap.get(fieldName);
//				logger.debug("========insertBatch field name:{},type:{},value:{}", fieldName, type, val);

				if (type.isEnum()) {
					sqlBuffer.append("'");
					if(val!=null) {
						Enum<?> e = (Enum<?>) val;
						sqlBuffer.append(e.name());
					}
					sqlBuffer.append("'");
				} else if (Date.class.equals(type)) {
					sqlBuffer.append("'");
					if(val!=null) {
						Date date = (Date) val;
						sqlBuffer.append(DateUtils.convert(date));
					}else{
						sqlBuffer.append("0000-00-00 00:00;00");
					}
					sqlBuffer.append("'");
				} else {
					sqlBuffer.append("'");
					if(val!=null) {
						sqlBuffer.append(val.toString());
					}
					sqlBuffer.append("'");
				}
				if(iterator.hasNext()){
					sqlBuffer.append(",");
				}
			}


			sqlBuffer.append(")");
		}
		return sqlBuffer.toString();
	}

	public String delete(Long id) {
		initFromThreadLocal();

		SQL sql = new SQL() {
			{
				DELETE_FROM(tableName);
				WHERE("ID = #{id}");
			}
		};
		return sql.toString();
	}

	public String deleteByPrimaryKey(final T t) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException{
		initFromThreadLocal();

		String sql = new SQL() {
			{
				boolean flg = false;
				DELETE_FROM(tableName);
				Map<String, Property> properties = ModelUtils.getProperties(t, null);
				for (Property property : properties.values()) {
					if(t.getClass().getDeclaredField(property.getName()).getAnnotation(javax.persistence.Id.class)!=null){
						WHERE(property.getColumnName() + " = #{" + property.getName() + "}");
						flg = true;
					}
				}
				if(!flg){
					throw new UnsupportedOperationException("please set jpa @Id to entity field");
				}
			}
		}.toString();
		logger.debug("deleteByPrimaryKey sql:{}",sql);
		//TODO
		return sql;
	}

	public String update(final T t) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		initFromThreadLocal();
		// 设置默认值
		t.setUpdateTime(Calendar.getInstance().getTime());
        LoginUserDto user = SessionUtils.getUser();


		Class clazz = t.getClass();
		if (user != null) {
			t.setUpdateUser(user.getUserName());

			if(StringUtils.isNotBlank(user.getLoginIp())) {
				try {
					Method method = clazz.getMethod("setRemoteIp",String.class);
					if (method != null) {
						method.setAccessible(true);
						method.invoke(t,user.getLoginIp());
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

		} else {
			t.setUpdateUser("system");
		}


		try {
			Method method = clazz.getMethod("setServerIp",String.class);
			if (method != null) {
				method.setAccessible(true);
				method.invoke(t,SERVER_IP);
			}
		} catch (Exception e) {
			logger.warn("=======>core update class:{},not found setServerIp ",clazz);
		}


		// 过滤不允许更新的字段
		t.setCreateTime(null);
		t.setCreateUser(null);
		SQL sql = new SQL() {
			{
				UPDATE(tableName);

				String className = StringUtils.split(modelClass.getName(), "$")[0];
				try {
					Map<String, Property> properties = ModelUtils.getProperties(Class.forName(className), ColumnTarget.UPDATE);

					for (Property property : properties.values()) {
						// 过滤不允许更新的字段
						if (property.isId() || property.isNullValue(t)||property.getName().equals("id")) {
							continue;
						}

						SET(property.getColumnName() + " = #{" + property.getName() + "}");
					}
				} catch (ClassNotFoundException e) {
					logger.warn(className+" not found!");
				}
				WHERE("ID = #{id}");
			}
		};
		return sql.toString();
	}

	private SQL SELECT_FROM() {
		final Map<String, Property> columns = ModelUtils.getProperties(modelClass, ColumnTarget.SELECT);
		return new SQL() {
			{
				for (Property property : columns.values()) {
					SELECT(property.getColumnName());
				}
				FROM(tableName);
			}
		};
	}

	private SQL WHERE(T findParams, SQL sql, String operator) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, Property> properties = ModelUtils.getProperties(findParams, ColumnTarget.WHERE);


		for (Property property : properties.values()) {
            if (operator.equalsIgnoreCase("LIKE")) {
            }
			sql.WHERE(property.getColumnName() + operator + "#{" + property.getName() + "}");
		}
		return sql;
	}

    private SQL WHERE_CUSTOM(Map<String, Object> dataMap, SQL sql) {
        Map<String, Property> properties = ModelUtils.getProperties(modelClass, null);
        List<CustomQueryParam> customQueryParams = (List<CustomQueryParam>) dataMap.get("queryParams");
        if (customQueryParams == null) {
            return sql;
        }
        dataMap.clear();
        int i = 0;
        for (CustomQueryParam customQueryParam : customQueryParams) {
            String key = customQueryParam.getProperty();
            Property property = properties.get(key);
            if (customQueryParam instanceof WithValueQueryParam) {
                WithValueQueryParam withValueQueryParam = (WithValueQueryParam) customQueryParam;
                dataMap.put(i + "", withValueQueryParam.getValue());
                sql.WHERE(property.getColumnName() + " " + withValueQueryParam.getOperator() + " #{" + i + "}");
                i++;
            } else if (customQueryParam instanceof NoValueQueryParam) {
                NoValueQueryParam noValueQueryParam = (NoValueQueryParam) customQueryParam;
                sql.WHERE(property.getColumnName() + " " + noValueQueryParam.getCondition());
            }
        }
        return sql;
    }

	private SQL ORDER(List<Sort> sortList, SQL sql) {
		Map<String, Property> properties = ModelUtils.getProperties(modelClass, ColumnTarget.ORDER);
		for (Property property : properties.values()) {
			sql.ORDER_BY(property.getOrder());
		}
        if (sortList != null) {
            for (Sort sort : sortList) {
                sql.ORDER_BY(sort.getProperty() + " " + sort.getDirection());
            }
        }
		return sql;
	}

    public String deleteAll() {
        initFromThreadLocal();
		SQL sql = new SQL() {
			{
				DELETE_FROM(tableName);
			}
		};
        return sql.toString();
    }*/
}
