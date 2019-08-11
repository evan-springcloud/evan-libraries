package test.org.evan.libraries.orm.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import test.org.evan.libraries.orm.support.MySQLTestCaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTempleteTest extends MySQLTestCaseSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testInsert() {
        final String sql = "insert into demo(FIELD_TEXT)values(?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, "我的");
                return ps;
            }
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        //		/hibernateTemplate.load(entityClass, id)

        LOGGER.info("testInsert, pk is [{}]", id);

    }
}
