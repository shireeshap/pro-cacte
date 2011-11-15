package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;

/**
 * @author Harsh Agarwal
 * @since Nov 24, 2008
 */
public class ScheduleCrfTabTest extends WebTestCase {
    private ScheduleCrfTab tab;
    private GenericRepository genericRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        genericRepository = registerMockFor(GenericRepository.class);
        tab = new ScheduleCrfTab();
        tab.setGenericRepository(genericRepository);


    }

    public void testConstructor() {
        assertNotNull(tab);
    }

    public void testTab() {
        expect(genericRepository.findById(StudyParticipantAssignment.class, null)).andReturn(new StudyParticipantAssignment()).anyTimes();
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