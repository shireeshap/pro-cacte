package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfItemQuery;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        StudyParticipantCrfItemQuery query = new StudyParticipantCrfItemQuery();
        ReportResultsHelper.parseRequestParametersAndFormQuery(request, query);
        query.filterByParticipantId(pId);
        query.filterByResponse(grade);
        List list = genericRepository.find(query);
        results.addAll(list);
        Map model = new HashMap();
        model.put("results", results);
        model.put("pid", pId);
        return new ModelAndView("reports/reportDetails_participantItems", model);
    }
}