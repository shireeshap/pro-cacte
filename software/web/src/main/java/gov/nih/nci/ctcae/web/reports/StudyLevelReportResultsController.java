package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class StudyLevelReportResultsController extends AbstractController {

    GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/studyLevelReportResults");

        StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);
        LinkedHashMap<Participant, ArrayList<Date>> datesMap = new LinkedHashMap<Participant, ArrayList<Date>>();

        TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> results = getCareResults(datesMap, list);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
        modelAndView.addObject("table", getHtmlTable(results, datesMap));

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDatesMap", datesMap);

        return modelAndView;
    }

    private TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> getCareResults(LinkedHashMap<Participant, ArrayList<Date>> datesMap, List<StudyParticipantCrfSchedule> schedules) throws ParseException {

        TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> organizationMap = new TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>(new OrganizationNameComparator());
        TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap;
        TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap;
        ArrayList<Date> dates;
        boolean participantAddedQuestion;
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
            Organization organization = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getOrganization();
            if (organizationMap.containsKey(organization)) {
                participantMap = organizationMap.get(organization);
            } else {
                participantMap = new TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>(new ParticipantNameComparator());
                organizationMap.put(organization, participantMap);
            }
            Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
            if (participantMap.containsKey(participant)) {
                symptomMap = participantMap.get(participant);
            } else {
                symptomMap = new TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>();
                participantMap.put(participant, symptomMap);
            }
            if (datesMap.containsKey(participant)) {
                dates = datesMap.get(participant);
            } else {
                dates = new ArrayList<Date>();
                datesMap.put(participant, dates);
            }
            dates.add(studyParticipantCrfSchedule.getStartDate());
            StudyParticipantCrfItem firstQuestion = new StudyParticipantCrfItem();
            ProCtcQuestion proCtcQuestion;

            ProCtcValidValue value;
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                if (proCtcQuestion.getDisplayOrder() == 1) {
                    firstQuestion = studyParticipantCrfItem;
                }
                String symptom = proCtcQuestion.getProCtcTerm().getProCtcTermVocab().getTermEnglish();
                value = studyParticipantCrfItem.getProCtcValidValue();
                participantAddedQuestion = false;
                buildMap(proCtcQuestion, symptom, value, symptomMap, firstQuestion, datesMap.get(participant).size() - 1, participantAddedQuestion);
            }
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                Question question = studyParticipantCrfScheduleAddedQuestion.getQuestion();
                String addedSymptom = question.getQuestionSymptom();
                ValidValue validValue;
                if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                    validValue = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                } else {
                    validValue = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue();
                }

                    value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                    String symptom = question.getQuestionSymptom();
                    participantAddedQuestion = true;
                    buildMap(question, symptom, validValue, symptomMap, null, datesMap.get(participant).size() - 1, participantAddedQuestion);
            }
        }

        return organizationMap;
    }

    public TreeMap<Organization, TreeMap<Participant, String>> getHtmlTable(TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> results, LinkedHashMap<Participant, ArrayList<Date>> datesMap) {
        TreeMap<Organization, TreeMap<Participant, String>> organizationTableMap = new TreeMap<Organization, TreeMap<Participant, String>>(new OrganizationNameComparator());

        for (Organization organization : results.keySet()) {
            TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap = results.get(organization);
            TreeMap<Participant, String> tableMap = new TreeMap<Participant, String>(new ParticipantNameComparator());
            organizationTableMap.put(organization, tableMap);
            for (Participant participant : participantMap.keySet()) {
                StringBuilder table = new StringBuilder();
                table.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                ArrayList valuesLists = new ArrayList();

                int numOfColumns = 1;
                TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap = participantMap.get(participant);
                for (String proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    numOfColumns = numOfColumns + questionMap.keySet().size();
                    for (Question proCtcQuestion : questionMap.keySet()) {
                        ArrayList<ValidValue> valuesList = questionMap.get(proCtcQuestion);
                        valuesLists.add(valuesList);
                    }
                }

                startRow(table);
                addColumn(table, "", 1, "");
                for (String proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    addColumn(table, proCtcTerm, questionMap.keySet().size(), "header-top");
                }
                endRow(table);
                startRow(table);
                addColumn(table, "", 1, "");
                for (String proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    for (Question question : questionMap.keySet()) {
                        ProCtcQuestion proQuestion = new ProCtcQuestion();
                        MeddraQuestion meddraQuestion = new MeddraQuestion();
                        if (question instanceof ProCtcQuestion) {
                            proQuestion = (ProCtcQuestion) question;
                            addColumn(table, proQuestion.getProCtcQuestionType().getDisplayName(), 1, "actual-question");
                        }
                        boolean isMeddraQuestion = false;
                        if (question instanceof MeddraQuestion) {
                            isMeddraQuestion = true;
                            meddraQuestion = (MeddraQuestion) question;
                            addColumn(table, meddraQuestion.getProCtcQuestionType().getDisplayName(), 1, "actual-question");
                        }

                    }
                }
                endRow(table);
                ArrayList<Date> dates = null;
                for (Participant participantT : datesMap.keySet()) {
                    if (participant.equals(participantT)) {
                        dates = datesMap.get(participant);
                        break;
                    }
                }

                int index = 0;
                ArrayList<ValidValue> valueList;
                if (dates != null) {
                    for (Date date : dates) {
                        startRow(table);
                        addColumn(table, DateUtils.format(date), 1, "");
                        for (Object obj : valuesLists) {
                            valueList = (ArrayList<ValidValue>) obj;
                            if (valueList.size() > index && valueList.get(index) != null) {
                                addColumn(table, valueList.get(index).getValue(SupportedLanguageEnum.ENGLISH), 1, "data");
                            } else {
                                addColumn(table, "", 1, "data");
                            }
                        }
                        index++;
                        endRow(table);
                    }
                }

                table.append("</table>");
                tableMap.put(participant, table.toString());
            }
        }
        return organizationTableMap;
    }


    private void buildMap(Question question, String symptom, ValidValue value, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap, StudyParticipantCrfItem firstQuestion, Integer dateIndex, boolean participantAddedQuestion) {
        LinkedHashMap<Question, ArrayList<ValidValue>> careResults;
        ProCtcQuestion proQuestion = new ProCtcQuestion();
        if (question instanceof ProCtcQuestion) {
            proQuestion = (ProCtcQuestion) question;
        }
        boolean isMeddraQuestion = false;
        if (question instanceof MeddraQuestion) {
            isMeddraQuestion = true;
        }
        ArrayList<ValidValue> validValue;
        if (symptomMap.containsKey(symptom)) {
            careResults = symptomMap.get(symptom);
        } else {
            careResults = new LinkedHashMap<Question, ArrayList<ValidValue>>();
            symptomMap.put(symptom, careResults);
        }

        if (careResults.containsKey(question)) {
            validValue = careResults.get(question);
        } else {
            validValue = new ArrayList<ValidValue>();
            for (int j = 0; j <= dateIndex; j++) {
                if (validValue.size() < j) {
                    validValue.add(null);
                }
            }
            careResults.put(question, validValue);
        }
        if (value == null && !isMeddraQuestion) {
            ProCtcValidValue myProCtcValidValue = ReportResultsHelper.getValidValueResponseCode(proQuestion, firstQuestion);
            myProCtcValidValue.setDisplayOrder(0);
            if (dateIndex > validValue.size()) {
                for (int j = validValue.size(); j < dateIndex; j++) {
                    validValue.add(null);
                }
            }
            validValue.add(dateIndex, myProCtcValidValue);
        } else {
            if (dateIndex > validValue.size()) {
                for (int j = validValue.size(); j < dateIndex; j++) {
                    validValue.add(null);
                }
            }
            validValue.add(dateIndex, value);
        }


    }

    private StudyParticipantCrfScheduleQuery parseRequestParametersAndFormQuery(HttpServletRequest request) throws ParseException {
        StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
        Integer studyId = Integer.parseInt(request.getParameter("study"));
        String studySite = request.getParameter("studySite");
        Integer crfId = Integer.parseInt(request.getParameter("crf"));
        CRF crf = genericRepository.findById(CRF.class, crfId);
        List<Integer> crfIds = new ArrayList();
        crfIds.add(crfId);
        if (crf.getParentCrf() != null) {
            crfIds.add(crf.getParentCrf().getId());
        }
        query.filterByCRFIds(crfIds);
        query.filterByStudy(studyId);
        query.filterByStatus(CrfStatus.COMPLETED);
        if (!StringUtils.isBlank(studySite)) {
            Integer studySiteId = Integer.parseInt(studySite);
            query.filterByStudySite(studySiteId);
        }
        request.getSession().setAttribute("study", genericRepository.findById(Study.class, studyId));
        request.getSession().setAttribute("crf", genericRepository.findById(CRF.class, crfId));

        return query;
    }

    private void startRow(StringBuilder table) {
        table.append("<tr>");
    }

    private void endRow(StringBuilder table) {
        table.append("</tr>");
    }

    private void addColumn(StringBuilder table, String text, int colSpan, String style) {
        if (text == null) {
            text = "";
        }
        String colSpanStr = "";
        if (colSpan > 1) {
            colSpanStr = "colspan=\"" + colSpan + "\"";
        }
        table.append("<td ").append(colSpanStr).append(" class=\"").append(style).append("\">").append(text).append("</td>");
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}