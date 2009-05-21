package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.SymptomSummaryReportDetailsQuery;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportDetailsController extends SymptomSummaryReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SymptomSummaryReportDetailsQuery query = new SymptomSummaryReportDetailsQuery();
        parseRequestParametersAndFormQuery(request, query);
        List results = genericRepository.find(query);
        Map model = new HashMap();
        model.put("results", results);
        model.put("response", request.getParameter("response"));
        ModelAndView modelAndView = new ModelAndView("reports/symptomSummaryDetails", model);
        return modelAndView;
    }
}