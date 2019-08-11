package org.evan.libraries.orm.mybaties;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Mybatis 配置辅助类
 * @author shenwei
 */
public class MybatisUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(MybatisUtil.class);

    public static SqlSessionFactory getSqlSessionFactory(
            DataSource dataSource,
            String typeAliasesPackage,
            String... mapperLocations

    ) {
        Assert.hasText(typeAliasesPackage, "typeAliasesPackage is not empty");
        Assert.notNull(dataSource, "dataSource is not null");
        Assert.notEmpty(mapperLocations, "mapperLocations can not empty");

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setVfs(SpringBootVFS.class);

        Properties properties = new Properties();
        //properties.put("Oracle", "oracle");
        properties.put("MySQL", "mysql");
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        databaseIdProvider.setProperties(properties);

        bean.setDatabaseIdProvider(databaseIdProvider);

//        //分页插件
//        PageHelper pageHelper = new PageHelper();
//        Properties props = new Properties();
//        props.setProperty("reasonable", "true");
//        props.setProperty("supportMethodsArguments", "true");
//        props.setProperty("returnPageInfo", "check");
//        // props.setProperty("params", "count=countSql");
//        pageHelper.setProperties(props);
//        //添加插件
//        bean.setPlugins(new Interceptor[]{pageHelper});

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<Resource>();
        List<String> mappersName = new ArrayList<String>();
        try {
            for (String e : mapperLocations) {
                Resource[] ss = resolver.getResources(e);
                for (Resource o : ss) {
                    mappersName.add(o.getFilename());
                    resources.add(o);
                }
            }
            LOGGER.info("Inited SqlSessionFactory,typeAliasesPackage:[{}],mapperLocations:{},Mappers:{}", typeAliasesPackage, mapperLocations, mappersName);
            bean.setMapperLocations(resources.toArray(new Resource[]{}));
            bean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
            return bean.getObject();
        } catch (Exception e) {
            throw new IllegalStateException("SqlSessionFactory init fail," + e.getMessage(), e);
        }
    }
}
