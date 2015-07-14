package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.query.UserNotificationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Harsh
 * Date: Jun 24, 2009
 * Time: 2:45:24 PM
 */
public class RemoveAlertController extends AbstractController {

    private GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uuid = request.getParameter("uuid");
        UserNotificationQuery query = new UserNotificationQuery();
        query.filterByUUID(uuid);
        Persistable persistable = genericRepository.findSingle(query);
        if (persistable != null) {
            UserNotification un = (UserNotification) persistable;
            un.setMarkDelete(true);
            genericRepository.save(un);
        }
        return new ModelAndView(new RedirectView("/proctcae/pages/home"));
    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
