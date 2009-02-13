package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.core.ListValues;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class SearchParticipantControllerTest extends WebTestCase {
    private SearchParticipantController searchParticipantController;
    private ParticipantCommand participantCommand;
    private ModelAndView modelAndView;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        searchParticipantController = new SearchParticipantController();

    }

    public void testHandleRequestInternal() throws Exception {
        modelAndView = searchParticipantController.handleRequestInternal(request, response);
        assertEquals("participant/searchParticipant",modelAndView.getViewName());
    }



}