package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**StudyParticipantCrfGradesCreator class
 * @author AmeyS
 * This trigger job is responsible for generating ctcae_grades for any newly submitted (non Eq5D) survey in the system.
 */
public class StudyParticipantCrfGradesCreator extends HibernateDaoSupport {
    protected static final Log logger = LogFactory.getLog(StudyParticipantCrfGradesCreator.class);
    private ProctcaeGradeMappingVersion proctcaeGradeMappingVersion;

	@Transactional
    public void createGradesForCompletedSchedules() {

        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        
        Query gradeMappingVersionQuery = session.createQuery(new String("SELECT pgmv FROM ProctcaeGradeMappingVersion pgmv " +
		" where pgmv.version = 'v1.0'"));
        proctcaeGradeMappingVersion = (ProctcaeGradeMappingVersion) gradeMappingVersionQuery.list().get(0);
        
        logger.error("StudyParticipantCrfGradeCreator: Nightly trigger bean job starts....");
        
        Query proctcaeGradeMappingQuery = session.createQuery(new String("SELECT count(*) FROM ProctcaeGradeMapping"));
        Long proctcaeGradeMappingCount = (Long) proctcaeGradeMappingQuery.list().get(0);
        
        if(proctcaeGradeMappingCount > 0){
        	
            List<StudyParticipantCrfSchedule> spCrfSchedules = (List<StudyParticipantCrfSchedule>)session.createQuery(
            		" SELECT spCrfSchedule FROM StudyParticipantCrfSchedule as spCrfSchedule " +
            		" left join spCrfSchedule.studyParticipantCrf as spcrf " +
            		" where spcrf.studyParticipantAssignment.status = 'ACTIVE' and spCrfSchedule.status = 'COMPLETED' ").list();
            
            try{
    	        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : spCrfSchedules) {
    	        	if(CrfStatus.COMPLETED.equals(studyParticipantCrfSchedule.getStatus())){
    	        		studyParticipantCrfSchedule.generateStudyParticipantCrfGrades(proctcaeGradeMappingVersion);
    	        	}
    	        }
            }catch (Exception e) {
    			logger.error("Error in nightly trigger for creating studyParticipantCrfGrades");
    		}finally{
    			tx.commit();
    		}
        }

        logger.error("StudyParticipantCrfGradeCreator: Nightly trigger bean job ends....");
    }

}