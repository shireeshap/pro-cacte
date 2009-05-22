package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.SymptomSummaryReportQuery;
import gov.nih.nci.ctcae.core.query.SymptomSummaryParticipantCountQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportResultsController extends AbstractController {

    protected GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String dateRange = "All";
        if (!StringUtils.isBlank(request.getParameter("startDate"))) {
            dateRange = request.getParameter("startDate") + " - " + request.getParameter("endDate");
        }
        SymptomSummaryReportQuery query = new SymptomSummaryReportQuery();
        parseRequestParametersAndFormQuery(request, query);
        List results = genericRepository.find(query);

        SymptomSummaryParticipantCountQuery query1 = new SymptomSummaryParticipantCountQuery();
        parseRequestParametersAndFormQuery(request, query1);
        List list = genericRepository.find(query1);
        Long l = (Long) list.get(0);

        results = addEmptyValues(results, ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));


        SymptomSummaryChartGenerator
                chartGenerator = new SymptomSummaryChartGenerator();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        JFreeChart chart = chartGenerator.getChart(results, proCtcTerm.getTerm(), request.getParameter("attribute"), dateRange, request.getQueryString(), l);

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

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, SymptomSummaryReportQuery query) throws ParseException {
        int crfId = Integer.parseInt(request.getParameter("crfId"));
        int symptomId = Integer.parseInt(request.getParameter("symptom"));
        String attributeName = request.getParameter("attribute");
        String gender = request.getParameter("gender");
        String studySiteId = request.getParameter("studySiteId");
        String visitRange = request.getParameter("visitRange");

        query.filterByCrf(crfId);
        query.filterBySymptomId(symptomId);
        query.filterByAttribute(ProCtcQuestionType.getByDisplayName(attributeName));
        if (!StringUtils.isBlank(gender) && !"all".equals(gender.toLowerCase())) {
            query.filterByParticipantGender(gender);
        }
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        if (!StringUtils.isBlank(visitRange) && "dateRange".equals(visitRange)) {
            Date startDate = DateUtils.parseDate(request.getParameter("startDate"));
            Date endDate = DateUtils.parseDate(request.getParameter("endDate"));
            query.filterByScheduleStartDate(startDate, endDate);
        }

        if (!StringUtils.isBlank(request.getParameter("response"))) {
            query.filterByResponse(request.getParameter("response"));
        }
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

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
