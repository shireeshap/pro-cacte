package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.query.AbstractQuery;
import gov.nih.nci.ctcae.core.query.NativeSQLQuery;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Jan 21, 2011
 * Time: 8:58:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProductionDataCorrector extends HibernateDaoSupport {

    public void correctDueDateOfScheduleInProdDb() {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int numberOfResults = 0;
                String sqlQuery = "select scs.id,(scs.start_date+cast(COALESCE(ccd.due_date_amount,'1') as int)) as new_due_date from sp_crf_schedules scs JOIN study_participant_crfs spc ON spc.id = scs.study_participant_crf_id  JOIN study_participant_assignments spa ON spa.id=spc.study_participant_id  JOIN crfs c ON c.id=spc.crf_id  JOIN form_arm_schedules fas ON c.id=fas.form_id  JOIN crf_cycle_definitions ccd ON fas.id = ccd.form_arm_schedules_id  where scs.baseline='false' and fas.arm_id=spa.arm_id  and scs.due_date <> (scs.start_date+cast(COALESCE(ccd.due_date_amount,'1') as int));";

                org.hibernate.SQLQuery nativeQuery = session.createSQLQuery(sqlQuery);
                nativeQuery.addScalar("id", Hibernate.INTEGER);
                nativeQuery.addScalar("new_due_date", Hibernate.DATE);
                List<Object[]> list = nativeQuery.list();
                for (Object[] obj : list) {
                        Query nativeUpdateQuery = session.createSQLQuery("update sp_crf_schedules set due_date =:par_due_date where id=:par_id");
                        nativeUpdateQuery.setParameter("par_due_date", (Date)obj[1]);
                        nativeUpdateQuery.setParameter("par_id", (Integer)obj[0]);
                        numberOfResults = nativeUpdateQuery.executeUpdate();
                        //System.out.println(numberOfResults); 

                }

                Query query = session.createSQLQuery("update sp_crf_schedules set status='SCHEDULED' where status='PASTDUE' and due_date>=now();");
                numberOfResults = query.executeUpdate();

//                Query queryUpdateEmail = session.createSQLQuery("update clinical_staffs set email_address = 'mg@demo.com';");
//                queryUpdateEmail.executeUpdate();
                //System.out.println(numberOfResults);


                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }


}
