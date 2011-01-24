package gov.nih.nci.ctcae.web;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Jan 21, 2011
 * Time: 8:58:05 PM
 * To change this template use File | Settings | File Templates.
 */

    public class ProductionDataCorrector extends HibernateDaoSupport {
        
        public void correctDueDateOfScheduleInProdDb(){
            getHibernateTemplate().execute(new HibernateCallback(){
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    int numberOfResults = 0;

                    Query query = session.createSQLQuery("update sp_crf_schedules scs1 set due_date = (select (scs.start_date+cast(COALESCE(ccd.due_date_amount,'1') as int)) from sp_crf_schedules scs JOIN study_participant_crfs spc ON spc.id = scs.study_participant_crf_id JOIN study_participant_assignments spa ON spa.id=spc.study_participant_id JOIN crfs c ON c.id=spc.crf_id JOIN form_arm_schedules fas ON c.id=fas.form_id JOIN crf_cycle_definitions ccd ON fas.id = ccd.form_arm_schedules_id where scs1.id= scs.id and scs.baseline='false' and fas.arm_id=spa.arm_id and scs.due_date <> (scs.start_date+cast(COALESCE(ccd.due_date_amount,'1') as int)) ) where scs1.id in ( select scs.id from sp_crf_schedules scs JOIN study_participant_crfs spc ON spc.id = scs.study_participant_crf_id JOIN study_participant_assignments spa ON spa.id=spc.study_participant_id JOIN crfs c ON c.id=spc.crf_id JOIN form_arm_schedules fas ON c.id=fas.form_id JOIN crf_cycle_definitions ccd ON fas.id = ccd.form_arm_schedules_id where scs.baseline='false' and fas.arm_id=spa.arm_id and scs.due_date <> (scs.start_date+cast(COALESCE(ccd.due_date_amount,'1') as int)) );");
                     numberOfResults = query.executeUpdate();
                    //System.out.println(numberOfResults);
                    query = session.createSQLQuery("update sp_crf_schedules scs set status='SCHEDULED' where status='PASTDUE' and due_date>=now();");
                    numberOfResults = query.executeUpdate();
                    //System.out.println(numberOfResults);
                    //session.flush();
                    
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        }



}
