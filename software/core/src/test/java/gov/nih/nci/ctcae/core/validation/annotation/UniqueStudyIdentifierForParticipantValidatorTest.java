package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.util.ArrayList;
import java.util.Collection;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Dec 17, 2010
 * Time: 11:04:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class UniqueStudyIdentifierForParticipantValidatorTest extends AbstractTestCase {
    private UniqueStudyIdentifierForParticipantValidator uniqueStudyIdentifierForParticipantValidator;
    private ParticipantRepository participantRepository;
    private Participant participant;
    Collection<Participant> participants;
    private StudyParticipantAssignment studyParticipantAssignment;

    public void UniqueStudyIdentifierForParticipantValidatorTest(){
        
    }
    public void setUp() throws Exception{
        super.setUp();
        uniqueStudyIdentifierForParticipantValidator = new UniqueStudyIdentifierForParticipantValidator();
        participantRepository = registerMockFor(ParticipantRepository.class);
        uniqueStudyIdentifierForParticipantValidator.setParticipantRepository(participantRepository);
        participant = new Participant();
        participants = new ArrayList<Participant>();
        participant.setAssignedIdentifier("1234");
        studyParticipantAssignment = new StudyParticipantAssignment();
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        studyParticipantAssignment.setStudyParticipantIdentifier("1234");
        participants.add(participant);
    }

    public void testValidateUniqueParticipantIdentifier(){
        Integer studyId=1;
        String assignedIdentifier="1234";
        Integer participantId=null;
        expect(participantRepository.find(isA(ParticipantQuery.class))).andReturn(participants).anyTimes();
        replayMocks();
        assertTrue("Identifier already exists in database", uniqueStudyIdentifierForParticipantValidator.validateUniqueParticipantIdentifier(studyId,assignedIdentifier,participantId));
        verifyMocks();
    }
}
