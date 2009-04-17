package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.objectgraph.FactResolver;
import com.semanticbits.rules.impl.RuleEvaluationResult;
import com.semanticbits.rules.exception.RuleSetNotFoundException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.IOException;

import gov.nih.nci.ctcae.core.domain.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class NotificationsEvaluationService {

    private static JavaMailSender javaMailSender;
    protected static final Log logger = LogFactory.getLog(NotificationsEvaluationService.class);


    public static void executeRules(StudyParticipantCrfSchedule studyParticipantCrfSchedule, CRF crf, StudyOrganization studySite) {
        RuleSet ruleSet = ProCtcAERulesService.getExistingRuleSetForCrfAndSite(crf, studySite);
        if (ruleSet == null) {
            return;
        }
        HashSet<Integer> distinctPageNumbers = new HashSet<Integer>();


        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            distinctPageNumbers.add(studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber());
        }

        Iterator<Integer> it = distinctPageNumbers.iterator();
        while (it.hasNext()) {
            List<StudyParticipantCrfItem> studyParticipantCrfItems = new ArrayList<StudyParticipantCrfItem>();
            int currentPageNumber = it.next();
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                if (studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber() == currentPageNumber) {
                    studyParticipantCrfItems.add(studyParticipantCrfItem);
                }
            }
            if (studyParticipantCrfItems.size() > 0) {
                List<Object> inputObjects = new ArrayList<Object>();

                for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {
                    if (studyParticipantCrfItem.getProCtcValidValue() != null) {
                        inputObjects.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm());
                        inputObjects.add(studyParticipantCrfItem.getProCtcValidValue());
                        inputObjects.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType());
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
                                String message = result.getMessage();
                                logger.info("Sending email to " + message);
                                SendEmails(message, studyParticipantCrfSchedule);
                            }
                        }
                    }
                } catch (RuleSetNotFoundException e) {
                    logger.error("RuleSet not found - " + e.getMessage());
                    return;
                }
            }
        }
    }

    private static void addEmail(String email, HashSet<String> emails) {
        if (!StringUtils.isBlank(email)) {
            emails.add(email);
        }
    }

    private static void SendEmails(String message, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();

        HashSet<String> emails = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(message, "||");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("PrimaryNurse")) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getResearchNurse();
                if (studyParticipantClinicalStaff.isNotify()) {
                    addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                }
            }
            if (token.equals("PrimaryPhysician")) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getTreatingPhysician();
                if (studyParticipantClinicalStaff.isNotify()) {
                    addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                }
            }
            if (token.equals("SiteCRA")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSiteCRAs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                    }
                }
            }
            if (token.equals("SitePI")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSitePIs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
                    }
                }
            }
            if (token.equals("LeadCRA")) {
                addEmail(studyParticipantAssignment.getStudySite().getStudy().getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
            }
            if (token.equals("PI")) {
                addEmail(studyParticipantAssignment.getStudySite().getStudy().getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
            }
        }

        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
            if (studyParticipantClinicalStaff.isNotify()) {
                addEmail(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress(), emails);
            }
        }

        logger.info(emails);
        try {
            String content = "This email is being sent from ProCtcAe system for ";
            content += "participant " + studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName() + ", triggered by the responses to the following questions";
            sendMail(getStringArr(emails), "Notification email", content);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
            helper.setText(content);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new Exception(" Error in sending email , please check the confiuration ", e);
        }
    }

}