package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.query.SymptomSummaryReportQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfAddedQuestionRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * User: Harsh
 * Date: Jun 2, 2009
 * Time: 8:01:32 AM
 */
public class ParticipantAddedQuestionsResultsReportResultsController extends AbstractController {
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

        query.filterByCrf(crfId);
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}