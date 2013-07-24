package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AddedMeddraQuestionWrapper;
import gov.nih.nci.ctcae.core.domain.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.domain.ParticipantAndOganizationWrapper;
import gov.nih.nci.ctcae.core.domain.SpcrfsWrapper;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mehul
 * Date: Apr 15, 2011
 */	
public class OverallStudyReportExcelController extends AbstractController {
    private OverallStudyData overallStudyData;
    private StudyWideFormatReportData studyWideFormatReportData;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
//        int id = Integer.parseInt(request.getParameter("id"));
        Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
        Long start = new Date().getTime();
        List<SpcrfsWrapper> listSchedules = studyWideFormatReportData.getSchedulesOnly(2);
        List<ResponsesRowMapper> listResoponses = studyWideFormatReportData.getResponsesOnly(2);
        List<AddedProCtcQuestionWrapper> listAddedProCtcQuestions = studyWideFormatReportData.getAddedProQuestions(2);
        List<AddedMeddraQuestionWrapper> listAddedMeddraQuestions = studyWideFormatReportData.getAddedMeddraQuestions(2);
        List<ParticipantAndOganizationWrapper> listParticipantsAndOrganizations = studyWideFormatReportData.getParticipantsAndOrg(2);
        Long end = new Date().getTime();
        System.out.println("EndTime: " + (end - start));
        request.getSession().setAttribute("list", null);
        //OverallStudyDataExcelView view = new OverallStudyDataExcelView();
        return null; //new ModelAndView(view);
    }

    public OverallStudyData getOverallStudyData() {
        return overallStudyData;
    }

    public void setOverallStudyData(OverallStudyData overallStudyData) {
        this.overallStudyData = overallStudyData;
    }
    
    public StudyWideFormatReportData getStudyWideFormatReportData() {
        return studyWideFormatReportData;
    }

    public void setStudyWideFormatReportData(StudyWideFormatReportData studyWideFormatReportData) {
        this.studyWideFormatReportData = studyWideFormatReportData;
    }
}
