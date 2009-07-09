package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.WorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomOverTimeReportDetailsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<Integer, Integer> participants = new HashMap<Integer, Integer>();
        HashMap<Participant, Integer> results = new HashMap<Participant, Integer>();
        String group = request.getParameter("group");
        if (StringUtils.isBlank(group)) {
            group = "cycle";
        }
        String col = request.getParameter("col");
        Integer sum = Integer.valueOf(request.getParameter("sum"));
        Integer colInt = Integer.parseInt(col.substring(col.indexOf(' ') + 1)) + sum - 1;
        WorstResponsesDetailsQuery query = new WorstResponsesDetailsQuery();
        parseRequestParametersAndFormQuery(request, query);
        query.filterByPeriod(group, colInt);
        List list = genericRepository.find(query);
        for (Object o : list) {
            Object[] oArr = (Object[]) o;
            int grade = (Integer) oArr[0];
            int pId = (Integer) oArr[1];
            participants.put(pId, grade);
        }
        for (Integer pId : participants.keySet()) {
            Integer grade = participants.get(pId);
            Participant p = genericRepository.findById(Participant.class, pId);
            results.put(p, grade);
        }
        String title = getTitle(request);
        title += " (" + col + ")";

        Map model = new HashMap();
        model.put("results", results);
        model.put("ser", request.getParameter("ser"));
        model.put("group", col);
        model.put("title", title);
        return new ModelAndView("reports/reportDetails", model);
    }

}