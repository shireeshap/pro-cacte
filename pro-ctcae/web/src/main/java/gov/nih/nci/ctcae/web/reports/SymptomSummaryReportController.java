package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.SymptomSummaryReportQuery;

import java.util.*;

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
        String gender = request.getParameter("gender");
        if (!StringUtils.isBlank(gender) && !"all".equals(gender.toLowerCase())) {
            query.filterByParticipantGender(gender);
        }
        query.filterByAttribute(ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));

        List results = genericRepository.find(query);

        addEmptyValues(results, ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));

        SymptomSummaryChartGenerator chartGenerator = new SymptomSummaryChartGenerator();
        response.setContentType("image/png");
        JFreeChart chart = chartGenerator.getChart(results);
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 700, 450);
        response.getOutputStream().close();
        return null;
    }

    private void addEmptyValues(List results, ProCtcQuestionType qType) {

        for (String vv : qType.getValidValues()) {
            boolean found = false;
            for (Object o : results) {
                Object[] obj = (Object[]) o;
                if (obj[1].equals(vv)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                results.add(new Object[]{0L, vv});
            }
        }
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
