package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//
/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchParticipantController extends AbstractController {


    private ParticipantAjaxFacade participantAjaxFacade;
    StudyRepository studyRepository;
    OrganizationRepository organizationRepository;
    protected Properties proCtcAEProperties;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");
        String searchString = request.getParameter("searchString");
        modelAndView.addObject("searchString",searchString);
        request.getSession().setAttribute("ParticipantSearchString", searchString);
        return modelAndView;
    }

    @Required
    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }
}
