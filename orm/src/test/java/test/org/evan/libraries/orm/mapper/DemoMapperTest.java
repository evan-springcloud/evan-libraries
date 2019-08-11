package test.org.evan.libraries.orm.mapper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.org.evan.libraries.orm.support.MySQLTestCaseSupport;
import test.org.evan.libraries.orm.support.mapper.DemoMapper;
import test.org.evan.libraries.orm.support.model.Demo;
import test.org.evan.libraries.orm.support.model.DemoQuery;
import test.org.evan.libraries.orm.support.testdata.TestData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DemoMapperTest extends MySQLTestCaseSupport {

    @Autowired
    private DemoMapper demoMapper;

    @Test
    public void testLoad() {
        Demo demo = demoMapper.load(1L);
        LOGGER.info(demo + "");
    }

    @Test
    @Rollback(false)
    public void testInsert() {
        Demo demo = TestData.random();
        demoMapper.insert(demo);
        LOGGER.info(demo.getId() + "");

        demo = TestData.random();
        //demo.setFieldCity("12345677889");
        demoMapper.insert(demo);
        LOGGER.info(demo.toString());
    }

    @Test
    @Rollback(false)
    public void testUpdate() {
        Demo demo = new Demo(4823L);
        demo.setFieldText("BBB");
        demo.setFieldDatetime(new Date());
        demo.setFieldNumber(new BigDecimal("22121212.312121212"));
        demo.setFieldHtmleditor("aaa");

        demoMapper.update(demo);
    }

    @Test
    @Rollback(false)
    public void testUpdateStatus() {
        demoMapper.updateStatus(4823L, 2);
    }

    @Test
    public void testDelete() {
        demoMapper.delete(4823L);
    }

    @Test
    public void testQueryForList() {
        DemoQuery demoQuery = new DemoQuery();
//        demoQuery.setGmtCreateFrom(DateUtils.parse("2011-01-01"));
//        demoQuery.setGmtCreateTo(DateUtils.parse("2013-12-31"));
//        demoQuery.setStatusEnumArray(PublishStatusEnum.NO_PUBLISH, PublishStatusEnum.PUBLISHED);
//        demoQuery.setSort(DemoColumns.ID.getColumn());
//        demoQuery.setColumns(DemoColumns.ID.getColumn(),//
//                DemoColumns.FIELD_REGION.getColumn(),//
//                DemoColumns.GMT_CREATE.getColumn(),//
//                DemoColumns.GMT_MODIFY.getColumn()//
//        );
//        demoQuery.setJoinDemoChild1(true);
        List<Demo> demos = demoMapper.queryList(demoQuery);

        LOGGER.info(demos.size() + "");
    }

    @Test
    public void testQueryForCount() {
        DemoQuery demoQuery = new DemoQuery();

        demoQuery.setFieldText("1");
        //demoQuery.setPageSize(5);

        List<Demo> demos = demoMapper.queryList(demoQuery);

        LOGGER.info(demos.size() + "");
    }

    @Test
    public void testQueryForPage() {
        DemoQuery demoQuery = new DemoQuery();

        //demoQuery.setFieldText("1");
//        demoQuery.setPageNo(2);
//        demoQuery.setPageSize(4);
//        demoQuery.setJoinDemoChild1(true);

        int count = demoMapper.queryCount(demoQuery);
        List<Demo> demos = demoMapper.queryList(demoQuery);
        //PageResult<Demo> pageResult = PageResult.create(demoQuery, demos, count);

        //log.info(pageResult.toString());
    }
}
