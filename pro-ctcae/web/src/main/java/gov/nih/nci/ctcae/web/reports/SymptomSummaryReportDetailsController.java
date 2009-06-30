package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.SymptomSummaryAllResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.query.SymptomSummaryWorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeAllResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportDetailsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<StudyParticipantCrfItem> results = new ArrayList<StudyParticipantCrfItem>();
        String type = request.getParameter("type");
        Integer value = Integer.parseInt(request.getParameter("col"));
        if ("WOR".equals(type)) {
            SymptomSummaryWorstResponsesDetailsQuery query = new SymptomSummaryWorstResponsesDetailsQuery();
            parseRequestParametersAndFormQuery(request, query);
            List list = genericRepository.find(query);
            for (Object o : list) {
                Object[] oArr = (Object[]) o;
                if (((Integer)oArr[0]).intValue() == value.intValue()) {
                    SymptomSummaryAllResponsesDetailsQuery temp = new SymptomSummaryAllResponsesDetailsQuery();
                    parseRequestParametersAndFormQuery(request, temp);
                    temp.filterByParticipantId((Integer) oArr[1]);
                    temp.filterByResponse((Integer) oArr[0]);
                    List l = genericRepository.find(temp);
                    results.addAll(l);
                }
            }
        } else {
            SymptomSummaryAllResponsesDetailsQuery query = new SymptomSummaryAllResponsesDetailsQuery();
            query.filterByResponse(value);
            parseRequestParametersAndFormQuery(request, query);
            results = genericRepository.find(query);

        }
        Map model = new HashMap();
        model.put("results", results);
        ModelAndView modelAndView = new ModelAndView("reports/reportDetails", model);
        return modelAndView;
    }
}