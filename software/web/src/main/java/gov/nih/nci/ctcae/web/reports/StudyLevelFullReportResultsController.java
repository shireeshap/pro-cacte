package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.jdbc.StudyWideFormatReportDao;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author AmeyS
 * Used to generate Overall study report.
 * Generate a wide format .csv report for ProCtc symptoms.
 * Also generates Eq5d report, if applicable for the selected study. 
 */
public class StudyLevelFullReportResultsController extends AbstractController {

	GenericRepository genericRepository;
	StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
	private StudyWideFormatReportDao studyWideFormatReportData;
	private ProOverallStudyReportHelper proReportHelper;
	private Eq5dOverallStudyReportHelper eq5dReportHelper;
	

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("reports/fullStudyReport");
		
		try{
			parseRequestParametersAndFetchData(request);
			Map<String, String> proParticipantInfoMap = new HashMap<String, String>();
			// Mapping a ProCtcQuestion to a column in Report.
			Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping = new TreeMap<String, Map<ProCtcQuestionType, String>>();
			// Mapping a MeddraQuestion to a column in Report.
			Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping = new TreeMap<String, Map<ProCtcQuestionType, String>>();
			// ProCtcQuestions to be displayed in Report's table header (ordered according to proCtcQuestionMapping)
			List<String> proCtcTermHeaders = new ArrayList<String>();
			// MeddraQuestions to be displayed in Report's table header (ordered according to meddraQuestionMapping)
			List<String> meddraTermHeaders = new ArrayList<String>();
			// Save the start dates of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<Date>>> proCrfDateMap = new TreeMap<String, LinkedHashMap<String, List<Date>>>();
			// Save the survey_answering_mode of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<String>>> proCrfModeMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
			// Save the survey status of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<CrfStatus>>> proCrfStatusMap = new TreeMap<String, LinkedHashMap<String, List<CrfStatus>>>();
			
			// Mapping a EQ-5D question to a column in Report.
			Map<String, Map<ProCtcQuestionType, String>> eq5dQuestionMapping = new TreeMap<String, Map<ProCtcQuestionType, String>>();
			// EQ-5D questions to be displayed in Report's table header (ordered according to eq5dQuestionMapping)
			List<String> eq5dTermHeaders = new ArrayList<String>();
			// Save the start dates of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<Date>>> eq5dCrfDateMap = new TreeMap<String, LinkedHashMap<String, List<Date>>>();
			// Save the survey_answering_mode of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<String>>> eq5dCrfModeMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
			// Save the survey status of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<CrfStatus>>> eq5dCrfStatusMap = new TreeMap<String, LinkedHashMap<String, List<CrfStatus>>>();
			// Save the VAS Health score
			Map<String, LinkedHashMap<String, List<String>>> eq5dCrfHealthScoreMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
			Map<String, String> eq5dParticipantInfoMap = new HashMap<String, String>();
		  
			
			int col = proReportHelper.generateQuestionMappingForTableHeader(proCtcQuestionMapping, proCtcTermHeaders);
			
			Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> overAllResults =
				proReportHelper.getCareResults(proCrfDateMap, proCrfModeMap, proCrfStatusMap, proParticipantInfoMap, meddraQuestionMapping, meddraTermHeaders, col);
			
			eq5dReportHelper.generateQuestionMappingForTableHeader(eq5dQuestionMapping, eq5dTermHeaders);
			Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> eq5dOverAllResults = 
				eq5dReportHelper.getCareResults(eq5dCrfDateMap, eq5dCrfModeMap, eq5dCrfStatusMap, eq5dCrfHealthScoreMap, eq5dParticipantInfoMap);
			
	
			modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
			request.getSession().setAttribute("sessionResultsMap", overAllResults);
			request.getSession().setAttribute("sessionCRFDatesMap", proCrfDateMap);
			request.getSession().setAttribute("sessionCRFModeMap", proCrfModeMap);
			request.getSession().setAttribute("sessionCRFStatusMap", proCrfStatusMap);
			request.getSession().setAttribute("sessionProCtcQuestionMapping", proCtcQuestionMapping);
			request.getSession().setAttribute("sessionMeddraQuestionMapping", meddraQuestionMapping);
			request.getSession().setAttribute("sessionProCtcTermHeaders", proCtcTermHeaders);
			request.getSession().setAttribute("sessionMeddraTermHeaders", meddraTermHeaders);
			request.getSession().setAttribute("participantInfoMap", proParticipantInfoMap);
			
			request.getSession().setAttribute("sessionEq5dResultsMap", eq5dOverAllResults);
			request.getSession().setAttribute("sessionEq5dCRFDatesMap", eq5dCrfDateMap);
			request.getSession().setAttribute("sessionEq5dCRFModeMap", eq5dCrfModeMap);
			request.getSession().setAttribute("sessionEq5dCRFStatusMap", eq5dCrfStatusMap);
			request.getSession().setAttribute("sessionEq5dHealthScoreMap", eq5dCrfHealthScoreMap);
			request.getSession().setAttribute("sessionEq5dProCtcQuestionMapping", eq5dQuestionMapping);
			request.getSession().setAttribute("sessionEq5dProCtcTermHeaders", eq5dTermHeaders);
			request.getSession().setAttribute("eq5dparticipantInfoMap", eq5dParticipantInfoMap);
		
		}catch (Exception e) {
			e.getStackTrace();
			logger.info("Debugging on error:" + e.getStackTrace());
		}
		return modelAndView;
	}
	
	public void addReportDataToSession(HttpServletRequest request, 
			Map<String, LinkedHashMap<String, List<Date>>> crfDateMap, Map<String, LinkedHashMap<String, List<String>>> crfModeMap,
			Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap, Map<String, String> participantInfoMap, 
			Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping,
			List<String> meddraTermHeaders, int col) throws ParseException {
		
		
	}
	
	private void parseRequestParametersAndFetchData(HttpServletRequest request) throws ParseException {
		String studyParam = request.getParameter("study");
		String crfIdParam = request.getParameter("crf");
		String studySiteParam = request.getParameter("studySite");
		Integer studyId = null;
		List<Integer> crfIds = null;
		Integer studySiteId = null;
		
		if(!StringUtils.isBlank(studyParam)){
			studyId = Integer.parseInt(studyParam);
		}

		if(!StringUtils.isBlank(crfIdParam)){
			Integer crfId = Integer.parseInt(crfIdParam);
			CRF crf = genericRepository.findById(CRF.class, crfId);
			crfIds = new ArrayList<Integer>();
			crfIds.add(crfId);
			while (crf.getParentCrf() != null) {
				crfIds.add(crf.getParentCrf().getId());
				crf = crf.getParentCrf();
			}
			request.getSession().setAttribute("crf", crf);
			request.getSession().setAttribute("crfTitle", crf.getTitle());
		}
		
		
		if (!StringUtils.isBlank(studySiteParam)) {
			studySiteId = Integer.parseInt(studySiteParam);
			StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studySiteId);
			request.getSession().setAttribute("organizationName", studyOrganization.getOrganization().getName());
		}
		
		boolean isEq5dReportData = false;
		proReportHelper.setSchedules(studyWideFormatReportData.getSchedulesOnly(studyId, crfIds, studySiteId, isEq5dReportData));
		proReportHelper.setResponses(studyWideFormatReportData.getResponsesOnly(studyId, crfIds, studySiteId, isEq5dReportData));
		proReportHelper.setAddedProCtcQuestions(studyWideFormatReportData.getAddedProQuestions(studyId, crfIds, studySiteId));
		proReportHelper.setAddedMeddraQuestions(studyWideFormatReportData.getAddedMeddraQuestions(studyId, crfIds, studySiteId));
		proReportHelper.setParticipantsAndOrganizations(studyWideFormatReportData.getParticipantsAndOrg(studyId, crfIds, studySiteId, isEq5dReportData));
		proReportHelper.setFirstResponseDates(studyWideFormatReportData.getFirstResponseDate(studyId, crfIds, studySiteId, isEq5dReportData));
		proReportHelper.setResponseModes(studyWideFormatReportData.getResponseModes(studyId, crfIds, studySiteId, isEq5dReportData));
		proReportHelper.setCrfQuestionsTemplate(studyWideFormatReportData.getCrfQuestionsTemplate(studyId, crfIds));
		
		isEq5dReportData = true;
		eq5dReportHelper.setSchedules(studyWideFormatReportData.getSchedulesOnly(studyId, crfIds, studySiteId, isEq5dReportData));
		eq5dReportHelper.setResponses(studyWideFormatReportData.getResponsesOnly(studyId, crfIds, studySiteId, isEq5dReportData));
		eq5dReportHelper.setParticipantsAndOrganizations(studyWideFormatReportData.getParticipantsAndOrg(studyId, crfIds, studySiteId, isEq5dReportData));
		eq5dReportHelper.setFirstResponseDates(studyWideFormatReportData.getFirstResponseDate(studyId, crfIds, studySiteId, isEq5dReportData));
		eq5dReportHelper.setResponseModes(studyWideFormatReportData.getResponseModes(studyId, crfIds, studySiteId, isEq5dReportData));
		eq5dReportHelper.setCrfQuestionsTemplate(studyWideFormatReportData.getCrfQuestionsTemplate(studyId, crfIds));
		
		request.getSession().setAttribute("study",
				genericRepository.findById(Study.class, Integer.parseInt(studyParam)));
	}
	
	@Required
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}
	@Required
	public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}
	
	 public StudyWideFormatReportDao getStudyWideFormatReportData() {
	        return studyWideFormatReportData;
	 }

	 public void setStudyWideFormatReportData(StudyWideFormatReportDao studyWideFormatReportData) {
	        this.studyWideFormatReportData = studyWideFormatReportData;
	 }
	 
	 @Required
	 public void setProReportHelper(ProOverallStudyReportHelper proReportHelper) {
			this.proReportHelper = proReportHelper;
	 }
	 
	 @Required
	 public void setEq5dReportHelper(Eq5dOverallStudyReportHelper eq5dReportHelper) {
			this.eq5dReportHelper = eq5dReportHelper;
	 }
}
