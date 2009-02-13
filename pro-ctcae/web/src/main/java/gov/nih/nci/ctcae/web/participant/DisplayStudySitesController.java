package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//
/**
 * @author Harsh Agarwal
 * @created Feb 12, 2008
 */
public class DisplayStudySitesController extends AbstractController {

    /**
     * The finder repository.
     */
    FinderRepository finderRepository;


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaystudysites");
        Integer organizationId = Integer.parseInt(request.getParameter("organizationId"));
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByOrganizationId(organizationId);
        query.filterByStudySiteOnly();
        List<StudySite> studySites = (List<StudySite>) finderRepository.find(query);

        modelAndView.addObject("studysites", studySites);

        return modelAndView;
    }


    public DisplayStudySitesController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}