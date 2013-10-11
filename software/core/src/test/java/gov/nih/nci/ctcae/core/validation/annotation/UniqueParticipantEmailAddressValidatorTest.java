package gov.nih.nci.ctcae.core.validation.annotation;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Dec 23, 2010
 * Time: 4:41:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class UniqueParticipantEmailAddressValidatorTest extends AbstractTestCase{
    private UniqueParticipantEmailAddressValidator validator;
    private ParticipantRepository participantRepository;
    private Participant participant;
    Collection<Participant> participants;
    private GenericRepository genericRepository;

    public void UniqueParticipantEmailAddressValidatorTest(){
    }

    public void setUp() throws Exception{
        super.setUp();
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new UniqueParticipantEmailAddressValidator();
        validator.setGenericRepository(genericRepository);
        participant = new Participant();
        participants = new ArrayList<Participant>();
        participant.setEmailAddress("reshma.koganti@gmail.com");
         
    }
    

    public void testValidateEmail(){
        String emailAddress ="reshma.koganti@gmail.com";
        Integer participantId=999;
        expect(genericRepository.find(isA(ParticipantQuery.class))).andStubReturn(participants);
        participants.add(participant);
        replayMocks();
        assertTrue("Email Address already exists in database", validator.validateEmail(emailAddress,participantId));
        verifyMocks();
    }
 
}
