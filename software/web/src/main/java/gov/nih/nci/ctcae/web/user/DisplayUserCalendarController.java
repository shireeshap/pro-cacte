package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mehul gulati
 *         Date: 5/18/12
 */

public class DisplayUserCalendarController extends AbstractController {
    private StudyParticipantCrfScheduleRepository spcsRepository;
    private ClinicalStaffRepository csRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        UserCalendarCommand userCalendarCommand = (UserCalendarCommand) request.getSession().getAttribute("userCalendarCommandObj");
        if (userCalendarCommand == null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userCalendarCommand = new UserCalendarCommand();
            userCalendarCommand.setUser(user);
            if (!user.isAdmin()) {
                ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
                clinicalStaffQuery.filterByUserName(user.getUsername());
                ClinicalStaff cs = csRepository.findSingle(clinicalStaffQuery);
                List<Integer> orgIds = new ArrayList<Integer>();
                if (cs != null) {
                    userCalendarCommand.setClinicalStaff(cs);
                    for (OrganizationClinicalStaff ocs : cs.getOrganizationClinicalStaffs()) {
                        for (StudyOrganizationClinicalStaff socs : ocs.getStudyOrganizationClinicalStaff()) {
                            if ("LEAD_CRA".equals(socs.getRole().getDisplayName())){
                                 userCalendarCommand.setLcra(true);
                            }
                            if ("SITE_CRA".equals(socs.getRole().getDisplayName())||"NURSE".equals(socs.getRole().getDisplayName())||"SITE_PI".equals(socs.getRole().getDisplayName())) {
                                userCalendarCommand.setScra(true);
                            }
                        }
                        orgIds.add(ocs.getOrganization().getId());
                    }
                    userCalendarCommand.setOrganizationIds(orgIds);
                }
            }
            userCalendarCommand.setProCtcAECalendar(new ProCtcAECalendar());
            userCalendarCommand.setSpcsRepository(spcsRepository);
        }
        ModelAndView modelAndView = new ModelAndView("user/ajax/displayUserCalendar");
        String direction = request.getParameter("dir");
        ProCtcAECalendar proCtcAECalendar = userCalendarCommand.getProCtcAECalendar();
        if (direction.equals("prev")) {
            proCtcAECalendar.add(-1);
            userCalendarCommand.setProCtcAECalendar(proCtcAECalendar);
        }
        if (direction.equals("next")) {
            proCtcAECalendar.add(1);
            userCalendarCommand.setProCtcAECalendar(proCtcAECalendar);
        }
        if (direction.equals("refresh")) {
            userCalendarCommand.getProCtcAECalendar().add(0);
        }
        userCalendarCommand.createCurrentMonthScheduleMap();
        modelAndView.addObject("userCalendarCommand", userCalendarCommand);
        request.getSession().setAttribute("userCalendarCommandObj", userCalendarCommand);
        return modelAndView;
    }

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }

    public void setCsRepository(ClinicalStaffRepository csRepository) {
        this.csRepository = csRepository;
    }
}
