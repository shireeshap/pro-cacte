package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mehul
 *         Date: 1/23/12
 */
public class FetchAvailableFormsController extends AbstractController {
    private StudyParticipantCrfScheduleAjaxFacade spcsfacade;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView("home");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        Date todaysDate = DateUtils.getCurrentDate();

        List<StudyParticipantCrfSchedule> availableSchedules = spcsfacade.searchSchedules(Integer.parseInt(startIndex), Integer.parseInt(results), sort, dir, CrfStatus.INPROGRESS, todaysDate);

        Long totalRecords = spcsfacade.resultCount(CrfStatus.INPROGRESS, todaysDate);
        SearchScheduleWrapper searchScheduleWrapper = new SearchScheduleWrapper();
        searchScheduleWrapper.setTotalRecords(totalRecords);
        searchScheduleWrapper.setRecordsReturned(availableSchedules.size());
        searchScheduleWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchScheduleWrapper.setPageSize(25);
        searchScheduleWrapper.setDir("asc");
        searchScheduleWrapper.setSearchScheduleDTOs(new SearchScheduleDTO[availableSchedules.size()]);
        int index = 0;
        for (StudyParticipantCrfSchedule spcs : availableSchedules) {
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
            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='spcsActions"
                    + spcs.getId() + "'"
                    + " onclick=\"javascript:showPopUpMenuSpcs('"
                    + spcs.getId()
                    + "','"
                    + spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getId()
                    + "','"
                    + spcs.getStudyParticipantCrf().getCrf().getTitle()
                    + "');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);
            searchScheduleWrapper.getSearchScheduleDTOs()[index] = dto;
            index++;
        }

        JSONObject jsonObject = JSONObject.fromObject(searchScheduleWrapper);
        Map<String, Object> modelMap = new HashMap();
        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);
        return new ModelAndView("jsonView", modelMap);
    }

    public void setSpcsfacade(StudyParticipantCrfScheduleAjaxFacade spcsfacade) {
        this.spcsfacade = spcsfacade;
    }
}
