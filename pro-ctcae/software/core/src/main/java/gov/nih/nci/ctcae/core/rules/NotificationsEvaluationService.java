package gov.nih.nci.ctcae.core.rules;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.rules.*;
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
    private GenericRepository genericRepository;

    public boolean executeRules(StudyParticipantCrfSchedule studyParticipantCrfSchedule, CRF crf, StudyOrganization studySite) throws Exception {
        List<NotificationRule> notificationRules = studySite.getNotificationRules();
        if (notificationRules.size() == 0) {
            notificationRules = crf.getNotificationRules();
        }
        ArrayList<String[]> criticalSymptoms = new ArrayList<String[]>();
        HashSet<String> emails = new HashSet<String>();
        HashSet<User> users = new HashSet<User>();
        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            for (NotificationRule notificationRule : notificationRules) {
                if (responseSatisfiesRule(studyParticipantCrfItem, notificationRule)) {
                    getRecipients(users, emails, notificationRule.getNotificationRuleRoles(), studyParticipantCrfSchedule);
                    ProCtcQuestion question = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                    criticalSymptoms.add(new String[]{question.getProCtcTerm().getTerm(), question.getProCtcQuestionType().getDisplayName()});

                }
            }
        }
        logger.info("Send email to " + users);
        Notification notification = new Notification();
        if (criticalSymptoms.size() > 0) {
            try {
                String emailMessage = getEmaiContent(studyParticipantCrfSchedule, criticalSymptoms);
                notification.setText(emailMessage);
                notification.setDate(new Date());
                addUserNotifications(notification, studyParticipantCrfSchedule, users);
                genericRepository.save(notification);

                sendMail(getStringArr(emails), "Notification email", emailMessage);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean responseSatisfiesRule(StudyParticipantCrfItem studyParticipantCrfItem, NotificationRule notificationRule) {
        ProCtcTerm proCtcTerm = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm();
        ProCtcQuestionType proCtcQuestionType = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType();
        int threshold = studyParticipantCrfItem.getProCtcValidValue().getDisplayOrder();
        for (NotificationRuleSymptom notificationRuleSymptom : notificationRule.getNotificationRuleSymptoms()) {
            if (notificationRuleSymptom.getProCtcTerm().equals(proCtcTerm)) {
                for (NotificationRuleCondition notificationRuleCondition : notificationRule.getNotificationRuleConditions()) {
                    if (notificationRuleCondition.getProCtcQuestionType().equals(proCtcQuestionType)) {
                        switch (notificationRuleCondition.getNotificationRuleOperator()) {
                            case GREATER_EQUAL:
                                return threshold >= notificationRuleCondition.getThreshold().intValue();
                            case EQUAL:
                                return threshold == notificationRuleCondition.getThreshold().intValue();
                            case GREATER:
                                return threshold > notificationRuleCondition.getThreshold().intValue();
                            case LESS:
                                return threshold < notificationRuleCondition.getThreshold().intValue();
                            case LESS_EQUAL:
                                return threshold <= notificationRuleCondition.getThreshold().intValue();
                            default:
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static void addEmail(String email, HashSet<String> emails) {
        if (!StringUtils.isBlank(email)) {
            emails.add(email);
        }
    }

    private static void getRecipients(HashSet<User> users, HashSet<String> emails, List<NotificationRuleRole> notificationRuleRoles, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {

        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();
        for (NotificationRuleRole notificationRuleRole : notificationRuleRoles) {
            ClinicalStaff cs = null;
            if (notificationRuleRole.getRole().equals(Role.NURSE)) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getResearchNurse();
                if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        cs = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff();
                    }
                }
            }
            if (notificationRuleRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getTreatingPhysician();
                if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        cs = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff();
                    }
                }
            }
            if (notificationRuleRole.getRole().equals(Role.SITE_CRA)) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSiteCRAs()) {
                    if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
                        if (studyParticipantClinicalStaff.isNotify()) {
                            cs = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff();
                        }
                    }
                }
            }
            if (notificationRuleRole.getRole().equals(Role.SITE_PI)) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSitePIs()) {
                    if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
                        if (studyParticipantClinicalStaff.isNotify()) {
                            cs = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff();
                        }
                    }
                }
            }
            if (notificationRuleRole.getRole().equals(Role.LEAD_CRA)) {
                cs = studyParticipantAssignment.getStudySite().getStudy().getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff();
            }
            if (notificationRuleRole.getRole().equals(Role.PI)) {
                cs = studyParticipantAssignment.getStudySite().getStudy().getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff();
            }
            if (cs != null) {
                addEmail(cs.getEmailAddress(), emails);
                users.add(cs.getUser());
            }
        }

        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
            if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
                if (studyParticipantClinicalStaff.isNotify()) {
                    ClinicalStaff cs = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff();
                    addEmail(cs.getEmailAddress(), emails);
                    users.add(cs.getUser());
                }
            }
        }
        logger.info(emails);

    }

    private static void addUserNotifications(Notification notification, StudyParticipantCrfSchedule studyParticipantCrfSchedule, HashSet<User> users) {
        for (User user : users) {
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
        content.append("<tr><td><b>").append(boldText).append("</b>").append(nonBoldText).append("</td></tr>");
    }

    private static String getEmaiContent(StudyParticipantCrfSchedule studyParticipantCrfSchedule, ArrayList<String[]> criticalSymptoms) {

        StudyParticipantCrfSchedule previousSchedule = null;
        StudyParticipantCrfSchedule firstSchedule = null;
        HashMap<String, String> currentScheduleMap = getMapForSchedule(studyParticipantCrfSchedule);
        HashMap<String, String> previousScheduleMap = null;
        HashMap<String, String> firstScheduleMap = null;

        List<StudyParticipantCrfSchedule> allSchedules = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfSchedulesByStatus(CrfStatus.COMPLETED);
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
        addRow(emailContent, "Participant name: ", participant.getDisplayName());
        addRow(emailContent, "Participant email: ", (participant.getEmailAddress() == null ? "Not specified" : participant.getEmailAddress()));
        addRow(emailContent, "Participant contact phone: ", (participant.getPhoneNumber() == null ? "Not specified" : participant.getPhoneNumber()));
        addRow(emailContent, "Study site: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getDisplayName());
        addRow(emailContent, "Study: ", studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getStudy().getDisplayName());
        StudyOrganizationClinicalStaff rn = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getResearchNurse().getStudyOrganizationClinicalStaff();
        addRow(emailContent, "Research nurse: ", rn == null ? "Not assigned" : rn.getDisplayName());
        StudyOrganizationClinicalStaff tp = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getTreatingPhysician().getStudyOrganizationClinicalStaff();
        addRow(emailContent, "Treating physician: ", tp == null ? "Not assigned" : tp.getDisplayName());
        emailContent.append("</table>");
        emailContent.append("<br>This notification was triggered by following responses: <br><br>");

        emailContent.append("<table border=\"1\">");
        emailContent.append("<tr>");
        emailContent.append("<td><b>Symptom</b></td>");
        emailContent.append("<td><b>Attribute</b></td>");
        if (firstSchedule != null) {
            emailContent.append("<td><b>First visit (").append(DateUtils.format(firstSchedule.getStartDate())).append(")</b></td>");
        }
        if (previousSchedule != null) {
            emailContent.append("<td><b>Previous visit (").append(DateUtils.format(previousSchedule.getStartDate())).append(")</b></td>");
        }
        emailContent.append("<td><b>Current visit (").append(DateUtils.format(studyParticipantCrfSchedule.getStartDate())).append(")</b></td>");
        emailContent.append("</tr>");

        for (String[] symptom : criticalSymptoms) {
            emailContent.append("<tr>");
            String strSymptom = symptom[0];
            String strAttr = symptom[1];
            String key = strSymptom + "~" + strAttr;
            emailContent.append("<td>").append(strSymptom).append("</td>");
            emailContent.append("<td>").append(strAttr).append("</td>");
            if (firstSchedule != null) {
                emailContent.append("<td>").append(firstScheduleMap.get(key)).append("</td>");
            }
            if (previousSchedule != null) {
                emailContent.append("<td>").append(previousScheduleMap.get(key)).append("</td>");
            }
            emailContent.append("<td>").append(currentScheduleMap.get(key)).append("</td>");
            emailContent.append("</tr>");
        }
        emailContent.append("</table>");
//        emailContent.append("<br>If you want to remove this message from your ProCtcAE alerts, <a href=");
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

    public static void sendMail(String[] to, String subject, String content) {
        try {
            if (javaMailSender == null) {
                javaMailSender = new JavaMailSender();
            }
            logger.info("Sending emails to " + Arrays.toString(to));
            System.out.println("Sending emails to " + Arrays.toString(to));
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(content, javaMailSender.isHtml());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error(" Error in sending email , please check the confiuration - " + e.getMessage());
        }
    }

    public void setGenericRepository(GenericRepository inGenericRepository) {
        genericRepository = inGenericRepository;
    }

}