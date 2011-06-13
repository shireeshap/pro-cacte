package gov.nih.nci.ctcae.core.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: allareddy
 * Date: 5/27/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class MeddraLoaderRepository extends JdbcDaoSupport {

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void batchExecute(final List<String[]> list, String sql, final boolean lltUpdate) throws Exception {

//                    System.out.println(getConnection().getAutoCommit());
//        List<String[]> updateTerms = list;

        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String[] updateTerms1 = list.get(i);
                if (lltUpdate) {
                    preparedStatement.setString(1, updateTerms1[0]);
                    preparedStatement.setString(2, updateTerms1[3]);
                    preparedStatement.setInt(3, Integer.parseInt(updateTerms1[2]));
                    preparedStatement.setString(4, updateTerms1[0]);
                    System.out.println(i);
                } else {
                    preparedStatement.setString(1, updateTerms1[1]);
                    preparedStatement.setString(2, updateTerms1[0]);
                }
//                             To change body of implemented methods use File | Settings | File Templates.
            }

            public int getBatchSize() {
                return list.size();  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

//          getJdbcTemplate().batchUpdate(list.toArray(new String[0]));
//        for (String query: list) {
//            System.out.println(query);
//            getJdbcTemplate().execute(query);
//        }
    }


}
