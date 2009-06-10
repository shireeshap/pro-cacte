package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;


/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ParticipantAjaxFacadeTest extends AbstractWebTestCase {

    public void testSearchParticipantByNameOrIdentfier() {
        Map parameterMap = null;
        Participant p = ParticipantTestHelper.getDefaultParticipant();
        ParticipantAjaxFacade facade = new ParticipantAjaxFacade();
        facade.setParticipantRepository(participantRepository);

        String table = facade.searchParticipant(parameterMap, p.getFirstName().substring(1, 3), p.getLastName().substring(1, 3), p.getAssignedIdentifier(), request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, "", p.getLastName().substring(1, 3), "", request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, p.getFirstName().substring(1, 3), "", "", request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, "", "", p.getAssignedIdentifier(), request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, "XXXXXXXXXXXX", "YYYYYYYYYY", "", request);
        assertNotNull(table);
        assertTrue("must not find participant", !table.contains(p.getFirstName()));
        assertTrue("must not find participant", !table.contains(p.getLastName()));

    }
    public void testMatchParticipantByStudySiteId() {
        Study s = StudyTestHelper.getDefaultStudy();
        StudySite ss = s.getLeadStudySite();
        ParticipantAjaxFacade facade = new ParticipantAjaxFacade();
        facade.setParticipantRepository(participantRepository);

        List<Participant> pl = facade.matchParticipantByStudySiteId("s",null, s.getId());
        assertNotNull(pl);
        assertEquals(4,pl.size());

        pl = facade.matchParticipantByStudySiteId("s",ss.getId(), s.getId());
        assertNotNull(pl);
        assertEquals(2,pl.size());

    }

}
