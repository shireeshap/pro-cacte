package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFIntegrationTest extends TestDataManager {

    private CRF crf, inValidCRF;
    private String title = "Cancer CRF" + UUID.randomUUID().toString();
    private Study study;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        createCrf();

    }

    private void saveCrf() {

        study = new Study();
        study.setShortTitle("study short title");
        study.setLongTitle("study long title");
        study.setAssignedIdentifier("assigned identifier");
        study = studyRepository.save(study);
        crf.setStudy(study);


        crf = crfRepository.save(crf);
    }

    private void createCrf() {
        crf = new CRF();
        crf.setTitle(title);
        crf.setDescription("Case Report Form for Cancer Patients");
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");
    }

    public void testCopy() {
        saveCrf();

        CRF copiedCrf = crf.copy();
        crfRepository.save(copiedCrf);
        assertNotNull(copiedCrf.getId());
    }

    public void testFindById() {
        saveCrf();

        CRF existingCrf = crfRepository.findById(crf.getId());
        assertEquals(crf.getTitle(), existingCrf.getTitle());
        assertEquals(crf.getStatus(), existingCrf.getStatus());
        assertEquals(crf.getCrfVersion(), existingCrf.getCrfVersion());
        assertEquals(crf.getDescription(), existingCrf.getDescription());

    }


    public void testFindByQuery() {
        saveCrf();

        CRFQuery crfQuery = new CRFQuery();

        Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
        assertFalse(crfs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf");
        assertEquals(size, crfs.size());
    }

    public void testFindByNullNextVersionIdQuery() {
        saveCrf();

        CRFQuery crfQuery = new CRFQuery();
        crfQuery.filterByNullNextVersionId();
        Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
        assertFalse(crfs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf where next_version_id is null");
        assertEquals(size, crfs.size());
    }

    public void testFindByTitleExactMatchQuery() {
        saveCrf();

        CRFQuery crfQuery = new CRFQuery();
        crfQuery.filterByTitleExactMatch(crf.getTitle());
        Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
        assertFalse(crfs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf where lower(crf.title)=?", new String[]{crf.getTitle().toLowerCase()});
        assertEquals(size, crfs.size());
        assertEquals("title must be unique", Integer.valueOf(1), Integer.valueOf(size));


    }

    public void testFilterByNotHavingCrfId() {
        saveCrf();

        CRFQuery crfQuery = new CRFQuery();
        crfQuery.filterByNotHavingCrfId(crf.getId());
        Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
        int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf where crf.id !=?", new Integer[]{crf.getId()});
        assertEquals(size, crfs.size());


    }

    public void testSaveCRFWithPage() {
        crf.addCrfPage(new CRFPage());
        saveCrf();
        assertNotNull(crf.getId());
        assertFalse(crf.getCrfPagesSortedByPageNumber().isEmpty());
        for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
            assertNotNull(crfPage.getId());
            assertSame(crf, crfPage.getCrf());
        }
    }

    public void testUpdateStatusOfCRF() throws ParseException {
        saveCrf();
        assertNotNull(crf.getId());
        crfRepository.updateStatusToReleased(crf);
        assertEquals(CrfStatus.RELEASED, crf.getStatus());
    }

    public void testUniqueCrfTitle() {
        inValidCRF = new CRF();

        try {
            inValidCRF = crfRepository.save(inValidCRF);

            fail("Expected CtcAeSystemException because title is unique");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullCRF() {
        inValidCRF = new CRF();

        try {
            inValidCRF = crfRepository.save(inValidCRF);
            fail("Expected CtcAeSystemException because title, status and formVersion are null");
        } catch (CtcAeSystemException e) {

        }
    }

    public void testSavingNullTitleCRF() {
        inValidCRF = new CRF();
        try {
            inValidCRF.setStatus(CrfStatus.DRAFT);
            inValidCRF.setCrfVersion("1.0");
            inValidCRF = crfRepository.save(inValidCRF);
            fail("Expected CtcAeSystemException because title is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullStatusCRF() {
        inValidCRF = new CRF();
        try {
            inValidCRF.setTitle("Cancer CRF");
            inValidCRF.setStatus(null);
            inValidCRF.setCrfVersion("1.0");
            crfRepository.save(inValidCRF);

            fail("Expected CtcAeSystemException because status is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullVersionCRF() {
        inValidCRF = new CRF();
        try {
            inValidCRF.setTitle(title);
            inValidCRF.setStatus(CrfStatus.DRAFT);
            crfRepository.save(inValidCRF);
            fail("Expected CtcAeSystemException because formVersion is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testVersionForm() throws ParseException {
        Study study = StudyTestHelper.getDefaultStudy();
        List<CRF> crfsToRemove = new ArrayList<CRF>();
        for (CRF crf : study.getCrfs()) {
            if (!crf.getCrfVersion().equals("1.0")) {
                crf.setStatus(CrfStatus.DRAFT);
                crfsToRemove.add(crf);
            }
        }

        study.getCrfs().removeAll(crfsToRemove);
        for (CRF crf : crfsToRemove) {
            crfRepository.delete(crf);
        }
        studyRepository.save(study);
        CRF crf = study.getCrfs().get(0);
        CRFPage lastPage = crf.getCrfPagesSortedByPageNumber().get(crf.getCrfPagesSortedByPageNumber().size() - 1);
        Integer lastPageNumber = lastPage.getPageNumber();

        for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
            studyParticipantCrf.createSchedules();
        }
        StudyParticipantCrf spc = crf.getStudyParticipantCrfs().get(0);
        spc.getStudyParticipantCrfAddedQuestions().clear();
        spc.addStudyParticipantCrfAddedQuestion(lastPage.getCrfPageItems().get(0).getProCtcQuestion(), lastPageNumber + 1);
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByTerm("Tremor, shaking");
        ProCtcTerm proCtcTerm = genericRepository.findSingle(proCtcTermQuery);

        spc.addStudyParticipantCrfAddedQuestion(proCtcTerm.getProCtcQuestions().get(0), lastPageNumber + 2);
        spc = genericRepository.save(spc);

        commitAndStartNewTransaction();
        assertEquals(16, spc.getStudyParticipantCrfSchedules().size());
        int numOfSPCs = crf.getStudyParticipantCrfs().size();

        CRF versionedCrf = crfRepository.versionCrf(crf);
        assertEquals("2.0", versionedCrf.getCrfVersion());
        versionedCrf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), -10));
        versionedCrf = crfRepository.updateStatusToReleased(versionedCrf);
        commitAndStartNewTransaction();

        spc = genericRepository.findById(StudyParticipantCrf.class, spc.getId());
        assertEquals(5, spc.getStudyParticipantCrfSchedules().size());
        assertEquals(numOfSPCs, spc.getCrf().getStudyParticipantCrfs().size());
        assertEquals(numOfSPCs, crf.getStudyParticipantCrfs().size());

        StudyParticipantCrf vSpc = null;
        StudyParticipantAssignment studyParticipantAssignment = spc.getStudyParticipantAssignment();
        for (StudyParticipantCrf studyParticipantCrf : versionedCrf.getStudyParticipantCrfs()) {
            if (studyParticipantCrf.getStudyParticipantAssignment().equals(studyParticipantAssignment)) {
                vSpc = studyParticipantCrf;
                break;
            }
        }
        assertNotNull(vSpc);
        assertEquals(1, vSpc.getStudyParticipantCrfAddedQuestions().size());

    }


}
