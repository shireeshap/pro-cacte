package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.List;
import java.util.Map;


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

        String table = facade.searchParticipant(parameterMap, p.getFirstName(), p.getLastName(), p.getAssignedIdentifier(), request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, "", p.getLastName(), "", request);
        assertNotNull(table);
        assertTrue("must find participant", table.contains(p.getFirstName()));
        assertTrue("must find participant", table.contains(p.getLastName()));

        table = facade.searchParticipant(parameterMap, p.getFirstName(), "", "", request);
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
        assertEquals(9,pl.size());

        pl = facade.matchParticipantByStudySiteId("s",ss.getId(), s.getId());
        assertNotNull(pl);
        assertEquals(5,pl.size());

    }

}
