package gov.nih.nci.ctcae.web.batch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleNotification;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRule;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleNotificationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**FormSubmissionNotificationsTest class.
 * @author Amey
 */
public class FormSubmissionNotificationsTest extends WebTestCase {
	StudyParticipantCrfSchedule studyParticipantCrfSchedule;
	FormSubmissionNotifications formSubmissionNotifications = new FormSubmissionNotifications();
	GenericRepository genericRepository;
	List<Persistable> spCrfNotifications;
	NotificationsEvaluationService notificationsEvaluationService;
	StudyParticipantCrf studyParticipantCrf;
	CRF crf;
	StudyParticipantAssignment studyParticipantAssignment;
	StudySite studySite;
	List<NotificationRule> notificationRules;
	boolean mailSent = true;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		studyParticipantCrfSchedule = registerMockFor(StudyParticipantCrfSchedule.class);
		spCrfNotifications = new ArrayList<Persistable>();
		createTestStudyParticipantCrfNotifications();
		genericRepository = registerMockFor(GenericRepository.class);
		notificationsEvaluationService = registerMockFor(NotificationsEvaluationService.class);
		formSubmissionNotifications.setGenericRepository(genericRepository);
		studyParticipantCrf = registerMockFor(StudyParticipantCrf.class);
		crf = registerMockFor(CRF.class);
		studyParticipantAssignment = registerMockFor(StudyParticipantAssignment.class);
		studySite = registerMockFor(StudySite.class);
		notificationRules = new ArrayList<NotificationRule>();
		notificationRules.add(new NotificationRule());
	}
	
	public void testSendFormSubmissionNotificationsForIVRS(){
		try {
			expect(genericRepository.find(isA(StudyParticipantCrfScheduleNotificationQuery.class))).andReturn(spCrfNotifications).anyTimes();
			expect(studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()).andReturn(null).anyTimes();
			expect(studyParticipantCrfSchedule.getStudyParticipantCrfItems()).andReturn(new ArrayList<StudyParticipantCrfItem>()).anyTimes();
			expect(studyParticipantCrfSchedule.getStudyParticipantCrf()).andReturn(studyParticipantCrf).anyTimes();
			expect(studyParticipantCrf.getStudyParticipantCrfAddedQuestions()).andReturn(null).anyTimes();
			expect(studyParticipantCrf.getCrf()).andReturn(crf).anyTimes();
			expect(studyParticipantCrf.getStudyParticipantAssignment()).andReturn(studyParticipantAssignment).anyTimes();
			expect(studyParticipantAssignment.getStudySite()).andReturn(studySite).anyTimes();
			expect(notificationsEvaluationService.executeRules(studyParticipantCrfSchedule, crf, studySite)).andReturn(true).anyTimes();
			expect(studySite.getNotificationRules()).andReturn(notificationRules).anyTimes();
			expect(genericRepository.save(isA(StudyParticipantCrfScheduleNotification.class))).andReturn(new StudyParticipantCrfScheduleNotification());
			replayMocks();

			StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification = (StudyParticipantCrfScheduleNotification) spCrfNotifications.get(0);
			assertEquals(studyParticipantCrfScheduleNotification.getStatus(), CrfStatus.SCHEDULED);
			assertEquals(studyParticipantCrfScheduleNotification.getCompletionDate(), null);
			
			formSubmissionNotifications.sendFormSubmissionNotificationsForIVRS();
			
			assertEquals(studyParticipantCrfScheduleNotification.getStatus(), CrfStatus.COMPLETED);
			assertTrue(DateUtils.compareDate(studyParticipantCrfScheduleNotification.getCompletionDate(), new Date()) == 0);
			verifyMocks();
			
			
		} catch (Exception e) {
			fail("testSendFormSubmissionNotificationsForIVRS() method failed.");
		}
		
	}
	
	
		
	private void createTestStudyParticipantCrfNotifications(){
		StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
		studyParticipantCrfScheduleNotification.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
		spCrfNotifications.add(studyParticipantCrfScheduleNotification);
	}
}
