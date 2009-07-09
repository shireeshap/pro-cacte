package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public abstract class AbstractReportResultsController extends AbstractController {

    protected GenericRepository genericRepository;

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, AbstractReportQuery query) throws ParseException {
        int crfId = Integer.parseInt(request.getParameter("crfId"));
        int symptomId = Integer.parseInt(request.getParameter("symptom"));
        String attributes = request.getParameter("attributes");
        String studySiteId = request.getParameter("studySiteId");
        String visitRange = request.getParameter("visitRange");
        String series = request.getParameter("ser");
        if (!StringUtils.isBlank(series)) {
            attributes = series;
        }
        query.filterByCrf(crfId);
        if (symptomId != -1) {
            query.filterBySymptomId(symptomId);
        }
        if (!StringUtils.isBlank(attributes)) {
            HashSet<ProCtcQuestionType> qT = new HashSet<ProCtcQuestionType>();
            StringTokenizer st = new StringTokenizer(attributes, ",");
            while (st.hasMoreTokens()) {
                String a = st.nextToken();
                if (!StringUtils.isBlank(a)) {
                    qT.add(ProCtcQuestionType.getByDisplayName(a));
                }
            }
            query.filterByAttributes(qT);
        }
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        if (!StringUtils.isBlank(visitRange) && "dateRange".equals(visitRange)) {
            Date startDate = DateUtils.parseDate(request.getParameter("startDate"));
            Date endDate = DateUtils.parseDate(request.getParameter("endDate"));
            query.filterByScheduleStartDate(startDate, endDate);
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    protected String getTitle(HttpServletRequest request) {
        StringBuffer title = new StringBuffer();
        int symptomId = Integer.parseInt(request.getParameter("symptom"));
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, symptomId);

        String attribute = request.getParameter("ser");
        title.append("Worst responses for " + proCtcTerm.getTerm() + " " + attribute);
        return title.toString();
    }

}