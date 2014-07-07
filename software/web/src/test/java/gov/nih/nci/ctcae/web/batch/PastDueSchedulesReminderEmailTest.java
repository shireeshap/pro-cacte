package gov.nih.nci.ctcae.web.batch;

import java.util.Date;
import java.util.List;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

/**PastDueSchedulesReminderEmailTest class.
 * @author Amey
 */
public class PastDueSchedulesReminderEmailTest extends AbstractWebIntegrationTestCase {
	PastDueSchedulesReminderEmail pastDueSchedulesReminderEmail;
	Study study;
	StudySite studySite;
	Date today;
	
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		pastDueSchedulesReminderEmail = new PastDueSchedulesReminderEmail();
		study = StudyTestHelper.getDefaultStudy();
		studySite = study.getStudySites().get(0);
		today = new Date();
	}
	
	public void testBuildClinicalStaffList(){
		List<StudyOrganizationClinicalStaff> clinicalStaffList = pastDueSchedulesReminderEmail.buildClinicalStaffList(studySite);
		
		StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
		query.filterByFirstNameOrLastNameOrNciIdentifier("CDAVIS");
		studyOrganizationClinicalStaffRepository.findSingle(query);
		assertTrue("SITE_CRA is not present", isStaffPresent(clinicalStaffList, studyOrganizationClinicalStaffRepository.findSingle(query)));
		
		query = new StudyOrganizationClinicalStaffQuery();
		query.filterByFirstNameOrLastNameOrNciIdentifier("HTODD");
		studyOrganizationClinicalStaffRepository.findSingle(query);
		assertTrue("SITE_CRA is not present", isStaffPresent(clinicalStaffList, studyOrganizationClinicalStaffRepository.findSingle(query)));
		
		query = new StudyOrganizationClinicalStaffQuery();
		query.filterByFirstNameOrLastNameOrNciIdentifier("LARCHER");
		studyOrganizationClinicalStaffRepository.findSingle(query);
		assertTrue("SITE_PI is not present", isStaffPresent(clinicalStaffList, studyOrganizationClinicalStaffRepository.findSingle(query)));
		
		query = new StudyOrganizationClinicalStaffQuery();
		query.filterByFirstNameOrLastNameOrNciIdentifier("LSIT");
		studyOrganizationClinicalStaffRepository.findSingle(query);
		assertTrue("LEAD_CRA is not present", isStaffPresent(clinicalStaffList, studyOrganizationClinicalStaffRepository.findSingle(query)));
		
		query = new StudyOrganizationClinicalStaffQuery();
		query.filterByFirstNameOrLastNameOrNciIdentifier("EBASCH");
		studyOrganizationClinicalStaffRepository.findSingle(query);
		assertTrue("LEAE_PI is not present", isStaffPresent(clinicalStaffList, studyOrganizationClinicalStaffRepository.findSingle(query)));
	}
	
	public void testUpdateScheduleStatusForPastDueSurveys(){
		StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
		studyParticipantCrfSchedule.setDueDate(DateUtils.addDaysToDate(today, -2));
		
		studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
		pastDueSchedulesReminderEmail.updateScheduleStatusForPastDueSurveys(studyParticipantCrfSchedule, today);
		assertEquals(CrfStatus.PASTDUE, studyParticipantCrfSchedule.getStatus());
		
		studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
		pastDueSchedulesReminderEmail.updateScheduleStatusForPastDueSurveys(studyParticipantCrfSchedule, today);
		assertEquals(CrfStatus.COMPLETED, studyParticipantCrfSchedule.getStatus());
	}
	
	public boolean isStaffPresent(List<StudyOrganizationClinicalStaff> list, StudyOrganizationClinicalStaff socs){
		for(StudyOrganizationClinicalStaff staff : list){
			if(staff.getOrganizationClinicalStaff().getClinicalStaff().getNciIdentifier().equals(socs.getOrganizationClinicalStaff().getClinicalStaff().getNciIdentifier())){
				return true;
			}
		}
		return false;
	}
}
