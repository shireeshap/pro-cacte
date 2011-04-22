package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mehul
 * Date: Apr 15, 2011
 */
public class OverallStudyReportExcelController extends AbstractController {
    private OverallStudyData overallStudyData;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
//        int id = Integer.parseInt(request.getParameter("id"));
        Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
        List<Object[]> list = overallStudyData.getStudyData(id);
        request.getSession().setAttribute("list", list);
        OverallStudyDataExcelView view = new OverallStudyDataExcelView();
        return new ModelAndView(view);
    }

    public OverallStudyData getOverallStudyData() {
        return overallStudyData;
    }

    public void setOverallStudyData(OverallStudyData overallStudyData) {
        this.overallStudyData = overallStudyData;
    }
}
