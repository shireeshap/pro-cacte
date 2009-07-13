package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class ParticipantReportExcelController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ParticipantLevelReportExcelView participantLevelReportExcelView = new ParticipantLevelReportExcelView();
        return new ModelAndView(participantLevelReportExcelView);
    }
}