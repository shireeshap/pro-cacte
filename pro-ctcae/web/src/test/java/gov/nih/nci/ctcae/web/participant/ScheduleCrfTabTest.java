package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @since Nov 24, 2008
 */
public class ScheduleCrfTabTest extends WebTestCase {
    private ScheduleCrfTab tab;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tab = new ScheduleCrfTab();


    }

    public void testConstructor() {
        assertNotNull(tab);
    }

    public void testTab() {
        assertEquals(Privilege.PRIVILEGE_PARTICIPANT_SCHEDULE_CRF, tab.getRequiredPrivilege());
        CRFRepository crfRepository = registerMockFor(CRFRepository.class);
        StudyParticipantAssignment a = new StudyParticipantAssignment();
        List<StudyParticipantCrf> l = new ArrayList<StudyParticipantCrf>();
        StudyParticipantCrf s = new StudyParticipantCrf();
        CRF c = new CRF();
        c.setId(1);
        s.setCrf(c);
        l.add(s);
        a.addStudyParticipantCrf(s);
        StudyParticipantCommand command = new StudyParticipantCommand();
        command.setStudyParticipantAssignment(a);

        tab.setCrfRepository(crfRepository);
        expect(crfRepository.findById(1)).andReturn(c);
        replayMocks();
        tab.onDisplay(request, command);
        verifyMocks();

        Map<String, Object> m =  tab.referenceData(command);
        assertNotNull(m.get("repetitionunits"));
        assertNotNull(m.get("duedateunits"));
        assertNotNull(m.get("repeatuntilunits"));
        assertNotNull(m.get("cyclelengthunits"));

        
    }


}