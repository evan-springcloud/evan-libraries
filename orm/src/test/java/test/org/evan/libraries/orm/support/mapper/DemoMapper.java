package test.org.evan.libraries.orm.support.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

import  test.org.evan.libraries.orm.support.model.Demo;
import test.org.evan.libraries.orm.support.model.DemoQuery;

public interface DemoMapper {
    /***/
    Demo load(Long id);

    /***/
    void insert(Demo demo);

    /***/
    void update(Demo demo);

    /***/
    void updateStatus(@Param("id") Long id, @Param("status") Serializable status);

    /***/
    void delete(Long id);

    /***/
    List<Demo> queryList(DemoQuery demoQuery);

    /***/
    int queryCount(DemoQuery demoQuery);
}
