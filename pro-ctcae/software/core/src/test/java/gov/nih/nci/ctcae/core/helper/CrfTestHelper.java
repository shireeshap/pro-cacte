package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class CrfTestHelper {

    private static final int numberOfSymptoms = 10;
    private static final String formTitle = "PRO Form 1";
    private static CRFRepository crfRepository;
    private static ProCtcTermRepository proCtcTermRepository;
    private static TestDataManager myTestDataManager;

    private CrfTestHelper() {

    }

    public static void inititalize() {
        crfRepository = TestDataManager.crfRepository;
        proCtcTermRepository = TestDataManager.proCtcTermRepository;
    }

    public static void createTestForm() throws Exception {
        ParticipantTestHelper.createParticipant("Charlie", "Boon", "1-4", StudyTestHelper.getDefaultStudy().getLeadStudySite(), 0);
        myTestDataManager.commitAndStartNewTransaction();
        deleteAutoGeneratedForms();
        CRF crf = new CRF();
        crf.setTitle(formTitle);
        firstTab_SelectStudy(crf, StudyTestHelper.getDefaultStudy());
        secondTab_FormBuilder(crf);
        thirdTab_ScheduleTemplate(crf);
        crfRepository.save(crf);
        fourthTab_Notifications(crf);
        crfRepository.save(crf);
        crf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), 2));
        crf = crfRepository.updateStatusToReleased(crf);
        createSecondaryForms();

    }

    public static void deleteIVRSTestForm() {
        CRFQuery query = new CRFQuery();
        query.filterByTitleExactMatch("IVRSForm");
        CRF crf = crfRepository.findSingle(query);
        if(crf!=null){
            crf.setStatus(CrfStatus.DRAFT);
            crf = crfRepository.save(crf);
            crfRepository.delete(crf);
        }
    }

    public static void createIVRSTestForm(Study study,TestDataManager localTestDataManager) throws Exception {
        ParticipantTestHelper.createIVRSParticipant("ivrs", "participant", "007", study.getLeadStudySite(), 0,"1234567890",1234);
        if(myTestDataManager == null)
             localTestDataManager.commitAndStartNewTransaction();
        else
            myTestDataManager.commitAndStartNewTransaction();
        CRF crf = new CRF();
        crf.setTitle("IVRSForm");
        firstTab_SelectStudy(crf, study);
        secondTab_IVRSFormBuilder(crf);
        thirdTab_ScheduleTemplate(crf);
        crf.getFormArmSchedules().get(0).getCrfCycleDefinitions().get(0).setDueDateUnit("Days");
        crf.getFormArmSchedules().get(0).getCrfCycleDefinitions().get(0).setDueDateValue("5");
        crf = crfRepository.save(crf);
        fourthTab_Notifications(crf);
        crf = crfRepository.save(crf);
        crf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), 2));
        crf = crfRepository.updateStatusToReleased(crf);
    }

    private static void createSecondaryForms() throws ParseException {
        CRF crf = new CRF();
        crf.setTitle("PRO Form 2");
        firstTab_SelectStudy(crf, StudyTestHelper.getSecondaryStudy());
        secondTab_FormBuilder(crf);

        crfRepository.save(crf);
        crf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), 3));
        crf = crfRepository.updateStatusToReleased(crf);

        crf = new CRF();
        crf.setTitle("PRO Form 3");
        firstTab_SelectStudy(crf, StudyTestHelper.getSecondaryStudy());
        secondTab_FormBuilder(crf);
        crfRepository.save(crf);
        crf.setEffectiveStartDate(DateUtils.addDaysToDate(new Date(), 0));
        crf = crfRepository.updateStatusToReleased(crf);
    }

    private static void firstTab_SelectStudy(CRF crf, Study study) {
        crf.setStudy(study);
        for (Arm arm : study.getArms()) {
            crf.addFormArmSchedule(arm);
        }
    }

    private static void secondTab_FormBuilder(CRF crf) {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(query);
        crf.setCrfVersion("1.0");
        for (int i = 0; i < numberOfSymptoms; i++) {
            crf.addProCtcTerm(proCtcTerms.get(i));
        }
        crf.updateCrfPageInstructions();
    }

     private static void secondTab_IVRSFormBuilder(CRF crf) {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(query);
        crf.setCrfVersion("1.0");
        for (int i = 0; i < 2; i++) {
            crf.addProCtcTerm(proCtcTerms.get(i));
        }
        crf.updateCrfPageInstructions();
    }

    private static void thirdTab_ScheduleTemplate(CRF crf) {
        for (FormArmSchedule formArmSchedule : crf.getFormArmSchedules()) {
            CRFCalendar calendar = new CRFCalendar();
            calendar.setRepeatEveryValue("2");
            calendar.setRepeatEveryUnit("Days");
            calendar.setDueDateValue("24");
            calendar.setDueDateUnit("Hours");
            calendar.setRepeatUntilValue("2");
            calendar.setRepeatUntilUnit("Number");

            formArmSchedule.addCrfCalendar(calendar);

            CRFCycleDefinition defA = new CRFCycleDefinition();
            defA.setCycleLength(14);
            defA.setCycleLengthUnit("Days");
            defA.setRepeatTimes("2");
            defA.setOrder(0);

            CRFCycle cycle1 = new CRFCycle();
            cycle1.setOrder(0);
            cycle1.setCycleDays(",1,8,13");
            defA.addCrfCycle(cycle1);

            CRFCycle cycle2 = new CRFCycle();
            cycle2.setOrder(1);
            cycle2.setCycleDays(",3,8,11");
            defA.addCrfCycle(cycle2);

            CRFCycle cycle5 = new CRFCycle();
            cycle5.setOrder(2);
            cycle5.setCycleDays(",");
            defA.addCrfCycle(cycle5);

            CRFCycle cycle6 = new CRFCycle();
            cycle6.setOrder(3);
            cycle6.setCycleDays("");
            defA.addCrfCycle(cycle6);

            formArmSchedule.addCrfCycleDefinition(defA);

            CRFCycleDefinition defB = new CRFCycleDefinition();
            defB.setCycleLength(21);
            defB.setCycleLengthUnit("Days");
            defB.setRepeatTimes("2");
            defB.setOrder(1);

            CRFCycle cycle3 = new CRFCycle();
            cycle3.setOrder(0);
            cycle3.setCycleDays(",4,10,18,21");
            defB.addCrfCycle(cycle3);

            CRFCycle cycle4 = new CRFCycle();
            cycle4.setOrder(1);
            cycle4.setCycleDays(",5,11,19");
            defB.addCrfCycle(cycle4);


            formArmSchedule.addCrfCycleDefinition(defB);
        }
    }

    private static void fourthTab_Notifications(CRF crf) throws Exception {
        List<String> symptoms = new ArrayList<String>();
        List<String> questiontypes = new ArrayList<String>();
        List<String> operators = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        List<String> notifications = new ArrayList<String>();


        for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                ProCtcQuestion q = crfPageItem.getProCtcQuestion();
                if (q.getProCtcQuestionType().equals(ProCtcQuestionType.SEVERITY)) {
                    symptoms.add(q.getProCtcTerm().getProCtcTermVocab().getTermEnglish());
                }
                if (symptoms.size() == 1) {
                    break;
                }
            }
        }

        questiontypes.add(ProCtcQuestionType.SEVERITY.getDisplayName());
        operators.add(">");
        values.add("2");
        notifications.add("PrimaryNurse");
        notifications.add("SiteCRA");
        notifications.add("PI");
        notifications.add("PrimaryPhysician");
        notifications.add("SitePI");
        notifications.add("LeadCRA");

    }

    private static void deleteAutoGeneratedForms() {
        CRFQuery query = new CRFQuery();
        List<CRF> crfs = (List<CRF>) crfRepository.find(query);
        for (CRF crf : crfs) {
            crfRepository.delete(crf);
        }
    }

    public static void setTestDataManager(TestDataManager testDataManager) {
        myTestDataManager = testDataManager;
    }
}