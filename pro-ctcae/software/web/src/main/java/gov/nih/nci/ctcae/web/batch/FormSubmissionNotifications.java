package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleNotification;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleNotificationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Query;
import java.util.Date;
import java.util.List;

/**
 * The Class FormSubmissionNotifications.
 *
 * @author Suneel Allareddy
 * @since Jan 4, 2011
 */
public class FormSubmissionNotifications extends HibernateDaoSupport{

    private GenericRepository genericRepository;
    protected static final Log logger = LogFactory.getLog(FormSubmissionNotifications.class);
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /**
     * sendFormSubmitNotifications method is works only for IVRS form submit forms for which notifications not generated till date.
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void sendFormSubmissionNotificationsForIVRS() {
        logger.info("FormSubmissionNotifications Job starts....");
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        StudyParticipantCrfScheduleNotificationQuery query = new StudyParticipantCrfScheduleNotificationQuery();
        query.filterByStatus(CrfStatus.SCHEDULED);
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        Query querySession = session.createQuery(query.getQueryString());
        querySession.setParameter("status", CrfStatus.SCHEDULED);
        //List<StudyParticipantCrfScheduleNotification> schedNotifications = genericRepository.find(query);
        List<StudyParticipantCrfScheduleNotification> schedNotifications = querySession.list();
        logger.info("IVRS notifications size::"+schedNotifications.size());
        NotificationsEvaluationService notificationsEvaluationService = new NotificationsEvaluationService();
        notificationsEvaluationService.setGenericRepository(genericRepository);
        try {
            for (StudyParticipantCrfScheduleNotification studyParticipantCrfSchNotification : schedNotifications) {
                logger.info("StudyParticipantCrfScheduleNotification::id"+studyParticipantCrfSchNotification.getId());
                StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfSchNotification.getStudyParticipantCrfSchedule();
                lazyInitializeSchedule(studyParticipantCrfSchedule);
                boolean mailSent = notificationsEvaluationService.executeRules(studyParticipantCrfSchedule, studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf(), studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());
                studyParticipantCrfSchNotification.setStatus(CrfStatus.COMPLETED);
                studyParticipantCrfSchNotification.setMailSent(mailSent);
                studyParticipantCrfSchNotification.setCompletionDate(new Date());
                //genericRepository.save(studyParticipantCrfSchNotification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
        logger.info("FormSubmissionNotifications Job end....");
    }

    public void lazyInitializeSchedule(StudyParticipantCrfSchedule schedule) {        
        schedule.getStudyParticipantCrfItems();
        schedule.getStudyParticipantCrfScheduleAddedQuestions();
        schedule.getStudyParticipantCrf();
        schedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions();
    }


}