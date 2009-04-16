package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.api.RuleAuthoringService;
import com.semanticbits.rules.api.RulesEngineService;
import com.semanticbits.rules.objectgraph.FactResolver;
import com.semanticbits.rules.impl.RuleEvaluationResult;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.ListValues;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class NotificationsEvaluationService {


    public static void executeRules(StudyParticipantCrfSchedule studyParticipantCrfSchedule, int currentPageIndex, CRF crf, StudyOrganization studySite) {
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
                RuleSet ruleSet = ProCtcAERulesService.getExistingRuleSetForCrfAndSite(crf, studySite);
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
                inputObjects.add(studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf());

                List<Object> outputObjects = ProCtcAERulesService.fireRules(inputObjects, ruleSet.getName());

                for (Object o : outputObjects) {
                    if (o instanceof RuleEvaluationResult) {
                        RuleEvaluationResult result = (RuleEvaluationResult) o;
                        if (!StringUtils.isBlank(result.getMessage())) {
                            String message = result.getMessage();
                            System.out.println("Sending email to " + message);
                            SendEmails(message, studyParticipantCrfSchedule);
                        }
                    }
                }
            }
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
                    emails.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
                }
            }
            if (token.equals("PrimaryPhysician")) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = studyParticipantAssignment.getTreatingPhysician();
                if (studyParticipantClinicalStaff.isNotify()) {
                    emails.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
                }
            }
            if (token.equals("SiteCRA")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSiteCRAs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        emails.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
                    }
                }
            }
            if (token.equals("SitePI")) {
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getSitePIs()) {
                    if (studyParticipantClinicalStaff.isNotify()) {
                        emails.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
                    }
                }
            }
            if (token.equals("LeadCRA")) {
                emails.add(studyParticipantAssignment.getStudySite().getStudy().getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
            }
            if (token.equals("PI")) {
                emails.add(studyParticipantAssignment.getStudySite().getStudy().getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
            }
        }

        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
            if (studyParticipantClinicalStaff.isNotify()) {
                emails.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress());
            }
        }

        System.out.println(emails);
    }


}