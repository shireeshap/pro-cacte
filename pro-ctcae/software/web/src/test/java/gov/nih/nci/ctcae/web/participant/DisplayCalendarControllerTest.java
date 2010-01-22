package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @since Nov 25, 2008
 */
public class DisplayCalendarControllerTest extends WebTestCase {
    DisplayCalendarController controller;
    ParticipantSchedule participantSchedule;
    ProCtcAECalendar calendar;

    public void reset() {
        resetMocks();
        controller = new DisplayCalendarController();
        request.setMethod("GET");
        request.setParameter("index", "0");
        StudyParticipantCommand studyParticipantCommand = registerMockFor(StudyParticipantCommand.class);
        participantSchedule = registerMockFor(ParticipantSchedule.class);
        calendar = registerMockFor(ProCtcAECalendar.class);
        List l = new ArrayList();
        l.add(participantSchedule);
        request.getSession().setAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command", studyParticipantCommand);
        expect(studyParticipantCommand.getParticipantSchedules()).andReturn(l).anyTimes();

    }

    public void testController() throws Exception {
        reset();
        request.setParameter("dir","prev");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        calendar.add(-1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir","next");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        calendar.add(1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir","refresh");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        calendar.add(0);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

    }
}