package gov.nih.nci.ctcae.core.repository;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: allareddy
 * Date: 5/27/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class MeddraLoaderRepository extends JdbcDaoSupport {

    @Transactional
    public void batchExecute(List<String> list){

          getJdbcTemplate().batchUpdate(list.toArray(new String[0]));
    }


}
