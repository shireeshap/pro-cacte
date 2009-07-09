package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.WorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportDetailsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<Integer, Integer> participants = new HashMap<Integer, Integer>();
        HashMap<Participant, Integer> results = new HashMap<Participant, Integer>();

        Integer value = Integer.parseInt(request.getParameter("col"));
        WorstResponsesDetailsQuery query = new WorstResponsesDetailsQuery();
        parseRequestParametersAndFormQuery(request, query);
        List list = genericRepository.find(query);
        for (Object o : list) {
            Object[] oArr = (Object[]) o;
            int grade = (Integer) oArr[0];
            if (grade == value) {
                int pId = (Integer) oArr[1];
                participants.put(pId, grade);
            }
        }
        for (Integer pId : participants.keySet()) {
            Integer grade = participants.get(pId);
            Participant p = genericRepository.findById(Participant.class, pId);
            results.put(p, grade);
        }

        Map model = new HashMap();
        model.put("results", results);
        model.put("col", value);
        model.put("ser", request.getParameter("ser"));
        model.put("title", getTitle(request));
        return new ModelAndView("reports/reportDetails", model);
    }
}