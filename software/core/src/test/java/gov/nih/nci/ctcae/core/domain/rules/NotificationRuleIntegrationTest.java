package gov.nih.nci.ctcae.core.domain.rules;

import java.util.List;
import java.util.Set;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Notification;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;

public class NotificationRuleIntegrationTest extends TestDataManager{
	NotificationsEvaluationService notificationsEvaluationService;
	NotificationRule notificationRule; 
	private static final String NOTIFICATION = "Notification";
	
	public void testNotifications() throws Exception{
	   
		Study study = StudyTestHelper.getDefaultStudy();
		StudyParticipantCrfSchedule spcrfs = getStudyParticipantCrfSchedule(study);
		notificationsEvaluationService = new NotificationsEvaluationService();
		notificationsEvaluationService.setGenericRepository(genericRepository);
		CRF crf = spcrfs.getStudyParticipantCrf().getCrf();
		
		CRFNotificationRule crfNotificationRule = new CRFNotificationRule();
		crfNotificationRule.setCrf(crf);
		crfNotificationRule.setDisplayOrder(1);
		notificationRule = createNotificationRule(crf);
		crfNotificationRule.setNotificationRule(notificationRule);
		
		crf.addCrfNotificationRule(crfNotificationRule);
	
		assertEquals(find(NOTIFICATION).size(),2);
		notificationsEvaluationService.executeRules(crf.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0), spcrfs.getStudyParticipantCrf().getCrf(), spcrfs.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());
		assertEquals(find(NOTIFICATION).size(),3);
	}

	
	protected NotificationRule getNotificaionRule(){
		NotificationRule notificationRule = new NotificationRule();
		notificationRule.setSiteOverRide(false);
		notificationRule.setTitle("NOTIFICATION");
		return notificationRule;
	}
	
	protected NotificationRule createNotificationRule(CRF crf){
		NotificationRule notificationRule = getNotificaionRule();
		NotificationRuleCondition notificationRuleCondition = new NotificationRuleCondition();
		notificationRuleCondition.setNotificationRule(notificationRule);
		ProCtcQuestion question = null;
		Set<ProCtcQuestionType> proCtcQuestionTypes = crf.getAllQuestionTypes();
		
		if(proCtcQuestionTypes.contains(ProCtcQuestionType.SEVERITY)){
			question = getQuestionOfType(crf, ProCtcQuestionType.SEVERITY);
		}else if(proCtcQuestionTypes.contains(ProCtcQuestionType.FREQUENCY)){
			question = getQuestionOfType(crf, ProCtcQuestionType.FREQUENCY);
		}
		
		if(question != null){
			notificationRuleCondition.setProCtcQuestionType(question.getProCtcQuestionType());
			notificationRuleCondition.setThreshold(0);
			notificationRuleCondition.setNotificationRuleOperator(NotificationRuleOperator.GREATER_EQUAL);
			notificationRule.addNotificationRuleCondition(notificationRuleCondition);
			
			NotificationRuleSymptom notificationRuleSymptom = new NotificationRuleSymptom();
			notificationRuleSymptom.setNotificationRule(notificationRule);
			notificationRuleSymptom.setProCtcTerm(question.getProCtcTerm());
			notificationRule.addNotificationRuleSymptom(notificationRuleSymptom);
		}
		
		
		return notificationRule;
	}
	
	protected ProCtcQuestion getQuestionOfType(CRF crf, ProCtcQuestionType proCtcQuestionType){
		
		StudyParticipantCrfSchedule spcrf = crf.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);
		
		for(StudyParticipantCrfItem spcrfItem : spcrf.getStudyParticipantCrfItems()){
			if(spcrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(proCtcQuestionType)){
				ProCtcValidValue value = (ProCtcValidValue) spcrfItem.getCrfPageItem().getProCtcQuestion().getValidValues().toArray()[1];
				spcrfItem.setValidValue(value);
				return spcrfItem.getCrfPageItem().getProCtcQuestion();
				
			}
		}
		return null;
	}
	
	
	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule(Study study){
		return study.getArms().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);
	}
	
	public List<Notification> find(String domainObject){
		return hibernateTemplate.find(" from "+ domainObject);
		
		
	}
}
