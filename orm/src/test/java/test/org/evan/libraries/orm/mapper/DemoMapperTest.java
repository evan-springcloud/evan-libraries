package test.org.evan.libraries.orm.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.evan.libraries.model.result.PageResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.org.evan.libraries.orm.support.MySQLTestCaseSupport;
import test.org.evan.libraries.orm.support.mapper.DemoMapper;
import test.org.evan.libraries.orm.support.model.Demo;
import test.org.evan.libraries.orm.support.model.DemoQuery;
import test.org.evan.libraries.orm.support.testdata.TestData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoMapperTest extends MySQLTestCaseSupport {

    @Autowired
    private DemoMapper demoMapper;

    @Test
    public void testLoad() {
        Demo demo = demoMapper.selectById(1L);
        LOGGER.info(">>>> demoMapper.selectById(1)={}", demo);

        List idList = new ArrayList();
        idList.add(50);
        idList.add(60);
        List<Demo> demos = demoMapper.selectBatchIds(idList);

        LOGGER.info(">>>> demoMapper.selectBatchIds({})={}", idList, demos);
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

//        QueryWrapper<Demo> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("fieldName","a");
//        queryWrapper.
//
//        LOGGER.info(demos.size() + "");
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
        demoQuery.setPageNo(2);
        demoQuery.setPageSize(5);

        int count = demoMapper.queryCount(demoQuery);
        List<Demo> demos = demoMapper.queryList(demoQuery);

        PageResult<Demo> pageResult = PageResult.create(demoQuery, demos, count);

        LOGGER.info(pageResult.toString());


//        Page page = PageHelper.startPage(demoQuery.getPageNo(), demoQuery.getPageSize());
//        demos = demoMapper.queryList(demoQuery);
//        PageInfo<Demo> demoPage = new PageInfo<>(page.getResult());
//        pageResult = PageResult.create(demoQuery, demos, page.getTotal());
//
//        LOGGER.info(demoPage.toString());

//        demoQuery.setJoinDemoChild1(true);

//        Page page = new Page();
//        page.setCurrent(demoQuery.getPageNo());
//        page.setSize(demoQuery.getPageSize());
//
//        IPage<Demo> demoPage = demoMapper.queryList(page,demoQuery);
//        LOGGER.info("{},{},{}",demoPage.getPages(),demoPage.getTotal());


        //PageResult<Demo> pageResult = PageResult.create(demoQuery, demos, count);

        //log.info(pageResult.toString());


    }


}
