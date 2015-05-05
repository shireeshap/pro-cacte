package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.WorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class ReportDetailsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<Integer, Integer> participants = new HashMap<Integer, Integer>();
        TreeMap<Participant, Integer> results = new TreeMap<Participant, Integer>();

        WorstResponsesDetailsQuery query = new WorstResponsesDetailsQuery();
        ReportResultsHelper.parseRequestParametersAndFormQuery(request, query);
        List list = genericRepository.find(query);
        String gradeIn = request.getParameter("grade");
        Integer gradeInt = -1;
        if (!StringUtils.isBlank(gradeIn)) {
            gradeInt = Integer.parseInt(gradeIn);
        }
        for (Object o : list) {
            Object[] oArr = (Object[]) o;
            int grade = (Integer) oArr[0];
            if (gradeInt == -1 || gradeInt == grade) {
                int pId = (Integer) oArr[1];
                participants.put(pId, grade);
            }
        }

        for (Integer pId : participants.keySet()) {
            Integer grade = participants.get(pId);
            Participant p = genericRepository.findById(Participant.class, pId);
            results.put(p, grade);
        }

        String title = getTitle(request);
        String period = request.getParameter("period");
        if (!StringUtils.isBlank(period)) {
            title += " (" + period + ")";
        }

        Map model = new HashMap();
        model.put("results", results);
        model.put("att", request.getParameter("att"));
        model.put("period", period);
        model.put("grade", request.getParameter("grade"));
        model.put("sum", request.getParameter("sum"));
        model.put("title", title);
        if (!StringUtils.isBlank(period)) {
            model.put("group", period.substring(0, period.indexOf(" ")).toLowerCase());
        }
        model.put("arms", request.getParameter("arms"));
        return new ModelAndView("reports/reportDetails", model);
    }

}