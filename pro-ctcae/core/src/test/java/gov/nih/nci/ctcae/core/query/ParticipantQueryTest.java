package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author mehul
 */

public class ParticipantQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT p from Participant p order by p.id", participantQuery.getQueryString());

    }

    public void testFilterByFirstName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantFirstName("John");
        assertEquals("SELECT p from Participant p WHERE lower(p.firstName) LIKE :firstName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("firstName"), "%john%");
    }

    public void testFilterByLastName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT p from Participant p WHERE lower(p.lastName) LIKE :lastName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("lastName"), "%dow%");
    }

    public void testFilterByFullName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantFirstName("John");
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT p from Participant p WHERE lower(p.lastName) LIKE :lastName AND lower(p.firstName) LIKE :firstName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 2);

    }

}
