package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 */

public class NotificationDetailsController extends AbstractController {

    GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/notification");
        String id = request.getParameter("id");
        UserNotification notification = genericRepository.findById(UserNotification.class, Integer.parseInt(id));
        notification.setNew(false);
        notification = genericRepository.save(notification);
        modelAndView.addObject("notification", notification);

        return modelAndView;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}