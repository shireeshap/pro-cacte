package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
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

public class StudyLevelFullReportResultsController extends AbstractController {

	GenericRepository genericRepository;

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("reports/fullStudyReport");

		// List of all the answered surveys, by all the participants, for all the crf's associated with the selected Study at each of the studySite.
		StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request);
		List<StudyParticipantCrfSchedule> list = genericRepository.find(query);

		// Mapping a ProCtcQuestion to a column in Report.
		TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping = new TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>>(new ProCtcTermNameComparator());
		// Mapping a MeddraQuestion to a column in Report.
		TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping = new TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>>(new lowLevelTermNameComparator());
		// ProCtcQuestion List to be displayed in Report's table header (ordered according to proCtcQuestionMapping)
		ArrayList<String> proCtcTermHeaders = new ArrayList<String>();
		// MeddraQuestion List to be displayed in Report's table header (ordered according to meddraQuestionMapping)
		ArrayList<String> meddraTermHeaders = new ArrayList<String>();

		// Save the start dates of the submitted surveys listed in 'list'
		TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap = new TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>(new CrfNameComparator());
		// Save the survey_answering_mode of the submitted surveys listed in 'list'
		TreeMap<CRF, LinkedHashMap<Participant, ArrayList<AppMode>>> crfModeMap = new TreeMap<CRF, LinkedHashMap<Participant, ArrayList<AppMode>>>(new CrfNameComparator());

		generateQuestionMappingForTableHeader(proCtcQuestionMapping, meddraQuestionMapping, proCtcTermHeaders, meddraTermHeaders);
		TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> overAllResults = getCareResults(crfDateMap, crfModeMap, list);

		modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
	  //modelAndView.addObject("table", getHtmlTable(results, datesMap));
		request.getSession().setAttribute("sessionResultsMap", overAllResults);
		request.getSession().setAttribute("sessionCRFDatesMap", crfDateMap);
		request.getSession().setAttribute("sessionCRFModeMap", crfModeMap);
		request.getSession().setAttribute("sessionProCtcQuestionMapping", proCtcQuestionMapping);
		request.getSession().setAttribute("sessionMeddraQuestionMapping", meddraQuestionMapping);
		request.getSession().setAttribute("sessionProCtcTermHeaders", proCtcTermHeaders);
		request.getSession().setAttribute("sessionMeddraTermHeaders", meddraTermHeaders);

		return modelAndView;
	}

	private int generateQuestionMappingForTableHeader(TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping,
			TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping, ArrayList<String> proCtcTermHeaders, ArrayList<String> meddraTermHeaders) {
		
		int col = 0;
		TreeMap<ProCtcQuestionType, String> typeMap;
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		List<ProCtcTerm> proCtcTerms = genericRepository.find(proCtcTermQuery);
		for (ProCtcTerm proCtcTerm : proCtcTerms) {
			if (proCtcQuestionMapping.containsKey(proCtcTerm)) {
				typeMap = proCtcQuestionMapping.get(proCtcTerm);
			} else {
				typeMap = new TreeMap<ProCtcQuestionType, String>();
				proCtcQuestionMapping.put(proCtcTerm, typeMap);
			}
			for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
				typeMap.put(proCtcQuestion.getProCtcQuestionType(), String.valueOf(col++));
				proCtcTermHeaders.add(proCtcTerm.getProCtcTermVocab().getTermEnglish()+ "_"	+ proCtcQuestion.getProCtcQuestionType());
			}
		}

		MeddraQuery meddraQuery = new MeddraQuery();
		List<LowLevelTerm> lowLevelTerms = genericRepository.find(meddraQuery);
		for (LowLevelTerm lowLevelTerm : lowLevelTerms) {
			if (lowLevelTerm.getMeddraQuestions().size() != 0) {
				if (meddraQuestionMapping.containsKey(lowLevelTerm)) {
					typeMap = proCtcQuestionMapping.get(lowLevelTerm);
				} else {
					typeMap = new TreeMap<ProCtcQuestionType, String>();
					meddraQuestionMapping.put(lowLevelTerm, typeMap);
				}
				for (MeddraQuestion meddraQuestion : lowLevelTerm.getMeddraQuestions()) {
					typeMap.put(meddraQuestion.getProCtcQuestionType(), 	String.valueOf(col++));
					meddraTermHeaders.add(lowLevelTerm.getLowLevelTermVocab().getMeddraTermEnglish()+ "_"+ meddraQuestion.getProCtcQuestionType());
				}
			}
		}
		return col;
	}

	private TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> getCareResults(
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap,TreeMap<CRF, LinkedHashMap<Participant, ArrayList<AppMode>>> crfModeMap,
			List<StudyParticipantCrfSchedule> schedules) throws ParseException {

		TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> organizationMap = 
				new TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>(new OrganizationNameComparator());
		TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> crfMap;
		TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap;
		TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap;
		ArrayList<Date> dates;
		ArrayList<AppMode> appModes;
		boolean participantAddedQuestion;
		for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
			Organization organization = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getOrganization();

			if (organizationMap.containsKey(organization)) {
				crfMap = organizationMap.get(organization);
			} else {
				crfMap = new TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>(new CrfNameComparator());
				organizationMap.put(organization, crfMap);
			}

			CRF crf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
			if (crfMap.containsKey(crf)) {
				participantMap = crfMap.get(crf);
			} else {
				participantMap = new TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>(new ParticipantNameComparator());
				crfMap.put(crf, participantMap);
			}
			Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
			if (participantMap.containsKey(participant)) {
				symptomMap = participantMap.get(participant);
			} else {
				symptomMap = new TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>();
				participantMap.put(participant, symptomMap);
			}

			LinkedHashMap<Participant, ArrayList<Date>> datesMap;
			LinkedHashMap<Participant, ArrayList<AppMode>> modeMap;
			if (crfDateMap.containsKey(crf)) {
				datesMap = crfDateMap.get(crf);
			} else {
				datesMap = new LinkedHashMap<Participant, ArrayList<Date>>();
				crfDateMap.put(crf, datesMap);
			}

			if (crfModeMap.containsKey(crf)) {
				modeMap = crfModeMap.get(crf);
			} else {
				modeMap = new LinkedHashMap<Participant, ArrayList<AppMode>>();
				crfModeMap.put(crf, modeMap);
			}

			if (datesMap.containsKey(participant)) {
				dates = datesMap.get(participant);
			} else {
				dates = new ArrayList<Date>();
				datesMap.put(participant, dates);
			}

			if (modeMap.containsKey(participant)) {
				appModes = modeMap.get(participant);
			} else {
				appModes = new ArrayList<AppMode>();
				modeMap.put(participant, appModes);
			}

			dates.add(studyParticipantCrfSchedule.getStartDate());
			AppMode appModeForSurvery;
			if (studyParticipantCrfSchedule.getStudyParticipantCrfItems().size() > 0) {
				if (studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(0).getResponseMode() != null) {
					appModes.add(studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(0).getResponseMode());
				} else {
					appModes.add(null);
				}
			} else {
				appModes.add(null);
			}

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

	public TreeMap<Organization, TreeMap<Participant, String>> getHtmlTable(
			TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> results, LinkedHashMap<Participant, ArrayList<Date>> datesMap) {
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
					addColumn(table, proCtcTerm, questionMap.keySet().size(),"header-top");
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
								addColumn(
										table, valueList.get(index).getValue(SupportedLanguageEnum.ENGLISH), 1, "data");
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

	private void buildMap(Question question, String symptom, ValidValue value, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap,
			StudyParticipantCrfItem firstQuestion, Integer dateIndex, boolean participantAddedQuestion) {
		
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

	private StudyParticipantCrfScheduleQuery parseRequestParametersAndFormQuery(
			HttpServletRequest request) throws ParseException {
		
		StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
		Integer studyId = Integer.parseInt(request.getParameter("study"));
		query.filterByStudy(studyId);
		query.filterByStatus(CrfStatus.COMPLETED);
		request.getSession().setAttribute("study",
				genericRepository.findById(Study.class, studyId));
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
