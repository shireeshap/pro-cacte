package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.exception.RuleSetNotFoundException;
import com.semanticbits.rules.impl.RuleEvaluationResult;
import com.semanticbits.rules.objectgraph.FactResolver;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class NotificationsEvaluationService {

    private static JavaMailSender javaMailSender;
    protected static final Log logger = LogFactory.getLog(NotificationsEvaluationService.class);
    private static GenericRepository genericRepository;


    public static void executeRules(StudyParticipantCrfSchedule studyParticipantCrfSchedule, CRF crf, StudyOrganization studySite) {
        ArrayList<String[]> criticalSymptoms = new ArrayList<String[]>();
        HashSet<String> emails = new HashSet<String>();
        RuleSet ruleSet = ProCtcAERulesService.getExistingRuleSetForCrfAndSite(crf, studySite);
        if (ruleSet == null) {
            return;
        }
        TreeMap<Integer, ArrayList<StudyParticipantCrfItem>> distinctPageNumbers = new TreeMap<Integer, ArrayList<StudyParticipantCrfItem>>();
        Notification notification = new Notification();

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            Integer myPageNumber = studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber();
            ArrayList<StudyParticipantCrfItem> myItems;
            myItems = distinctPageNumbers.get(myPageNumber);
            if (myItems == null) {
                myItems = new ArrayList<StudyParticipantCrfItem>();
                distinctPageNumbers.put(myPageNumber, myItems);
            }
            myItems.add(studyParticipantCrfItem);
        }

        for (Integer pageNumber : distinctPageNumbers.keySet()) {
            List<StudyParticipantCrfItem> studyParticipantCrfItems = distinctPageNumbers.get(pageNumber);

            if (studyParticipantCrfItems != null && studyParticipantCrfItems.size() > 0) {
                List<Object> inputObjects = new ArrayList<Object>();

                ArrayList<String[]> temp = new ArrayList<String[]>();
                for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {
                    if (studyParticipantCrfItem.getProCtcValidValue() != null) {
                        String[] tempArr = new String[2];
                        inputObjects.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm());
                        tempArr[0] = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getTerm();
                        inputObjects.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType());
                        tempArr[1] = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().getDisplayName();
                        inputObjects.add(studyParticipantCrfItem.getProCtcValidValue());
                        temp.add(tempArr);
                    }
                }

                FactResolver f = new FactResolver();
                inputObjects.add(f);
                ProCtcAEFactResolver proCtcAEFactResolver = new ProCtcAEFactResolver();
                inputObjects.add(proCtcAEFactResolver);
                inputObjects.add(crf);

                try {
                    List<Object> outputObjects = ProCtcAERulesService.fireRules(inputObjects, ruleSet.getName());

                    for (Object o : outputObjects) {
                        if (o instanceof RuleEvaluationResult) {
                            RuleEvaluationResult result = (RuleEvaluationResult) o;
                            if (!StringUtils.isBlank(result.getMessage())) {
                                String recipients = result.getMessage();
                                logger.info("Send email to " + recipients);
                                getRecipients(notification, emails, recipients, studyParticipantCrfSchedule);
                                criticalSymptoms.addAll(temp);
                            }
                        }
                    }
                } catch (RuleSetNotFoundException e) {
                    logger.error("RuleSet not found - " + e.getMessage());
                    return;
                }
            }
        }

        if (criticalSymptoms.size() > 0) {
            try {
                String emailMessage = getEmaiContent(studyParticipantCrfSchedule, criticalSymptoms);
                notification.setText(emailMessage);
                notification.setDate(new Date());
                genericRepository.save(notification);
                
                sendMail(getStringArr(emails), "Notification email", emailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void addEmail(String email, HashSet<String> emails) {
        if (!StringUtils.isBlank(email)) {
            emails.add(email);
        }
    }

    private static void getRecipients(Notification notification, HashSet<String> emails, String message, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {

        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();
        StringTokenizer st = new StringTokenizer(message, "||");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("PrimaryNurse")) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getResearchNurse();
                if (studyParticipantClinicalStaff.isNotify()) {
                    addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                    addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff);
                }
            }
            if (token.equals("PrimaryPhysician")) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getTreatingPhysician();
                if (studyParticipantClinicalStaff.isNotify()) {
                    addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                    addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff);
                }
            }
            if (token.equals("SiteCRA")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSiteCRAs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                        addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff);
                    }
                }
            }
            if (token.equals("SitePI")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSitePIs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                        addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff);
                    }
                }
            }
            if (token.equals("LeadCRA")) {
                addEmail(studyParticipantAssignment.getStudySite().getStudy().getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantAssignment.getStudySite().getStudy().getLeadCRA().getOrganizationClinicalStaff());
            }
            if (token.equals("PI")) {
                addEmail(studyParticipantAssignment.getStudySite().getStudy().getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantAssignment.getStudySite().getStudy().getPrincipalInvestigator().getOrganizationClinicalStaff());
            }
        }

        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
            if (studyParticipantClinicalStaff.isNotify()) {
                addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff);
            }
        }
        logger.info(emails);

    }

    private static void addUserNotification(Notification notification, StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyParticipantClinicalStaff studyParticipantClinicalStaff) {
        addUserNotification(notification, studyParticipantCrfSchedule, studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getUser());
    }

    private static void addUserNotification(Notification notification, StudyParticipantCrfSchedule studyParticipantCrfSchedule, OrganizationClinicalStaff organizationClinicalStaff) {
        addUserNotification(notification, studyParticipantCrfSchedule, organizationClinicalStaff.getClinicalStaff().getUser());
    }

    private static void addUserNotification(Notification notification, StudyParticipantCrfSchedule studyParticipantCrfSchedule, User user) {
        UserNotification userNotification = new UserNotification();
        userNotification.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        userNotification.setUuid(UUID.randomUUID().toString());
        userNotification.setMarkDelete(false);
        userNotification.setNew(true);
        userNotification.setParticipant(studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant());
        userNotification.setStudy(studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy());
        userNotification.setUser(user);
        notification.addUserNotification(userNotification);
    }


    private static HashMap<String, String> getMapForSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (StudyParticipantCrfItem i : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            ProCtcQuestion q = i.getCrfPageItem().getProCtcQuestion();
            if (i.getProCtcValidValue() == null) {
                map.put(q.getProCtcTerm().getTerm() + "~" + q.getProCtcQuestionType().getDisplayName(), "");
            } else {
                map.put(q.getProCtcTerm().getTerm() + "~" + q.getProCtcQuestionType().getDisplayName(), i.getProCtcValidValue().getValue());
            }
        }
        return map;
    }

    private static void addRow(StringBuilder content, String boldText, String nonBoldText) {
        content.append("<tr><td><b>" + boldText + "</b>" + nonBoldText + "</td></tr>");
    }

    private static String getEmaiContent(StudyParticipantCrfSchedule studyParticipantCrfSchedule, ArrayList<String[]> criticalSymptoms) {

        StudyParticipantCrfSchedule currentSchedule = studyParticipantCrfSchedule;
        StudyParticipantCrfSchedule previousSchedule = null;
        StudyParticipantCrfSchedule firstSchedule = null;
        HashMap<String, String> currentScheduleMap = getMapForSchedule(currentSchedule);
        HashMap<String, String> previousScheduleMap = null;
        HashMap<String, String> firstScheduleMap = null;

        List<StudyParticipantCrfSchedule> allSchedules = studyParticipantCrfSchedule.getStudyParticipantCrf().getCompletedCrfs();
        if (allSchedules.size() == 1) {
            previousSchedule = allSchedules.get(0);
            previousScheduleMap = getMapForSchedule(previousSchedule);
        }
        if (allSchedules.size() > 1) {
            firstSchedule = allSchedules.get(0);
            firstScheduleMap = getMapForSchedule(firstSchedule);

            previousSchedule = allSchedules.get(allSchedules.size() - 1);
            previousScheduleMap = getMapForSchedule(previousSchedule);
        }

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><head></head><body>");
        emailContent.append("<table>");

        addRow(emailContent, "", "This is an auto-generated email from PRO-CTCAE system.");
        Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
        addRow(emailContent, "Participant name: ", participant.getDisplayName() + "[" + participant.getAssignedIdentifier() + "]");
        addRow(emailContent, "Participant email: ", (participant.getEmailAddress() == null ? "Not specified" : participant.getEmailAddress()));
        addRow(emailContent, "Participant contact phone: ", (participant.getPhoneNumber() == null ? "Not specified" : participant.getPhoneNumber()));
        addRow(emailContent, "Study site: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getDisplayName());
        addRow(emailContent, "Study: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getStudy().getShortTitle() + "[" + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getStudy().getAssignedIdentifier() + "]");
        addRow(emailContent, "Research nurse: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getResearchNurse().getStudyOrganizationClinicalStaff().getDisplayName());
        addRow(emailContent, "Treating physician: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getTreatingPhysician().getStudyOrganizationClinicalStaff().getDisplayName());
        emailContent.append("</table>");
        emailContent.append("<br>This notification was triggered by following responses: <br><br>");

        emailContent.append("<table border=\"1\">");
        emailContent.append("<tr>");
        emailContent.append("<td><b>Symptom</b></td>");
        emailContent.append("<td><b>Attribute</b></td>");
        if (firstSchedule != null) {
            emailContent.append("<td><b>First visit (" + DateUtils.format(firstSchedule.getStartDate()) + ")</b></td>");
        }
        if (previousSchedule != null) {
            emailContent.append("<td><b>Previous visit (" + DateUtils.format(previousSchedule.getStartDate()) + ")</b></td>");
        }
        emailContent.append("<td><b>Current visit (" + DateUtils.format(currentSchedule.getStartDate()) + ")</b></td>");
        emailContent.append("</tr>");

        for (String[] symptom : criticalSymptoms) {
            emailContent.append("<tr>");
            String strSymptom = symptom[0];
            String strAttr = symptom[1];
            String key = strSymptom + "~" + strAttr;
            emailContent.append("<td>" + strSymptom + "</td>");
            emailContent.append("<td>" + strAttr + "</td>");
            if (firstSchedule != null) {
                emailContent.append("<td>" + firstScheduleMap.get(key) + "</td>");
            }
            if (previousSchedule != null) {
                emailContent.append("<td>" + previousScheduleMap.get(key) + "</td>");
            }
            emailContent.append("<td>" + currentScheduleMap.get(key) + "</td>");
            emailContent.append("</tr>");
        }
        emailContent.append("</table>");
        emailContent.append("</body></html>");
        return emailContent.toString();
    }

    private static String[] getStringArr(HashSet<String> emails) {
        String[] ret = new String[emails.size()];
        Iterator<String> it = emails.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = it.next();
            i++;
        }
        return ret;
    }

    public static void sendMail(String[] to, String subject, String content) throws Exception {
        try {
            if (javaMailSender == null) {
                javaMailSender = new JavaMailSender();
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(content, javaMailSender.isHtml());
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new Exception(" Error in sending email , please check the confiuration ", e);
        }
    }

    public static void setGenericRepository(GenericRepository inGenericRepository) {
        genericRepository = inGenericRepository;
    }
}