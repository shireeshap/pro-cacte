package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.jdbc.support.CrfQuestionsTemplateWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ParticipantAndOganizationWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ResponseWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.SpcrfsWrapper;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.text.ParseException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author AmeyS
 *         Helper for generating overall study report for Eq5D surverys
 */
public class Eq5dOverallStudyReportHelper {
    GenericRepository genericRepository;
    private Log logger;
    List<SpcrfsWrapper> schedules;
    Map<Integer, List<ResponseWrapper>> responses;
    Map<Integer, List<ParticipantAndOganizationWrapper>> participantsAndOrganizations;
    Map<Integer, Date> firstResponseDates;
    Map<Integer, HashSet<String>> responseModes;
    Map<String, List<CrfQuestionsTemplateWrapper>> crfQuestionsTemplate;
    private static String FREQUENCY = "FRQ";
    private static String INTERFERENCE = "INT";
    private static String SEVERITY = "SEV";
    private static String PRESENT = "PRES";
    private static String AMOUNT = "AMT";
    private static Integer MARK_MANUAL_SKIP = -55;
    private static String NOT_AVAILABLE = "Not Available";

    public Eq5dOverallStudyReportHelper() {
        logger = LogFactory.getLog(getClass());
    }


    public int generateQuestionMappingForTableHeader(Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping,
                                                     List<String> proCtcTermHeaders) {

        int col = 0;
        String prefix = "";
        String proCtcTermEnglishTermText;
        Map<ProCtcQuestionType, String> typeMap;
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery(true, false, true);
        List<ProCtcTerm> proCtcTerms = genericRepository.find(proCtcTermQuery);
        for (ProCtcTerm proCtcTerm : proCtcTerms) {
            if (proCtcQuestionMapping.containsKey(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH))) {
                typeMap = proCtcQuestionMapping.get(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH));
            } else {
                typeMap = new TreeMap<ProCtcQuestionType, String>();
                proCtcQuestionMapping.put(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH), typeMap);
            }
            for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
                typeMap.put(proCtcQuestion.getProCtcQuestionType(), String.valueOf(col++));
                switch (proCtcQuestion.getProCtcQuestionType()) {
                    case FREQUENCY:
                        prefix = FREQUENCY;
                        break;

                    case SEVERITY:
                        prefix = SEVERITY;
                        break;

                    case INTERFERENCE:
                        prefix = INTERFERENCE;
                        break;

                    case PRESENT:
                        prefix = PRESENT;
                        break;
                    case AMOUNT:
                        prefix = AMOUNT;
                        break;
                }

                proCtcTermEnglishTermText = prefix + "_" + proCtcTerm.getProCtcTermVocab().getTermEnglish();
                if (proCtcTermEnglishTermText.length() > 32)
                    proCtcTermEnglishTermText = proCtcTermEnglishTermText.substring(0, 32);
                proCtcTermHeaders.add(proCtcTermEnglishTermText);
            }
        }
        return col;
    }

    public Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> getCareResults(
            Map<String, Map<String, LinkedHashMap<String, List<Date>>>>  crfDateMap,
            Map<String, LinkedHashMap<String, List<String>>> crfModeMap,
            Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap,
            Map<String, LinkedHashMap<String, List<String>>> healthScoreMap,
            Map<String, String> participantInfoMap) throws ParseException {

        Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> organizationMap =
                new TreeMap<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>();
        Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap;
        Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap;
        Map<String, LinkedHashMap<String, List<String>>> symptomMap;

        LinkedHashMap<String, List<Date>> datesToListByType;
        ArrayList<String> appModes;
        ArrayList<CrfStatus> statusList;
        ArrayList<String> healthScoreList;

        for (SpcrfsWrapper studyParticipantCrfSchedule : schedules) {


            String organization = participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getOrganizationName();

            if (organizationMap.get(organization) != null) {
                crfMap = organizationMap.get(organization);
            } else {
                crfMap = new TreeMap<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>();
                organizationMap.put(organization, crfMap);
            }

            String crf = studyParticipantCrfSchedule.getCrfTitle();
            if (crfMap.get(crf) != null) {
                participantMap = crfMap.get(crf);
            } else {
                participantMap = new TreeMap<String, Map<String, LinkedHashMap<String, List<String>>>>();
                ;
                crfMap.put(crf, participantMap);
            }

            String participant = participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getParticipantId();
            if (participantInfoMap.get(participant) == null) {
                participantInfoMap.put(participant, participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getStudyParticipantIdentifier());
            }
            if (participantMap.get(participant) != null) {
                symptomMap = participantMap.get(participant);
            } else {
                symptomMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
                participantMap.put(participant, symptomMap);
            }

            Map<String, LinkedHashMap<String, List<Date>>> datesMap;
            LinkedHashMap<String, List<String>> modeMap;
            LinkedHashMap<String, List<CrfStatus>> statusMap;
            LinkedHashMap<String, List<String>> VasMap;

            if(crfDateMap.get(crf) != null){
                datesMap = crfDateMap.get(crf);
            } else {
                datesMap = new LinkedHashMap<>();
                crfDateMap.put(crf, datesMap);
            }


            if (crfModeMap.containsKey(crf)) {
                modeMap = crfModeMap.get(crf);
            } else {
                modeMap = new LinkedHashMap<String, List<String>>();
                crfModeMap.put(crf, modeMap);
            }

            if (crfStatusMap.containsKey(crf)) {
                statusMap = crfStatusMap.get(crf);
            } else {
                statusMap = new LinkedHashMap<String, List<CrfStatus>>();
                crfStatusMap.put(crf, statusMap);
            }

            if (datesMap.containsKey(participant)) {
                datesToListByType = datesMap.get(participant);
            } else {
                //init the lists
                datesToListByType = new LinkedHashMap<>();
                datesToListByType.put("firstResponseDates", new ArrayList<Date>());
                datesToListByType.put("scheduledStartDates", new ArrayList<Date>());
                datesToListByType.put("scheduledEndDates", new ArrayList<Date>());
                datesMap.put(participant, datesToListByType);
            }

            if (healthScoreMap.get(crf) != null) {
                VasMap = healthScoreMap.get(crf);
            } else {
                VasMap = new LinkedHashMap<String, List<String>>();
                healthScoreMap.put(crf, VasMap);
            }

            if (modeMap.containsKey(participant)) {
                appModes = (ArrayList<String>) modeMap.get(participant);
            } else {
                appModes = new ArrayList<String>();
                modeMap.put(participant, appModes);
            }

            if (statusMap.containsKey(participant)) {
                statusList = (ArrayList<CrfStatus>) statusMap.get(participant);
            } else {
                statusList = new ArrayList<CrfStatus>();
                statusMap.put(participant, statusList);
            }

            if (VasMap.containsKey(participant)) {
                healthScoreList = (ArrayList<String>) VasMap.get(participant);
            } else {
                healthScoreList = new ArrayList<String>();
                VasMap.put(participant, healthScoreList);
            }

            statusList.add(studyParticipantCrfSchedule.getStatus());
            if (studyParticipantCrfSchedule.getHealthAmount() != null) {
                healthScoreList.add(studyParticipantCrfSchedule.getHealthAmount().toString());
            } else {
                healthScoreList.add(NOT_AVAILABLE);
            }
            String mode = null;
            if (responseModes.get(studyParticipantCrfSchedule.getId()) != null) {
                HashSet<String> responseModeSet = (HashSet<String>) responseModes.get(studyParticipantCrfSchedule.getId());
                Iterator<String> itr = responseModeSet.iterator();
                mode = (itr.hasNext() ? itr.next() : null);
                while (itr.hasNext()) {
                    mode += ", " + itr.next();
                }

            }
            appModes.add(mode);
            datesToListByType.get("firstResponseDates").add(firstResponseDates.get(studyParticipantCrfSchedule.getId()));
            datesToListByType.get("scheduledStartDates").add(studyParticipantCrfSchedule.getStartDate());
            datesToListByType.get("scheduledEndDates").add(studyParticipantCrfSchedule.getDueDate());



            if (responses.get(studyParticipantCrfSchedule.getId()) != null) {
                for (ResponseWrapper studyParticipantCrfItem : responses.get(studyParticipantCrfSchedule.getId())) {
                    try {
                        String symptom = studyParticipantCrfItem.getTermEnglish();
                        String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
                        String value = studyParticipantCrfItem.getResponseCode();

                        LinkedHashMap<String, List<String>> careResults;
                        ArrayList<String> validValue;
                        if (symptomMap.get(symptom) != null) {
                            careResults = symptomMap.get(symptom);
                        } else {
                            careResults = new LinkedHashMap<String, List<String>>();
                            symptomMap.put(symptom, careResults);
                        }

                        if (careResults.get(questionType) != null) {
                            validValue = (ArrayList<String>) careResults.get(questionType);
                        } else {
                            validValue = new ArrayList<String>();
                            careResults.put(questionType, validValue);
                        }

                        int dateIndex = datesMap.get(participant).get("firstResponseDates").size() - 1;
                        if (dateIndex > validValue.size()) {
                            for (int j = validValue.size(); j < dateIndex; j++) {
                                validValue.add(null);
                            }
                        }

                        if (value == null) {
                            validValue.add(MARK_MANUAL_SKIP.toString());
                        } else {
                            validValue.add(value);
                        }
                    } catch (Exception e) {
                        logger.debug(new String("Error in populating responses: " + e.getMessage()));
                        e.printStackTrace();
                    }
                }
            } else {
                defaultResponses(symptomMap, crfQuestionsTemplate.get(crf));
            }
        }
        return organizationMap;
    }

    // Mark by default as MANUAL_SKIP (-55), for schedules for which studyParticipantCrfItems are not yet created in the db.
    private void defaultResponses(Map<String, LinkedHashMap<String, List<String>>> symptomMap, List<CrfQuestionsTemplateWrapper> studyParticipantCrfItems) {
        for (CrfQuestionsTemplateWrapper studyParticipantCrfItem : studyParticipantCrfItems) {
            String symptom = studyParticipantCrfItem.getTermEnglish();
            String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
            String value = MARK_MANUAL_SKIP.toString();

            LinkedHashMap<String, List<String>> careResults;
            List<String> validValue;
            if (symptomMap.get(symptom) != null) {
                careResults = symptomMap.get(symptom);
            } else {
                careResults = new LinkedHashMap<String, List<String>>();
                symptomMap.put(symptom, careResults);
            }

            if (careResults.get(questionType) != null) {
                validValue = (ArrayList<String>) careResults.get(questionType);
            } else {
                validValue = new ArrayList<String>();
                careResults.put(questionType, validValue);
            }
            validValue.add(value);
        }
    }

    public void setSchedules(List<SpcrfsWrapper> eq5dSchedules) {
        this.schedules = eq5dSchedules;
    }


    public void setResponses(Map<Integer, List<ResponseWrapper>> eq5dResoponses) {
        this.responses = eq5dResoponses;
    }


    public void setParticipantsAndOrganizations(
            Map<Integer, List<ParticipantAndOganizationWrapper>> eq5dParticipantsAndOrganizations) {
        this.participantsAndOrganizations = eq5dParticipantsAndOrganizations;
    }


    public void setFirstResponseDates(Map<Integer, Date> eq5dFirstResponseDates) {
        this.firstResponseDates = eq5dFirstResponseDates;
    }


    public void setResponseModes(Map<Integer, HashSet<String>> eq5dResponseModes) {
        this.responseModes = eq5dResponseModes;
    }

    public void setCrfQuestionsTemplate(
            Map<String, List<CrfQuestionsTemplateWrapper>> crfQuestionsTemplate) {
        this.crfQuestionsTemplate = crfQuestionsTemplate;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
