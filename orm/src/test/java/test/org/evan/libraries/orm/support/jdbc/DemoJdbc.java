package test.org.evan.libraries.orm.support.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
@Repository
public class DemoJdbc {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkFieldText(Long id, String fieldText) {

        List<Object> list = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(128);
        sql.append("select count(1) from demo where field_text=?");
        list.add(fieldText);
        if (id != null) {//修改的时候判断
            sql.append(" and id<>?");
            list.add(id);
        }
        long count = jdbcTemplate.queryForObject(sql.toString(), list.toArray(), Long.class);
        return count == 0;
    }
}