package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;

/**
 * @author Harsh Agarwal
 * @since Nov 25, 2008
 */
public class DisplayCalendarControllerTest extends WebTestCase {
    DisplayCalendarController controller;
    ParticipantSchedule participantSchedule;
    ProCtcAECalendar calendar;
    StudyParticipantAssignment spa;
    StudyParticipantCommand studyParticipantCommand;
    ParticipantCommand participantCommand;

    public void reset() {
        resetMocks();
        controller = new DisplayCalendarController();
        request.setMethod("GET");
        request.setParameter("index", "0");
        studyParticipantCommand = registerMockFor(StudyParticipantCommand.class);
        participantCommand = registerMockFor(ParticipantCommand.class);
        participantSchedule = registerMockFor(ParticipantSchedule.class);
        spa = registerMockFor(StudyParticipantAssignment.class);
        
        calendar = registerMockFor(ProCtcAECalendar.class);
        List l = new ArrayList();
        l.add(participantSchedule);
        request.getSession().setAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command", studyParticipantCommand);
        request.getSession().setAttribute(CreateParticipantController.class.getName() + ".FORM." + "command", participantCommand);
        expect(participantCommand.getParticipantSchedules()).andReturn(l).anyTimes();
        expect(participantCommand.getSelectedStudyParticipantAssignment()).andReturn(spa);
    }

    public void testController() throws Exception {
        reset();
        request.setParameter("dir", "prev");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(-1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir", "next");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir", "refresh");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(0);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

    }
}