package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.query.SymptomOverTimeReportQuery;
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
public class SymptomOverTimeReportDetailsController extends SymptomSummaryReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SymptomOverTimeReportQuery query = new SymptomOverTimeReportQuery();
        parseRequestParametersAndFormQuery(request, query);
        List results = genericRepository.find(query);
        String group = request.getParameter("group");
        if (StringUtils.isBlank(group)) {
            group = "week";
        }

        Map model = new HashMap();
        model.put("results", transformData(results, request.getParameter("cat"), group));
        model.put("group", group);
        ModelAndView modelAndView = new ModelAndView("reports/symptomOverTimeDetails", model);
        return modelAndView;
    }

    private ArrayList<StudyParticipantCrfItem> transformData(List results, String inCat, String group) {
        ArrayList<StudyParticipantCrfItem> out = new ArrayList<StudyParticipantCrfItem>();
        Calendar c = Calendar.getInstance();
        int inCatInt = Integer.parseInt(inCat);
        for (Object obj : results) {
            StudyParticipantCrfItem item = (StudyParticipantCrfItem) obj;
            Date startDate = item.getStudyParticipantCrfSchedule().getStartDate();

            c.setTime(startDate);
            int cat = 0;
            if (group.equals("week")) {
                cat = c.get(Calendar.WEEK_OF_YEAR);
            }
            if (group.equals("month")) {
                cat = c.get(Calendar.MONTH);
            }
            if (cat == inCatInt) {
                out.add(item);
            }
        }

        return out;
    }
}