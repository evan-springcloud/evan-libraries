package test.org.evan.libraries.orm.support.mapper;

import org.apache.ibatis.annotations.Param;
import test.org.evan.libraries.orm.support.model.Demo;
import test.org.evan.libraries.orm.support.model.DemoQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface DemoMapper {
    /***/
    Demo selectById(Long id);

    List<Demo> selectBatchIds(Collection<Long> ids);

    /***/
    void insert(Demo demo);

    /***/
    void update(Demo demo);

    /***/
    void delete(Long id);

    /***/
    void updateStatus(@Param("id") Long id, @Param("status") Serializable status);

    /***/
    List<Demo> queryList(DemoQuery demoQuery);

    /***/
    int queryCount(DemoQuery demoQuery);
}
