package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.domain.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public abstract class AbstractReportResultsController extends AbstractController {

    protected GenericRepository genericRepository;


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    protected String getTitle(HttpServletRequest request) {
        StringBuffer title = new StringBuffer();
        int symptomId = Integer.parseInt(request.getParameter("symptom"));
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, symptomId);

        String attribute = request.getParameter("att");
        title.append("Worst responses for ").append(proCtcTerm.getTerm()).append(" ").append(attribute);
        return title.toString();
    }

    public final List<Arm> getArms(HttpServletRequest request) {
        int crfId = Integer.parseInt(request.getParameter("crf"));
        CRF crf = genericRepository.findById(CRF.class, crfId);
        Study study = genericRepository.findById(Study.class, crf.getStudy().getId());
        return study.getNonDefaultArms();
    }

    public final void addAllAttributesToModelAndView(HttpServletRequest request, ModelAndView modelAndView) {
        HashSet<String> allAttributes = new HashSet<String>();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }
        modelAndView.addObject("symptom", proCtcTerm.getTerm());
        modelAndView.addObject("allAttributes", allAttributes);
    }

    public final Long getParticipantCount(HttpServletRequest request, Arm arm) throws ParseException {
        ReportParticipantCountQuery cquery = new ReportParticipantCountQuery();
        ReportResultsHelper.parseRequestParametersAndFormQuery(request, cquery);
        cquery.filterByArm(arm);
        List list = genericRepository.find(cquery);
        return (Long) list.get(0);
    }

    public final String getCountString(HttpServletRequest request, HashSet<Integer> selectedArms) throws ParseException {
        String countString = "";
        if (selectedArms.size() > 1) {
            for (Integer armid : selectedArms) {
                Arm arm = genericRepository.findById(Arm.class, armid);
                if (arm != null) {
                    int numOfParticipantsOnArm = getParticipantCount(request, arm).intValue();
                    countString += "\n[" + arm.getTitle() + ": N=" + numOfParticipantsOnArm + "]";
                }
            }
        } else {
            countString = "" + getParticipantCount(request, null).intValue();
        }
        return countString;
    }
}