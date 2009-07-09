package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.StudyParticipantCrfItemQuery;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class GetParticipantCrfItemsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<StudyParticipantCrfItem> results = new ArrayList<StudyParticipantCrfItem>();
        Integer pId = Integer.valueOf(request.getParameter("pid"));
        Integer grade = Integer.parseInt(request.getParameter("grade"));
        String group = request.getParameter("groupby");
        StudyParticipantCrfItemQuery query = new StudyParticipantCrfItemQuery();
        parseRequestParametersAndFormQuery(request, query);
        query.filterByParticipantId(pId);
        query.filterByResponse(grade);
        if (!StringUtils.isBlank(group)) {
            String period = group.substring(0, group.indexOf(' '));
            String periodValue = group.substring(group.indexOf(' ') + 1);
            query.filterByPeriod(period, Integer.parseInt(periodValue));
        }
        List list = genericRepository.find(query);
        results.addAll(list);
        Map model = new HashMap();
        model.put("results", results);
        model.put("pid", pId);
        return new ModelAndView("reports/reportDetails_participantItems", model);
    }
}