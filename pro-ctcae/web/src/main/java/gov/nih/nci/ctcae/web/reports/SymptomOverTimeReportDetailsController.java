package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.query.SymptomOverTimeReportQuery;
import org.springframework.web.servlet.ModelAndView;

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


        Map model = new HashMap();
        model.put("results", transformData(results, request.getParameter("week")));
        ModelAndView modelAndView = new ModelAndView("reports/symptomOverTimeDetails", model);
        return modelAndView;
    }

    private ArrayList<StudyParticipantCrfItem> transformData(List results, String inWeek) {
        ArrayList<StudyParticipantCrfItem> out = new ArrayList<StudyParticipantCrfItem>();
        Calendar c = Calendar.getInstance();
        int inWeekInt = Integer.parseInt(inWeek);
        for (Object obj : results) {
            StudyParticipantCrfItem item = (StudyParticipantCrfItem) obj;
            Date startDate = item.getStudyParticipantCrfSchedule().getStartDate();

            c.setTime(startDate);

            int week = c.get(Calendar.WEEK_OF_YEAR);
            if (week == inWeekInt) {
                out.add(item);
            }
        }

        return out;
    }
}