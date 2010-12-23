package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

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
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("firstName"), "john");
    }

    public void testFilterByParticipantIdentifier() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantIdentifier("id001");
        assertEquals("SELECT p from Participant p WHERE lower(p.assignedIdentifier) LIKE :assignedIdentifier order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("assignedIdentifier"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("assignedIdentifier"), "id001");
    }
    public void testFilterByStudyI() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByStudy(null);
        assertEquals("SELECT p from Participant p order by p.id",
                participantQuery.getQueryString());

         participantQuery = new ParticipantQuery();
               participantQuery.filterByStudy(1);
               assertEquals("SELECT p from Participant p left join p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study WHERE study.id =:studyId order by p.id",
                       participantQuery.getQueryString());

        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("studyId"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("studyId"), 1);
    }

    public void testFilterByLastName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT p from Participant p WHERE lower(p.lastName) LIKE :lastName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("lastName"), "dow");
    }

    public void testFilterByFullName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantFirstName("John");
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT p from Participant p WHERE lower(p.lastName) LIKE :lastName AND lower(p.firstName) LIKE :firstName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 2);

    }
    public void testFilterByUsername() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByUsername("1");
        assertEquals("SELECT p from Participant p WHERE p.user.username =:username order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", 1,participantQuery.getParameterMap().size());

    }

    public void testFabc(){
        String[] strings = StringUtils.split("  Mehul           Gulati   ", null);
        for(String string:strings){
            System.out.println(string);
            System.out.println(String.format("hdjhjd %s%s",StringUtils.lowerCase("L"),"%"));
            System.out.println(String.format("( lower(p.firstName) = :%s AND lower(p.lastName) like %s) or " +
                        "(lower(p.lastName) = :%s AND lower(p.firstName) like %s) ", "FIRST_TOKEN","SECOND_TOKEN","THIRD_TOKEN","FOURTH_TOKEN"));
        }
    }

}
