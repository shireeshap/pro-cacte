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
        participant.setAddress("abc");
   /*     SimpleDateFormat s = new SimpleDateFormat("MM-DD-YYYY");
        Date d = null;
        try{
            d = s.parse("10-28-2008");

        }catch (java.text.ParseException p){
        }
            participant.setBirthDate(d);

     */
        participant.setBirthDate(new Date());
        participant.setEthnicity("hispanic");
        participant.setGender("male");
        participant.setRace("xyz");
        participant.setMaidenName("m");
        participant.setTitle("mr");
        participant.setMiddleName("mid");

        assertEquals("john", participant.getFirstName());
        assertEquals("Dow", participant.getLastName());
        assertEquals("abc" , participant.getAddress());
        assertEquals(new Date() , participant.getBirthDate());
        assertEquals("hispanic" , participant.getEthnicity());
        assertEquals("male" , participant.getGender());
        assertEquals("xyz" , participant.getRace());
        assertEquals("m" , participant.getMaidenName());
        assertEquals("mid" ,participant.getMiddleName());
        assertEquals("mr" ,participant.getTitle());
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

        participant.setAddress("abc");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setAddress("abc");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setBirthDate(new Date());
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setBirthDate(new Date());
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setEthnicity("hispanic");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setEthnicity("hispanic");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setGender("male");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setGender("male");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setRace("xyz");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setRace("xyz");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setMaidenName("m");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setMaidenName("m");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setMiddleName("mid");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setMiddleName("mid");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);

        participant.setTitle("mr");
        assertFalse(participant.equals(anotherParticipant));
        anotherParticipant.setTitle("mr");
        assertEquals(anotherParticipant.hashCode(), participant.hashCode());
        assertEquals(anotherParticipant, participant);


    }
}
