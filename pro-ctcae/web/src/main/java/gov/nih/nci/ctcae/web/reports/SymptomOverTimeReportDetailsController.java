package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeAllResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeAllResponsesQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesDetailsQuery;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.Participant;
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
        String type = request.getParameter("type");
        String col = request.getParameter("col");
        Integer sum = Integer.valueOf(request.getParameter("sum"));
        Integer colInt = Integer.parseInt(col.substring(col.indexOf(' ') + 1)) + sum - 1;
        List<StudyParticipantCrfItem> results = new ArrayList<StudyParticipantCrfItem>();
        if ("WOR".equals(type)) {
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
        } else {
            SymptomOverTimeAllResponsesDetailsQuery query = new SymptomOverTimeAllResponsesDetailsQuery(colInt, group);
            parseRequestParametersAndFormQuery(request, query);
            results = genericRepository.find(query);
        }

        Map model = new HashMap();
        model.put("results", results);
        model.put("group", group);
        ModelAndView modelAndView = new ModelAndView("reports/reportDetails", model);
        return modelAndView;
    }

}