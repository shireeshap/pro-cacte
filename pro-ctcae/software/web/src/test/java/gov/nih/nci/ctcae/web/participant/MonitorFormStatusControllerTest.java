package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Vinay Kumar
 * @since Nov 4, 2008
 */
public class MonitorFormStatusControllerTest extends AbstractWebTestCase {

    Study s;
    MonitorFormStatusController controller;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        s = StudyTestHelper.getDefaultStudy();
        CRF crf = s.getCrfs().get(0);

        request.setMethod("GET");
        request.setParameter("studyId", s.getId().toString());
        request.setParameter("crfId", crf.getId().toString());
        request.setParameter("dateRange", "thisWeek");
        request.setParameter("status", "all");
        request.setParameter("view", "initial");
        request.setParameter("direction", "");
        controller = new MonitorFormStatusController();
        controller.setStudyRepository(studyRepository);
        controller.setStudyOrganizationRepository(studyOrganizationRepository);
    }

    public void testControllerNoFilter() throws Exception {

        ModelAndView mv = controller.handleRequest(request, response);

        HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>> sMap = (HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>>) mv.getModel().get("crfStatusMap");
        assertEquals(s.getStudySites().size(), sMap.keySet().size());
        for (StudySite ss : sMap.keySet()) {
            HashMap<Participant, StudyParticipantCrfSchedule[]> pMap = sMap.get(ss);
            assertEquals(ss.getStudyParticipantAssignments().size(), pMap.keySet().size());
            for (Participant p : pMap.keySet()) {
                StudyParticipantCrfSchedule[] sArr = pMap.get(p);
                assertEquals(7, sArr.length);
            }
        }
        List<Date> dates = (List<Date>) mv.getModel().get("calendar");
        assertEquals(7, dates.size());
        assertEquals(dates.get(6), mv.getModel().get("pgStartNext"));
        assertEquals(dates.get(0), mv.getModel().get("pgStartPrev"));
        assertEquals("weekly", mv.getModel().get("tablePeriod"));
    }

    public void testControllerSiteFilter() throws Exception {

        request.addParameter("studySiteId", s.getLeadStudySite().getId().toString());
        request.setParameter("view", "monthly");
        request.setParameter("pgStartDatePrev", DateUtils.format(DateUtils.addDaysToDate(new Date(), 1)));
        ModelAndView mv = controller.handleRequest(request, response);

        HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>> sMap = (HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>>) mv.getModel().get("crfStatusMap");
        assertEquals(1, sMap.keySet().size());
        for (StudySite ss : sMap.keySet()) {
            assertEquals(s.getLeadStudySite(), ss);
            HashMap<Participant, StudyParticipantCrfSchedule[]> pMap = sMap.get(ss);
            assertEquals(ss.getStudyParticipantAssignments().size(), pMap.keySet().size());
            for (Participant p : pMap.keySet()) {
                StudyParticipantCrfSchedule[] sArr = pMap.get(p);
                assertEquals(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) , sArr.length);
            }
        }
        List<Date> dates = (List<Date>) mv.getModel().get("calendar");
        assertEquals(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) , dates.size());
        assertEquals(dates.get(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - 1), mv.getModel().get("pgStartNext"));
        assertEquals(dates.get(0), mv.getModel().get("pgStartPrev"));
        assertEquals("monthly", mv.getModel().get("tablePeriod"));


    }

    public void testControllerParticipantFilter() throws Exception {
        request.addParameter("studySiteId", s.getLeadStudySite().getId().toString());
        request.addParameter("participantId", ParticipantTestHelper.getDefaultParticipant().getId().toString());
        request.setParameter("view", "monthly");
        request.setParameter("pgStartDatePrev", DateUtils.format(new Date()));
        request.setParameter("direction", "next");
        request.setParameter("dateRange", "custom");
        request.setParameter("pgStartDateNext", DateUtils.format(new Date()));
        ModelAndView mv = controller.handleRequest(request, response);

        HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>> sMap = (HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>>) mv.getModel().get("crfStatusMap");
        assertEquals(1, sMap.keySet().size());
        for (StudySite ss : sMap.keySet()) {
            assertEquals(s.getLeadStudySite(), ss);
            HashMap<Participant, StudyParticipantCrfSchedule[]> pMap = sMap.get(ss);
            assertEquals(1, pMap.keySet().size());
            for (Participant p : pMap.keySet()) {
                assertEquals(p, ParticipantTestHelper.getDefaultParticipant());
                StudyParticipantCrfSchedule[] sArr = pMap.get(p);
                assertEquals(30, sArr.length);
            }
        }
        List<Date> dates = (List<Date>) mv.getModel().get("calendar");
        assertEquals(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH), dates.size());
        assertEquals(dates.get(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - 1), mv.getModel().get("pgStartNext"));
        assertEquals(dates.get(0), mv.getModel().get("pgStartPrev"));
        assertEquals("monthly", mv.getModel().get("tablePeriod"));


    }

}