package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.AddedSymptomVerbatim;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.AddedSymptomVerbatimQuery;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.repository.AddedSymptomVerbatimRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * User: Harsh, Amey
 * Date: Jun 2, 2009
 * Time: 8:01:32 AM
 */
public class ParticipantAddedQuestionsReportResultsController extends AbstractController {
    GenericRepository genericRepository;
    AddedSymptomVerbatimRepository addedSymptomVerbatimRepository;
    public static final String RESULTANT_VERBATIM_WRAPPER = "resultantVerbatimWrapper";
    
    

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantAddedQuestionsResults");

        boolean isMeddraAddedQuestionQuery = false;
        ParticipantAddedQuestionsReportQuery query = new ParticipantAddedQuestionsReportQuery();
        parseRequestParametersAndFormQuery(request, query, isMeddraAddedQuestionQuery);
        List result = genericRepository.find(query);

        isMeddraAddedQuestionQuery = true;
        ParticipantAddedQuestionsReportQuery meddraQuery = new ParticipantAddedQuestionsReportQuery(isMeddraAddedQuestionQuery);
        parseRequestParametersAndFormQuery(request, meddraQuery, isMeddraAddedQuestionQuery);
        List meddraResults = genericRepository.find(meddraQuery);
        
        result.addAll(meddraResults);
        
        List<ParticipantAddedSymptomVerbatimWrapper> verbatimWrappers = generateAddedSymptomsExport(request);
        request.getSession().setAttribute(RESULTANT_VERBATIM_WRAPPER, verbatimWrappers);
        
        modelAndView.addObject("results", result);
        modelAndView.addObject("isReportEmpty", result.isEmpty());
        return modelAndView;
    }

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, ParticipantAddedQuestionsReportQuery query, boolean isMeddraAddedQuestionQuery) throws ParseException {
    	String crfParam = request.getParameter("crf");
        String studySiteId = request.getParameter("studySite");
        String symptom = request.getParameter("symptom");
        String studyParam = request.getParameter("study");
        
        if(StringUtils.isBlank(studyParam)){
        	studyParam = (String) request.getSession().getAttribute("study");
        } else {
        	request.getSession().setAttribute("study", studyParam);
        }
        
        if(!StringUtils.isBlank(crfParam)){
    		int crfId = Integer.parseInt(crfParam);
    		CRF crf = genericRepository.findById(CRF.class, crfId);
    		List<Integer> crfIds = new ArrayList<Integer>();
    		crfIds.add(crf.getId());
    		while(crf.getParentCrf()!= null){
    			crfIds.add(crf.getParentCrf().getId());
    			crf = crf.getParentCrf();
    		}
    		query.filterByCrfs(crfIds);
    	} else if(!StringUtils.isBlank(studyParam)){
    		List<CRF> crfList = ReportResultsHelper.getReducedCrfs(Integer.parseInt(studyParam));
    		List<Integer> crfIds = new ArrayList<Integer>();
    		for(CRF crf : crfList){
    			crfIds.add(crf.getId());
    			while(crf.getParentCrf() != null){
    				crfIds.add(crf.getId());
    				crf = crf.getParentCrf();
    			}
    		}
    		if(!crfIds.isEmpty()){
    			query.filterByCrfs(crfIds);
        	} else {
        		query.filterByStudyId(Integer.parseInt(studyParam));
        	}
    	}
       
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }
        
        if(!isMeddraAddedQuestionQuery){
        	if (!StringUtils.isBlank(symptom)) {
        		query.filterBySymptom(symptom);
        	}
        } else {
        	if (!StringUtils.isBlank(symptom)) {
        		query.filterByMeddraSymptom(symptom);
        	}
        }
    }
    
    
    protected List<ParticipantAddedSymptomVerbatimWrapper> generateAddedSymptomsExport(HttpServletRequest request) throws ParseException {
    	List<ParticipantAddedSymptomVerbatimWrapper> verbatimWrappers = new ArrayList<ParticipantAddedSymptomVerbatimWrapper>();
    	
    	ParticipantAddedQuestionsReportQuery query = new ParticipantAddedQuestionsReportQuery(QueryStrings.STUDY_PARTICIPANT_CRF_SCHEDULED_ADDED_QUESTION_BASIC);
    	parseAndBuildParticipantAddedQuestionsReportQuery(request, query);
    	List<StudyParticipantCrfScheduleAddedQuestion> spcrfsAddedQuestions = genericRepository.find(query);
    	
    	Map<Integer, Map<ProCtcTerm, String>> proVerbatimMap = new HashMap<Integer, Map<ProCtcTerm, String>>();
		Map<Integer, Map<LowLevelTerm, String>> meddraVerbatimMap = new HashMap<Integer, Map<LowLevelTerm, String>>();
		buildAddedSymptomVerbatimMap(request, proVerbatimMap, meddraVerbatimMap);
		
    	for(StudyParticipantCrfScheduleAddedQuestion addedQuestion: spcrfsAddedQuestions) {
    		ParticipantAddedSymptomVerbatimWrapper verbatimWrapper = new ParticipantAddedSymptomVerbatimWrapper();
    		
    		//set patients identifier
    		verbatimWrapper.setStudyParticipantIdentifier(addedQuestion
    													 .getStudyParticipantCrfSchedule()
    													 .getStudyParticipantCrf()
    													 .getStudyParticipantAssignment()
    													 .getStudyParticipantIdentifier());

    		StudyParticipantCrfSchedule studyParticipantCrfSchedule = addedQuestion.getStudyParticipantCrfSchedule();
    		
    		if(addedQuestion.getProCtcQuestion() != null) {
    			if(proVerbatimMap.get(studyParticipantCrfSchedule.getId()) != null) {
    				ProCtcTerm proCtcTerm = addedQuestion.getProCtcQuestion().getProCtcTerm();
    				
    				if(proVerbatimMap.get(studyParticipantCrfSchedule.getId()).get(proCtcTerm) != null) {
    					// if verbatim is recorded for pro term set pro_term and verbatim
    					verbatimWrapper.setProCtcTerm(proCtcTerm);
    					verbatimWrapper.setVerbatim(proVerbatimMap.get(studyParticipantCrfSchedule.getId()).get(proCtcTerm));
    					
    				} else {
    					// if verbatim is not recorded for this pro term for some reason, just set the pro_term
    					verbatimWrapper.setProCtcTerm(proCtcTerm);
    				}
    				
    			} else {
    				// no verbatim is recorded (probably for records before PRKC-2471 feature)
    				verbatimWrapper.setProCtcTerm(addedQuestion.getProCtcQuestion().getProCtcTerm());
    			}
    			
    		} else {
    			if(meddraVerbatimMap.get(studyParticipantCrfSchedule.getId()) != null) {
    				LowLevelTerm lowLevelTerm = addedQuestion.getMeddraQuestion().getLowLevelTerm();
    				
    				if(meddraVerbatimMap.get(studyParticipantCrfSchedule.getId()).get(lowLevelTerm) != null) {
    					// if verbatim is recorded for llt set low level term and verbatim
    					verbatimWrapper.setLowLevelTerm(lowLevelTerm);
    					verbatimWrapper.setVerbatim(meddraVerbatimMap.get(studyParticipantCrfSchedule.getId()).get(lowLevelTerm));
    				} else {
    					// if verbatim is not recorded for some reason for this llt, just set the low level term
    					verbatimWrapper.setLowLevelTerm(lowLevelTerm);
    				}
    			} else {
    				LowLevelTerm lowLevelTerm = addedQuestion.getMeddraQuestion().getLowLevelTerm();
    				
    				// no verbatim is recorded and this llt is not either a participant added free text symptom
    				if(!lowLevelTerm.isParticipantAdded()) {
    					verbatimWrapper.setLowLevelTerm(lowLevelTerm);
    					
    				} else {
    					// no verbatim is recorded as this llt is a participant added free text symptom
    					verbatimWrapper.setVerbatim(lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
    					
    				}
    			}
    		}
    		
    		verbatimWrappers.add(verbatimWrapper);
    	}
    	
    	// retain distinct verbatim set and return as list
    	Set<ParticipantAddedSymptomVerbatimWrapper> verbatimSet = new HashSet<ParticipantAddedSymptomVerbatimWrapper>(verbatimWrappers);
		return new ArrayList<>(verbatimSet);
		
	}
    
    protected void buildAddedSymptomVerbatimMap(HttpServletRequest request,
    		 									Map<Integer, Map<ProCtcTerm, String>> proVerbatimMap,
    		 									Map<Integer, Map<LowLevelTerm, String>> meddraVerbatimMap) throws ParseException {
    	
    	AddedSymptomVerbatimQuery query = new AddedSymptomVerbatimQuery(QueryStrings.ADDED_SYMPTOM_VERBATIM_QUERY_BASIC);
    	parseAddedSymptomVerbatimQuery(request, query);
    	
    	List<AddedSymptomVerbatim> addedSymptomVerbatims = (List<AddedSymptomVerbatim>) addedSymptomVerbatimRepository.find(query);
    	
    	for(AddedSymptomVerbatim verbatim: addedSymptomVerbatims) {
    		Map<ProCtcTerm, String> proVerbatimSubMap;
    		Map<LowLevelTerm, String> meddraVerbatimSubMap;
    		
			if(verbatim.getProctcTerm() != null) {
				if(proVerbatimMap.get(verbatim.getStudyParticipantCrfSchedule().getId()) != null) {
					proVerbatimSubMap = proVerbatimMap.get(verbatim.getStudyParticipantCrfSchedule().getId());
				} else {
					proVerbatimSubMap = new HashMap<ProCtcTerm, String>();
    				proVerbatimMap.put(verbatim.getStudyParticipantCrfSchedule().getId(), proVerbatimSubMap);
				}
				
				proVerbatimSubMap.put(verbatim.getProctcTerm(), verbatim.getVerbatim());
				
			} else {
				if(meddraVerbatimMap.get(verbatim.getStudyParticipantCrfSchedule().getId()) != null) {
					meddraVerbatimSubMap = meddraVerbatimMap.get(verbatim.getStudyParticipantCrfSchedule().getId());
				} else {
					meddraVerbatimSubMap = new HashMap<LowLevelTerm, String>();
					meddraVerbatimMap.put(verbatim.getStudyParticipantCrfSchedule().getId(), meddraVerbatimSubMap);
				}
				
				meddraVerbatimSubMap.put(verbatim.getLowLevelTerm(), verbatim.getVerbatim());
			}
			
    	}
    	
    }

    protected void parseAddedSymptomVerbatimQuery(HttpServletRequest request, AddedSymptomVerbatimQuery query) throws ParseException {
    	String crfParam = request.getParameter("crf");
        String studySiteId = request.getParameter("studySite");
        String studyParam = request.getParameter("study");
        
      	if(StringUtils.isNotBlank(crfParam)) {
      		int crfId = Integer.parseInt(crfParam);
    		CRF crf = genericRepository.findById(CRF.class, crfId);
    		List<Integer> crfIds = new ArrayList<Integer>();
    		crfIds.add(crf.getId());
    		while(crf.getParentCrf()!= null){
    			crfIds.add(crf.getParentCrf().getId());
    			crf = crf.getParentCrf();
    		}
    		query.filterByCrfs(crfIds);
      	} else if(!StringUtils.isBlank(studyParam)){
    		List<CRF> crfList = ReportResultsHelper.getReducedCrfs(Integer.parseInt(studyParam));
    		List<Integer> crfIds = new ArrayList<Integer>();
    		for(CRF crf : crfList){
    			crfIds.add(crf.getId());
    			while(crf.getParentCrf() != null){
    				crfIds.add(crf.getId());
    				crf = crf.getParentCrf();
    			}
    		}
    		if(!crfIds.isEmpty()){
    			query.filterByCrfs(crfIds);
        	} else {
        		query.filterByStudyId(Integer.parseInt(studyParam));
        	}
    	}
      	
      	if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }
      	
	}
    
    protected void parseAndBuildParticipantAddedQuestionsReportQuery(HttpServletRequest request, ParticipantAddedQuestionsReportQuery query) throws ParseException {
    	String crfParam = request.getParameter("crf");
        String studySiteId = request.getParameter("studySite");
        String studyParam = request.getParameter("study");
        
        if(StringUtils.isBlank(studyParam)){
        	studyParam = (String) request.getSession().getAttribute("study");
        } else {
        	request.getSession().setAttribute("study", studyParam);
        }
        
        if(!StringUtils.isBlank(crfParam)){
    		int crfId = Integer.parseInt(crfParam);
    		CRF crf = genericRepository.findById(CRF.class, crfId);
    		List<Integer> crfIds = new ArrayList<Integer>();
    		crfIds.add(crf.getId());
    		while(crf.getParentCrf()!= null){
    			crfIds.add(crf.getParentCrf().getId());
    			crf = crf.getParentCrf();
    		}
    		query.filterByCrfs(crfIds);
			request.getSession().setAttribute("crfTitle", crf.getTitle());
			
    	} else if(!StringUtils.isBlank(studyParam)){
    		List<CRF> crfList = ReportResultsHelper.getReducedCrfs(Integer.parseInt(studyParam));
    		List<Integer> crfIds = new ArrayList<Integer>();
    		for(CRF crf : crfList){
    			crfIds.add(crf.getId());
    			while(crf.getParentCrf() != null){
    				crfIds.add(crf.getId());
    				crf = crf.getParentCrf();
    			}
    		}
    		if(!crfIds.isEmpty()){
    			query.filterByCrfs(crfIds);
        	} else {
        		query.filterByStudyId(Integer.parseInt(studyParam));
        	}
    	}
       
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
            StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, Integer.parseInt(studySiteId));
			request.getSession().setAttribute("organizationName", studyOrganization.getOrganization().getName());
        }
        
        
        if(StringUtils.isNotBlank(studyParam)) {
        	request.getSession().setAttribute("selectedStudy", 
        			genericRepository.findById(Study.class, Integer.parseInt(studyParam)));
        }
        
    }

	@Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
    @Required
    public void setAddedSymptomVerbatimRepository(AddedSymptomVerbatimRepository addedSymptomVerbatimRepository) {
		this.addedSymptomVerbatimRepository = addedSymptomVerbatimRepository;
	}
}