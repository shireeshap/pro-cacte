package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeAllResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
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

        String group = request.getParameter("group");
        if (StringUtils.isBlank(group)) {
            group = "week";
        }
        String col = request.getParameter("col");
        Integer sum = Integer.valueOf(request.getParameter("sum"));
        Integer colInt = Integer.parseInt(col.substring(col.indexOf(' ') + 1)) + sum - 1;
        List<StudyParticipantCrfItem> results = new ArrayList<StudyParticipantCrfItem>();
        SymptomOverTimeWorstResponsesDetailsQuery query = new SymptomOverTimeWorstResponsesDetailsQuery(colInt, group);
        parseRequestParametersAndFormQuery(request, query);
        List list = genericRepository.find(query);
        for (Object o : list) {
            Object[] oArr = (Object[]) o;
            SymptomOverTimeAllResponsesDetailsQuery temp = new SymptomOverTimeAllResponsesDetailsQuery(colInt, group);
            parseRequestParametersAndFormQuery(request, temp);
            temp.filterByParticipantId((Integer) oArr[1]);
            temp.filterByResponse((Integer) oArr[0]);
            List l = genericRepository.find(temp);
            results.addAll(l);
        }


        Map model = new HashMap();
        model.put("results", results);
        model.put("group", group);
        return new ModelAndView("reports/reportDetails", model);
    }

}