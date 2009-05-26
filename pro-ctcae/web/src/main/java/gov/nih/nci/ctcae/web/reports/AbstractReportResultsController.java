package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.query.SymptomSummaryReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public abstract class AbstractReportResultsController extends AbstractController {

    protected GenericRepository genericRepository;

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

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}