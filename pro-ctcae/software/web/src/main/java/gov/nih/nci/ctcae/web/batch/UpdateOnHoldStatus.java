package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author AmeyS
 * 
 */
public class UpdateOnHoldStatus extends HibernateDaoSupport{
	
	public GenericRepository genericRepository;
	private static final String STATUS_ACTIVE = "ACTIVE";
	 
	@SuppressWarnings("deprecation")
	@Transactional
    public void updateStatus(){
	 DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
     DataAuditInfo.setLocal(auditInfo);
     Session session = getHibernateTemplate().getSessionFactory().openSession();
     Transaction tx = session.beginTransaction();
     tx.begin();
     logger.debug("UpdateOnHoldStatus: Nightly trigger bean job starts....");
	
     StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
     query.filterByStatus(STATUS_ACTIVE);
     List<StudyParticipantAssignment> studyParticipantAssignmentsList = genericRepository.find(query);
     Date today = new Date();
	 for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAssignmentsList) {
		 if (studyParticipantAssignment.getStatus() != null && studyParticipantAssignment.getStatus().equals(RoleStatus.ACTIVE)){
			 if (studyParticipantAssignment.getOnHoldTreatmentDate() != null){
				if (today.equals(studyParticipantAssignment.getOnHoldTreatmentDate())
						|| today.after(studyParticipantAssignment.getOnHoldTreatmentDate())) {
				  studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);
				  genericRepository.save(studyParticipantAssignment);
				}
		     }
		 }
	 }
     logger.debug("UpdateOnHoldStatus: Nightly trigger bean job ends....");
    }
	 
	public void setGenericRepository(GenericRepository genericRepository){
		this.genericRepository = genericRepository;
	}
}
