package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.SymptomSummaryReportQuery;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.*;
import java.io.PrintWriter;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportController extends AbstractController {

    private GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SymptomSummaryReportQuery query = new SymptomSummaryReportQuery();

        query.filterByCrf(Integer.parseInt(request.getParameter("crfId")));
        query.filterBySymptomId(Integer.parseInt(request.getParameter("symptom")));
        query.filterByAttribute(ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));
        String dateRange = "All";
        String gender = request.getParameter("gender");
        if (!StringUtils.isBlank(gender) && !"all".equals(gender.toLowerCase())) {
            query.filterByParticipantGender(gender);
        }

        String studySiteId = request.getParameter("studySiteId");
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        String visitRange = request.getParameter("visitRange");
        if (!StringUtils.isBlank(visitRange) && "dateRange".equals(visitRange)) {
            Date startDate = DateUtils.parseDate(request.getParameter("startDate"));
            Date endDate = DateUtils.parseDate(request.getParameter("endDate"));
            dateRange = request.getParameter("startDate") + " - " + request.getParameter("endDate");
            query.filterByScheduleStartDate(startDate, endDate);
        }

        List results = genericRepository.find(query);

        results = addEmptyValues(results, ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));

        SymptomSummaryChartGenerator chartGenerator = new SymptomSummaryChartGenerator();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        JFreeChart chart = chartGenerator.getChart(results, proCtcTerm.getTerm(), request.getParameter("attribute"), dateRange);

        //  Write the chart image to the temporary directory
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String filename = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);

        String imageMap = ChartUtilities.getImageMap(filename, info);

        Map model = new HashMap();
        model.put("filename", filename);
        model.put("imagemap", imageMap);
        ModelAndView modelAndView = new ModelAndView("reports/bar_chart", model);
        return modelAndView;
    }

    private List addEmptyValues(List results, ProCtcQuestionType qType) {
        List newResults = new ArrayList();
        for (String vv : qType.getValidValues()) {
            boolean found = false;
            for (Object o : results) {
                Object[] obj = (Object[]) o;
                if (obj[1].equals(vv)) {
                    found = true;
                    newResults.add(o);
                    break;
                }
            }
            if (!found) {
                newResults.add(new Object[]{0L, vv});
            }
        }
        return newResults;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
