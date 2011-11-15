package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class SpCrfItemCreator extends HibernateDaoSupport {
    protected static final Log logger = LogFactory.getLog(SpCrfItemCreator.class);

    @Transactional
    public void createSpCrfItemsForAvailableSchedules() {

        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        logger.error("SpCrfItemCreator: Nightly trigger bean job starts....");
        
        List<StudyParticipantCrfSchedule> spCrfSchedules = (List<StudyParticipantCrfSchedule>)session.createQuery(
        		"select spCrfSchedule from StudyParticipantCrfSchedule as spCrfSchedule where spCrfSchedule.startDate <= ? and spCrfSchedule.dueDate >= ?")
        			.setDate(0, DateUtils.getCurrentDate()).setDate(1, DateUtils.getCurrentDate()).list();

        for (StudyParticipantCrfSchedule spCrfSchedule : spCrfSchedules) {
        	//the get creates the spCrfItems for you
        	spCrfSchedule.getStudyParticipantCrfItems();
        }
        
        tx.commit();
        logger.error("SpCrfItemCreator: Nightly trigger bean job ends....");
    }

}
