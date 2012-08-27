package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.user.SearchNotificationDTO;
import gov.nih.nci.ctcae.web.user.SearchNotificationWrapper;
import gov.nih.nci.ctcae.web.user.UserAjaxFacade;
import net.sf.json.JSONObject;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mehul
 * Date: 2/8/12
 */

public class FetchAlertsController extends AbstractController {
      private UserAjaxFacade userAjaxFacade;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView("home");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        List<UserNotification> notifications = userAjaxFacade.searchNotifications(Integer.parseInt(startIndex), Integer.parseInt(results), sort, dir, userName);
        Long totalRecords = userAjaxFacade.resultCount(userName);

        SearchNotificationWrapper searchNotificationWrapper = new SearchNotificationWrapper();
        searchNotificationWrapper.setTotalRecords(totalRecords);
        searchNotificationWrapper.setRecordsReturned(25);
        searchNotificationWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchNotificationWrapper.setPageSize(25);
        searchNotificationWrapper.setDir("asc");
        searchNotificationWrapper.setSearchNotificationDTOs(new SearchNotificationDTO[notifications.size()]);
        int index = 0;
        for (UserNotification userNotification : notifications) {
            SearchNotificationDTO dto = new SearchNotificationDTO();
            dto.setDate(DateUtils.format(userNotification.getNotification().getDate()));
            dto.setParticipantName(userNotification.getParticipant().getDisplayName());
            dto.setStudyTitle(userNotification.getStudy().getShortTitle());

             String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='alertActions"
                    + userNotification.getId() + "'"
                    + " onclick=\"javascript:showPopUpMenuAlerts('"
                    + userNotification.getId()
                    + "','"
                    + userNotification.getStudyParticipantCrfSchedule().getId()
                    + "','"
                    + userNotification.getUuid()
                    + "','"
                    + userNotification.getParticipant().getId()
                    + "');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);
            searchNotificationWrapper.getSearchNotificationDTOs()[index] = dto;
            index++;
        }
        JSONObject jsonObject = JSONObject.fromObject(searchNotificationWrapper);
        Map<String, Object> modelMap = new HashMap();
        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);
        return new ModelAndView("jsonView", modelMap);
    }

    public void setUserAjaxFacade(UserAjaxFacade userAjaxFacade) {
        this.userAjaxFacade = userAjaxFacade;
    }
}
