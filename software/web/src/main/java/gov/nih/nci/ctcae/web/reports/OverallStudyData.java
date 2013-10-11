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

                String sqlQuery = "SELECT s.short_title, o.name, c.title, p.first_name,\n" +
                        "p.last_name, scs.start_date, scs.due_date, pctv.term_english, pcq.question_type,\n" +
                        "pcvvv.value_english\n" +
                        "FROM\n" +
                        "studies s JOIN crfs c ON c.study_id = s.id\n" +
                        "JOIN study_participant_crfs spc ON spc.crf_id = c.id\n" +
                        "JOIN sp_crf_schedules scs ON scs.study_participant_crf_id = spc.id\n" +
                        "JOIN study_participant_crf_items spci ON spci.sp_crf_schedule_id = scs.id\n" +
                        "JOIN crf_page_items cpi ON cpi.id = spci.crf_item_id\n" +
                        "JOIN pro_ctc_questions pcq ON cpi.pro_ctc_question_id = pcq.id\n" +
                        "JOIN pro_ctc_terms pct ON pcq.pro_ctc_term_id = pct.id\n" +
                        "JOIN pro_ctc_terms_vocab pctv ON pct.id = pctv.pro_ctc_terms_id\n" +
                        "JOIN pro_ctc_valid_values pcvv ON spci.pro_ctc_valid_value_id = pcvv.id\n" +
                        "JOIN pro_ctc_valid_values_vocab pcvvv ON pcvv.id = pcvvv.pro_ctc_valid_values_id\n" +
                        "JOIN study_participant_assignments spa ON spc.study_participant_id = spa.id\n" +
                        "JOIN participants p ON p.id = spa.participant_id\n" +
                        "JOIN study_organizations ss ON ss.study_id = s.id\n" +
                        "JOIN organizations o ON ss.organization_id = o.id\n" +
                        "WHERE s.id =:par_id and c.status = 'RELEASED' and scs.status = 'COMPLETED'\n" +
                        "order by s.short_title, o.name, c.title, p.first_name, p.last_name, scs.start_date, scs.due_date, pctv.term_english, pcq.display_order";

                org.hibernate.SQLQuery nativeQuery = session.createSQLQuery(sqlQuery);
                nativeQuery.setParameter("par_id", sid);
                list.addAll(nativeQuery.list());
                return null;
            }
        });
        return list;
    }
}
