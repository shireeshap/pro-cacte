package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.meddra.MeddraAjaxFacade;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.text.ParseException;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Harsh Agarwal
 * Date June 11, 2009
 */
public class SubmitFormCommandTest extends AbstractWebTestCase {
    List<ListOrderedMap> l;
    SubmitFormCommand sfc;
    StudyParticipantCrfSchedule spcs;
    MeddraAjaxFacade meddraAjaxFacade;

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-dwr.xml");
        return list.toArray(new String[]{});
    }


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        deleteAndCreateTestData();
        l = jdbcTemplate.queryForList("select s.id from sp_crf_schedules s");
        sfc = new SubmitFormCommand();
        sfc.setGenericRepository(genericRepository);

        spcs = genericRepository.findById(StudyParticipantCrfSchedule.class, (Integer) l.get(0).getValue(0));
    }

    public void testSetSchedule() throws ParseException {
        sfc.setStudyParticipantCrfSchedule(spcs);
        assertEquals(spcs, sfc.getStudyParticipantCrfSchedule());

        CRF crf = spcs.getStudyParticipantCrf().getCrf();
        CRF versionedCrf = crfRepository.versionCrf(crf);
        versionedCrf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), -10));
        versionedCrf = crfRepository.updateStatusToReleased(versionedCrf);
        commitAndStartNewTransaction();

        sfc.setStudyParticipantCrfSchedule(spcs);
        assertNotSame(sfc.getStudyParticipantCrfSchedule(), spcs);
        assertEquals(versionedCrf, sfc.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getCrf());
        assertNull(genericRepository.findById(StudyParticipantCrfSchedule.class, spcs.getId()));

    }

    public void testAddParticipantAddedQuestions() {
        assertEquals(0, spcs.getStudyParticipantCrfScheduleAddedQuestions().size());
        sfc.setStudyParticipantCrfSchedule(spcs);
        if (!isMeddraTermsLoaded()) {
            for (int i = 0; i < 8; i++) {
                meddraAjaxFacade.handleMedDRA((String) applicationContext.getBean("meddraDirectory"), i, "MEDDRA_TEST_AUTO_LOAD");
            }
        }
        String[] symptoms = new String[4];
        symptoms[0] = "Increased skin sensitivity to sunlight";
        symptoms[1] = "Bloating of the abdomen (belly)";
        symptoms[2] = "Anorexia";
        symptoms[3] = "Abscess breast";

        sfc.addParticipantAddedQuestions(symptoms);
        assertEquals(6, spcs.getStudyParticipantCrfScheduleAddedQuestions().size());

    }

    public void testInitialize() {
        sfc.setStudyParticipantCrfSchedule(spcs);
        assertEquals(0, sfc.getCurrentPageIndex());
        assertEquals(0, sfc.getTotalPages());
        assertEquals(0, sfc.getParticipantAddedQuestionIndex());
        assertNull(sfc.getDisplayRules());
        sfc.initialize();
        assertEquals(1, sfc.getCurrentPageIndex());
        assertEquals(10, sfc.getTotalPages());
        assertEquals(11, sfc.getParticipantAddedQuestionIndex());
        assertNotNull(sfc.getDisplayRules());

    }

    @Required
    public void setMeddraAjaxFacade(MeddraAjaxFacade meddraAjaxFacade) {
        this.meddraAjaxFacade = meddraAjaxFacade;
    }
}