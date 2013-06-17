package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * User: Harsh
 * Date: Jun 2, 2009
 * Time: 8:01:32 AM
 */
public class ParticipantAddedQuestionsReportResultsController extends AbstractController {
    GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantAddedQuestionsResults");

        ParticipantAddedQuestionsReportQuery query = new ParticipantAddedQuestionsReportQuery();
        parseRequestParametersAndFormQuery(request, query);
        
        List result = genericRepository.find(query);
        modelAndView.addObject("results", result);
        return modelAndView;
    }

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, ParticipantAddedQuestionsReportQuery query) throws ParseException {
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
    			while(crf.getParentCrf() != null){
    				crfIds.add(crf.getId());
    				crf = crf.getParentCrf();
    			}
    		}
    		query.filterByCrfs(crfIds);
    	}
       
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        if (!StringUtils.isBlank(symptom)) {
            query.filterBySymptom(symptom);
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}