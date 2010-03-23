package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.List;


/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ParticipantAjaxFacadeTest extends AbstractWebTestCase {

   
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
