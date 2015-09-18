package gov.nih.nci.ctcae.web.reports;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Amey
 * ParticipantAddedQuestionsCsvController  class.
 * Controller for generating export for participant added symptoms report.
 */
public class ParticipantAddedQuestionsCsvController extends AbstractController {
	
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map model = null;
        ParticipantAddedQuestionsReportCsvView view = new ParticipantAddedQuestionsReportCsvView();
        return new ModelAndView(view);
    }
    
}