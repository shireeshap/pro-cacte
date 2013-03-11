package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class UpdateOnHoldStatus extends HibernateDaoSupport{
	
	public GenericRepository genericRepository;
	
	 @Transactional
	    public void updateStatus(){
		 
		 DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
	     DataAuditInfo.setLocal(auditInfo);
	     Session session = getHibernateTemplate().getSessionFactory().openSession();
	     Transaction tx = session.beginTransaction();
	     tx.begin();
	     logger.debug("Nightly trigger bean job starts....");
		
	     Query query = session.createQuery(new String("Select study from Study study"));
	     List<Study> studies = query.list();
	     Date today = new Date();
	     
	     for (Study study : studies) {
	    	 for (StudySite studySite : study.getStudySites()) {
	    		   for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
					if (studyParticipantAssignment.getStatus() != null && studyParticipantAssignment.getStatus().equals(RoleStatus.ACTIVE))
							if(studyParticipantAssignment.getOnHoldTreatmentDate() != null){
								if(today.equals(studyParticipantAssignment.getOnHoldTreatmentDate()) || today.after(studyParticipantAssignment.getOnHoldTreatmentDate())){
								  studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);
								  genericRepository.save(studyParticipantAssignment);
								}
							
	    			   }
	    		   }
	    	 }
	     }
	     
	    }
	 
	 public void setGenericRepository(GenericRepository genericRepository){
		 this.genericRepository = genericRepository;
	 }
}
