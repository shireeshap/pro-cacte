package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author  Vinay Kumar
 * @since Nov 21, 2008
 */
public class StudyParticipantAssignmentQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT o from StudyParticipantAssignment o", query
                        .getQueryString());

    }

    public void testFilterByStudyId() throws Exception {
        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        query.filterByStudyId(1);
        assertEquals(
                "SELECT o from StudyParticipantAssignment o left join o.studySite as ss join ss.study as study WHERE study.id =:studyId",
                query.getQueryString());
        assertEquals("wrong number of parameters", query.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", query.getParameterMap().containsKey(
                "studyId"));
        assertEquals("wrong parameter value", query.getParameterMap().get("studyId"),
                1);


    }


    public void testFilterByParticipantId() throws Exception {
        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        query.filterByParticipantId(2);
        assertEquals(
                "SELECT o from StudyParticipantAssignment o WHERE o.participant.id = :participantId",
                query.getQueryString());
        assertEquals("wrong number of parameters", query.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", query.getParameterMap().containsKey(
                "participantId"));
        assertEquals("wrong parameter value", query.getParameterMap().get(
                "participantId"), 2);


    }



}