package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * @author Harsh Agarwal
 *         Date June 12, 2009
 */
public class AddCrfScheduleControllerTest extends WebTestCase {

    AddCrfScheduleController controller;
    ParticipantSchedule participantSchedule;
    ProCtcAECalendar calendar;

    protected void reset() throws Exception {
        resetMocks();
        controller = new AddCrfScheduleController();
        request.setMethod("GET");
        request.setParameter("index", "0");
        StudyParticipantCommand studyParticipantCommand = registerMockFor(StudyParticipantCommand.class);
        participantSchedule = registerMockFor(ParticipantSchedule.class);
        calendar = new ProCtcAECalendar();
        List l = new ArrayList();
        l.add(participantSchedule);
        request.getSession().setAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command", studyParticipantCommand);
        expect(participantSchedule.getCalendar()).andReturn(calendar);
        expect(studyParticipantCommand.getParticipantSchedules()).andReturn(l);
    }

    public void testController() throws Exception {

        reset();
        request.setParameter("action", "delall");
        participantSchedule.removeAllSchedules();
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "moveall");
        request.setParameter("date", "9,1");
        participantSchedule.moveAllSchedules(8);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "moveallfuture");
        request.setParameter("date", "6,1");

        Calendar c = new GregorianCalendar();
        c.setTime(calendar.getTime());
        participantSchedule.moveFutureSchedules(c, 5);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "delallfuture");
        request.setParameter("date", "1");
        c.setTime(calendar.getTime());
        c.set(Calendar.DATE, 1);
        participantSchedule.deleteFutureSchedules(c);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "add,del");
        request.setParameter("date", "5,1");
        c.setTime(calendar.getTime());
        c.set(Calendar.DATE, 5);
        expect(participantSchedule.createSchedule(c, 86400000, -1, -1)).andReturn(null);

        Calendar d = new GregorianCalendar();
        d.setTime(calendar.getTime());
        d.set(Calendar.DATE, 1);
        participantSchedule.removeSchedule(d);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "add");
        request.setParameter("date", "9");
        c.setTime(calendar.getTime());
        c.set(Calendar.DATE, 9);
        expect(participantSchedule.createSchedule(c, 86400000, -1, -1)).andReturn(null);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("action", "del");
        request.setParameter("date", "5");
        c.setTime(calendar.getTime());
        c.set(Calendar.DATE, 5);
        participantSchedule.removeSchedule(c);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

    }

}