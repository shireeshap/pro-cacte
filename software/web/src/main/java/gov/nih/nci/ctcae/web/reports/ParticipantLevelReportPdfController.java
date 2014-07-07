package gov.nih.nci.ctcae.web.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class ParticipantLevelReportPdfController extends AbstractController {
	 private static String CTCAE_GRADES = "ctcaeGrades";
	 
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv;
        String reportType = request.getParameter("rt");
        if (reportType != null && "worstSymptom".equals(reportType)) {
            ParticipantLevelWorstSymptomReportPdfView worstSymptomReportPdfView = new ParticipantLevelWorstSymptomReportPdfView();
            mv = new ModelAndView(worstSymptomReportPdfView);
            
        } else if(reportType != null && CTCAE_GRADES.equals(reportType)){
        	ParticipantLevelCtcaeGradesReportPdfView participantLevelCtcaeGradesReportPdfView = new ParticipantLevelCtcaeGradesReportPdfView();
        	mv = new ModelAndView(participantLevelCtcaeGradesReportPdfView);
        	
        } else {
            ParticipantLevelReportPdfView participantLevelReportPdfView = new ParticipantLevelReportPdfView();
            mv = new ModelAndView(participantLevelReportPdfView);
        }
        return mv;
    }
}
