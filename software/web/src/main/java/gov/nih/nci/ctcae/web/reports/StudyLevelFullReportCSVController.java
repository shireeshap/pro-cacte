package gov.nih.nci.ctcae.web.reports;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelFullReportCSVController extends AbstractController {
	
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map model = null;
        StudyLevelFullReportCsvView view = new StudyLevelFullReportCsvView();
        return new ModelAndView(view);
    }
    
}