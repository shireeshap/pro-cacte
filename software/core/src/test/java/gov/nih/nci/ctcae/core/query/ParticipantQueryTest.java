package gov.nih.nci.ctcae.core.query;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

/**
 * @author mehul
 */

public class ParticipantQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        assertEquals("wrong parsing for constructor",
                "SELECT distinct p from Participant p order by p.id", participantQuery.getQueryString());

    }

    public void testFilterByFirstName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByParticipantFirstName("John");
        assertEquals("SELECT distinct p from Participant p WHERE lower(p.firstName) LIKE :firstName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("firstName"), "%john%");
    }

    public void testFilterByParticipantIdentifier() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByParticipantIdentifier("id001");
        assertEquals("SELECT distinct p from Participant p WHERE lower(p.assignedIdentifier) LIKE :assignedIdentifier order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("assignedIdentifier"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("assignedIdentifier"), "%id001%");
    }
    public void testFilterByStudyI() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByStudy(null);
        assertEquals("SELECT distinct p from Participant p order by p.id",
                participantQuery.getQueryString());

         participantQuery = new ParticipantQuery(false);
               participantQuery.filterByStudy(1);
               assertEquals("SELECT distinct p from Participant p left join p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study WHERE study.id =:studyId order by p.id",
                       participantQuery.getQueryString());

        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("studyId"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("studyId"), 1);
    }

    public void testFilterByLastName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT distinct p from Participant p WHERE lower(p.lastName) LIKE :lastName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", participantQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", participantQuery.getParameterMap().get("lastName"), "%dow%");
    }

    public void testFilterByFullName() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByParticipantFirstName("John");
        participantQuery.filterByParticipantLastName("dow");
        assertEquals("SELECT distinct p from Participant p WHERE lower(p.lastName) LIKE :lastName AND lower(p.firstName) LIKE :firstName order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", participantQuery.getParameterMap().size(), 2);

    }
    public void testFilterByUsername() throws Exception {
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByUsername("1");
        assertEquals("SELECT distinct p from Participant p WHERE p.user.username =:username order by p.id",
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

    public void testFilterByUserNumber() throws Exception{
        String userNumber ="1234567890";
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByUserNumber(userNumber);
        assertEquals("SELECT distinct p from Participant p WHERE p.userNumber =:userNumber order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", 1,participantQuery.getParameterMap().size());


    }

    public void testFilterByEmail() throws Exception{
        String email = "reshma.koganti@gmail.com" ;
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByEmail(email);
        assertEquals("SELECT distinct p from Participant p WHERE lower(p.emailAddress) LIKE :emailAddress order by p.id",
                participantQuery.getQueryString());
        assertEquals("wrong number of parameters", 1,participantQuery.getParameterMap().size());
    }
}
