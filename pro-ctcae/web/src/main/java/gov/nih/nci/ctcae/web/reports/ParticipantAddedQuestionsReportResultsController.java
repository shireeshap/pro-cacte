package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * User: Harsh
 * Date: Jun 2, 2009
 * Time: 8:01:32 AM
 */
public class ParticipantAddedQuestionsReportResultsController extends AbstractController {
    GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantAddedQuestionsResults");

        ParticipantAddedQuestionsReportQuery query = new ParticipantAddedQuestionsReportQuery();
        parseRequestParametersAndFormQuery(request, query);

        List result = genericRepository.find(query);
        modelAndView.addObject("results", result);
        return modelAndView;
    }

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, ParticipantAddedQuestionsReportQuery query) throws ParseException {
        int crfId = Integer.parseInt(request.getParameter("crfId"));
        String studySiteId = request.getParameter("studySiteId");
        String symptom = request.getParameter("symptom");

        query.filterByCrf(crfId);
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        if (!StringUtils.isBlank(symptom)) {
            query.filterBySymptom(symptom);
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}