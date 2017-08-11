package com.tasfe.framework.crud.mysql;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by Lait on 2017/7/7.
 */
@Component
public class SqlSessionFactoryProcessor implements BeanFactoryPostProcessor {
    //private final static String MAPPER_LOCATION_PATH = "com/tasfe/framework/crud/mysql/impls/mybatis/mapper/CrudMapper.xml";

    private final static String MAPPER_LOCATION_PATH =
            "META-INF/mybatis/mappers/InsertMapper.xml," +
                    "META-INF/mybatis/mappers/SelectMapper.xml," +
                    "META-INF/mybatis/mappers/UpdateMapper.xml," +
                    "META-INF/mybatis/mappers/DeleteMapper.xml," +
                    "META-INF/mybatis/mappers/FunctionMapper.xml";

    private final static String INSERT_MAPPER = "META-INF/mybatis/mappers/InsertMapper.xml";
    private final static String UPDATE_MAPPER = "META-INF/mybatis/mappers/UpdateMapper.xml";
    private final static String SELECT_MAPPER = "META-INF/mybatis/mappers/SelectMapper.xml";
    private final static String DELETE_MAPPER = "META-INF/mybatis/mappers/DeleteMapper.xml";
    private final static String FUNCTION_MAPPER = "META-INF/mybatis/mappers/FunctionMapper.xml";

    private Resource resources[] = new Resource[6];

    {
        resources[0] = new ClassPathResource(INSERT_MAPPER);
        resources[1] = new ClassPathResource(UPDATE_MAPPER);
        resources[2] = new ClassPathResource(SELECT_MAPPER);
        resources[3] = new ClassPathResource(DELETE_MAPPER);
        resources[4] = new ClassPathResource(FUNCTION_MAPPER);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        /**
         * 在原有的mapper.xml集合中增加CrudMapper.xml文件
         */

        BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition("sqlSessionFactory");
        MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();


        //SqlSessionFactoryBean sqlSessionFactoryBean = configurableListableBeanFactory.getBean(SqlSessionFactoryBean.class);
        try {
            //Field field = FieldReflectUtil.findField(SqlSessionFactoryBean.class, "mapperLocations");

            /*Resource mapperLocations[] = (Resource[]) FieldReflectUtil.getFieldValue(sqlSessionFactoryBean,field);
            if(mapperLocations != null){
                List<Resource> mpList =  Arrays.asList(mapperLocations);
                mpList.add(resource);
                resources = (Resource[]) mpList.toArray();
                //resources = new Resource[mapperLocations.length+1];
                //System.arraycopy(mapperLocations, 0, resources, 1, mapperLocations.length);
            }*/
            //mapperLocations = resources;
            /**
             * 重新设值
             */
            String proName = "mapperLocations";
            //Resource resource = new ClassPathResource(MAPPER_LOCATION_PATH);
            if (mutablePropertyValues.contains(proName)) {
                /**
                 * 强转
                 */
                TypedStringValue typedStringValue = (TypedStringValue) mutablePropertyValues.getPropertyValue(proName).getValue();
                String mapperLocations = typedStringValue.getValue();
                //typedStringValue.setValue(mapperLocations + "," + MAPPER_LOCATION_PATH);

                /*resources = new Resource[];
                System.arraycopy(res, 0, resources, 1, res.length);
                resources[0] = resource;*/
                /*Resource resources[] = new Resource[2];
                resources[0] = resource;*/
                resources[5] = new ClassPathResource(mapperLocations);
                mutablePropertyValues.addPropertyValue(proName, resources);
            } else {
                Resource resources[] = this.resources;
                mutablePropertyValues.add(proName, resources);
            }
            //sqlSessionFactoryBean.setMapperLocations(resources);
        } catch (Exception e) {
            throw new BeanInitializationException(e.getMessage());
        }
    }
}
