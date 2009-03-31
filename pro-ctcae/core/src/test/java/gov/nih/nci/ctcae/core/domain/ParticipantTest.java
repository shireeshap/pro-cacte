package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author mehul
 */

public class ParticipantTest extends TestCase {
    private Participant participant;

    public void testConstructor() {
        participant = new Participant();
        assertNull(participant.getFirstName());
        assertNull(participant.getLastName());
    }

    public void testGetterAndSetter() {
        participant = new Participant();
        String name = "john";
        participant.setFirstName(name);
        participant.setLastName("Dow");
       
        Date date = new Date();
        participant.setBirthDate(date);
        participant.setGender("male");
        participant.setMaidenName("m");
        participant.setMiddleName("mid");

        assertEquals("john", participant.getFirstName());
        assertEquals("Dow", participant.getLastName());
        assertEquals(date, participant.getBirthDate());
        assertEquals("male", participant.getGender());
        assertEquals("m", participant.getMaidenName());
        assertEquals("mid", participant.getMiddleName());
        assertEquals("john Dow", participant.getDisplayName());
    }

    public void testEqualsAndHashCode() {
        Participant anotherParticipant = null;
        assertEquals(anotherParticipant, participant);
        participant = new Participant();
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant = new Participant();
        assertEquals(anotherParticipant, participant);
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());

        participant.setFirstName("John");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setFirstName("John");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setLastName("Dow");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setLastName("Dow");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        assertFalse(participant.equals(anotherParticipant));
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        Date date = new Date();
        participant.setBirthDate(date);
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setBirthDate(date);
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        assertFalse(participant.equals(anotherParticipant));
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        participant.setGender("male");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setGender("male");
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        assertFalse(participant.equals(anotherParticipant));
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        participant.setMaidenName("m");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setMaidenName("m");
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        participant.setMiddleName("mid");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setMiddleName("mid");
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);

        assertFalse(participant.equals(anotherParticipant));
        assertEquals(Integer.valueOf(anotherParticipant.hashCode()), Integer.valueOf(participant.hashCode()));
        assertEquals(anotherParticipant, participant);


    }
}
