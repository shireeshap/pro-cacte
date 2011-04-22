package gov.nih.nci.ctcae.web.reports;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mehul
 * Date: Apr 15, 2011
 */
public class OverallStudyData extends HibernateDaoSupport {

    public List getStudyData(Integer id) {
        final int sid = id;
        final List<Object[]> list = new ArrayList();
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String sqlQuery = "select s.short_title, o.name, c.title, p.first_name, p.last_name, scs.start_date, scs.due_date, pct.term, pcq.question_type, pcvv.value\n" +
                        "from studies s, study_participant_crfs spc, crfs c, sp_crf_schedules scs, study_participant_crf_items spci, crf_page_items cpi, crf_pages cp, pro_ctc_terms pct, pro_ctc_questions pcq, pro_ctc_valid_values pcvv, study_participant_assignments spa, participants p,\n" +
                        "study_organizations ss    , organizations o   \n" +
                        "where spc.crf_id = c.id and c.study_id = s.id and s.id =:par_id and c.status = 'RELEASED'     \n" +
                        "and scs.study_participant_crf_id = spc.id and scs.status = 'COMPLETED' and spci.sp_crf_schedule_id = scs.id\n" +
                        "and cpi.id = spci.crf_item_id and cpi.crf_page_id = cp.id and cp.pro_ctc_term_id = pct.id and cpi.pro_ctc_question_id = pcq.id\n" +
                        "and spci.pro_ctc_valid_value_id = pcvv.id and spc.study_participant_id = spa.id and p.id = spa.participant_id and\n" +
                        "spa.study_site_id = ss.id and ss.study_id = s.id and ss.organization_id = o.id\n" +
                        "order by s.short_title, o.name,  c.title, p.first_name, p.last_name, scs.start_date, scs.due_date, pct.term, pcq.display_order";
                org.hibernate.SQLQuery nativeQuery = session.createSQLQuery(sqlQuery);
                nativeQuery.setParameter("par_id", sid);
                list.addAll(nativeQuery.list());
                return null;
            }
        });
        return list;
    }
}
