package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mehul
 *         Date: 1/26/12
 */
public class FetchOverdueFormsController extends AbstractController {
    private StudyParticipantCrfScheduleAjaxFacade spcsFacade;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView("home");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        Date current = new Date();
        List<StudyParticipantCrfSchedule> overdueSchedules = spcsFacade.searchSchedules(Integer.parseInt(startIndex), Integer.parseInt(results), sort, dir, CrfStatus.PASTDUE, current);
        Long totalRecords = spcsFacade.resultCount(CrfStatus.PASTDUE, current);
        SearchScheduleWrapper searchOverdueScheduleWrapper = new SearchScheduleWrapper();
        searchOverdueScheduleWrapper.setTotalRecords(totalRecords);
        searchOverdueScheduleWrapper.setRecordsReturned(25);
        searchOverdueScheduleWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchOverdueScheduleWrapper.setPageSize(25);
        searchOverdueScheduleWrapper.setDir("asc");
        searchOverdueScheduleWrapper.setSearchScheduleDTOs(new SearchScheduleDTO[overdueSchedules.size()]);
        int index = 0;
        for (StudyParticipantCrfSchedule spcs : overdueSchedules) {
            SearchScheduleDTO dto = new SearchScheduleDTO();
            dto.setStatus(spcs.getStatus());
            if (spcs.getDueDate() != null) {
                dto.setDueDate(DateUtils.format(spcs.getDueDate()));
            }
            if (spcs.getStartDate() != null) {
                dto.setStartDate(DateUtils.format(spcs.getStartDate()));
            }
            dto.setFormTitle(spcs.getStudyParticipantCrf().getCrf().getTitle());
            dto.setStudyTitle(spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy().getShortTitle());
            dto.setParticipantName(spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName());
            searchOverdueScheduleWrapper.getSearchScheduleDTOs()[index] = dto;
            index++;
        }
        JSONObject jsonObject = JSONObject.fromObject(searchOverdueScheduleWrapper);
        Map<String, Object> modelMap = new HashMap();
        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);
        return new ModelAndView("jsonView", modelMap);
    }

    public void setSpcsFacade(StudyParticipantCrfScheduleAjaxFacade spcsFacade) {
        this.spcsFacade = spcsFacade;
    }
}

