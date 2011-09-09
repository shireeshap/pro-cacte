package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

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

        TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>> results = getCareResults(datesMap, list);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
        modelAndView.addObject("table", getHtmlTable(results, datesMap));

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDatesMap", datesMap);

        return modelAndView;
    }

    private TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>> getCareResults(LinkedHashMap<Participant, ArrayList<Date>> datesMap, List<StudyParticipantCrfSchedule> schedules) throws ParseException {

        TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>> organizationMap = new TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>>(new OrganizationNameComparator());
        TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> participantMap;
        TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap;
        ArrayList<Date> dates;

        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
            Organization organization = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getOrganization();
            if (organizationMap.containsKey(organization)) {
                participantMap = organizationMap.get(organization);
            } else {
                participantMap = new TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>(new ParticipantNameComparator());
                organizationMap.put(organization, participantMap);
            }
            Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
            if (participantMap.containsKey(participant)) {
                symptomMap = participantMap.get(participant);
            } else {
                symptomMap = new TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
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
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                if (proCtcQuestion.getDisplayOrder() == 1) {
                    firstQuestion = studyParticipantCrfItem;
                }
                ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                buildMap(proCtcQuestion, symptom, value, symptomMap, firstQuestion);
            }
        }

        return organizationMap;
    }

    public TreeMap<Organization, TreeMap<Participant, String>> getHtmlTable(TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>> results, LinkedHashMap<Participant, ArrayList<Date>> datesMap) {
        TreeMap<Organization, TreeMap<Participant, String>> organizationTableMap = new TreeMap<Organization, TreeMap<Participant, String>>(new OrganizationNameComparator());

        for (Organization organization : results.keySet()) {
            TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> participantMap = results.get(organization);
            TreeMap<Participant, String> tableMap = new TreeMap<Participant, String>(new ParticipantNameComparator());
            organizationTableMap.put(organization, tableMap);
            for (Participant participant : participantMap.keySet()) {
                StringBuilder table = new StringBuilder();
                table.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                ArrayList valuesLists = new ArrayList();

                int numOfColumns = 1;
                TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = participantMap.get(participant);
                for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    numOfColumns = numOfColumns + questionMap.keySet().size();
                    for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                        ArrayList<ProCtcValidValue> valuesList = questionMap.get(proCtcQuestion);
                        valuesLists.add(valuesList);
                    }
                }

                startRow(table);
                addColumn(table, "", 1, "");
                for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    addColumn(table, proCtcTerm.getProCtcTermVocab().getTermEnglish(), questionMap.keySet().size(), "header-top");
                }
                endRow(table);
                startRow(table);
                addColumn(table, "", 1, "");
                for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                        addColumn(table, proCtcQuestion.getProCtcQuestionType().getDisplayName(), 1, "actual-question");
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
                if (dates != null) {
                    for (Date date : dates) {
                        startRow(table);
                        addColumn(table, DateUtils.format(date), 1, "");
                        for (Object obj : valuesLists) {
                            ArrayList<ProCtcValidValue> valueList = (ArrayList<ProCtcValidValue>) obj;
                            addColumn(table, valueList.get(index).getValue(SupportedLanguageEnum.ENGLISH), 1, "data");
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


    private void buildMap(ProCtcQuestion proCtcQuestion, ProCtcTerm symptom, ProCtcValidValue value, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap, StudyParticipantCrfItem firstQuestion) {
        LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults;
        ArrayList<ProCtcValidValue> validValue;
        if (symptomMap.containsKey(symptom)) {
            careResults = symptomMap.get(symptom);
        } else {
            careResults = new LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>();
            symptomMap.put(symptom, careResults);
        }

        if (careResults.containsKey(proCtcQuestion)) {
            validValue = careResults.get(proCtcQuestion);
        } else {
            validValue = new ArrayList<ProCtcValidValue>();
            careResults.put(proCtcQuestion, validValue);
        }
        if (value == null) {
            ProCtcValidValue myProCtcValidValue = ReportResultsHelper.getValidValueResponseCode(proCtcQuestion, firstQuestion);
//            ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
//            myProCtcValidValue.setProCtcQuestion(proCtcQuestion);
            myProCtcValidValue.setDisplayOrder(0);
            validValue.add(myProCtcValidValue);
        } else {
            validValue.add(value);
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
        if (crf.getParentCrf()!=null) {
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
