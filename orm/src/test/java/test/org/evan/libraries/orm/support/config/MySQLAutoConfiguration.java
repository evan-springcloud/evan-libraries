package test.org.evan.libraries.orm.support.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.evan.libraries.orm.mybaties.MybatisUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * Created on 2017/7/16.
 *
 * @author evan.shen
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {
        "test.org.evan.libraries.orm.support.mapper",
}) //mybatis mapper 所在package
public class MySQLAutoConfiguration implements TransactionManagementConfigurer {

    private static final String TYPE_ALIASES_PACKAGE =
            "test.org.evan.libraries.orm.support.model";

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        return MybatisUtil.getSqlSessionFactory(dataSource, TYPE_ALIASES_PACKAGE, "classpath*:mapper/*Mapper.xml");
    }


    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
