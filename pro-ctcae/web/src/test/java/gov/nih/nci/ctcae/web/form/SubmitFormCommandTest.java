package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.Fixture;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;


import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class SubmitFormCommandTest extends WebTestCase {

    private SubmitFormCommand command;
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    FinderRepository finderRepository;
    GenericRepository genericRepository;
    List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>();
    private CRF crf, newCrf;
    private Study study;
    private ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3, proCtcQuestion4, proCtcQuestion5, proCtcQuestion6, proCtcQuestion7, proCtcQuestion8;
    private StudyParticipantAssignment studyParticipantAssignment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new SubmitFormCommand();
        finderRepository = registerMockFor(FinderRepository.class);
        genericRepository = registerMockFor(GenericRepository.class);
        command.setFinderRepository(finderRepository);
        command.setGenericRepository(genericRepository);

        ProCtcTerm proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Fatigue");

        ProCtcTerm proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("Pain");

        ProCtcTerm proCtcTerm3 = new ProCtcTerm();
        proCtcTerm3.setTerm("Cough");

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);
        CrfPageItem item1 = new CrfPageItem();
        item1.setId(1);
        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setId(-10);
        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue1);
        item1.addCrfPageItemDisplayRules(crfPageItemDisplayRule);
        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item1.setProCtcQuestion(proCtcQuestion1);
        item1.setDisplayOrder(1);


        CrfPageItem item2 = new CrfPageItem();
        item2.setId(2);
        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item2.setProCtcQuestion(proCtcQuestion2);
        item2.setDisplayOrder(2);

        CrfPageItem item3 = new CrfPageItem();
        item3.setId(3);
        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcTerm(proCtcTerm2);
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item3.setProCtcQuestion(proCtcQuestion3);
        item3.setDisplayOrder(3);

        CrfPageItem item4 = new CrfPageItem();
        item4.setId(4);
        proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setProCtcTerm(proCtcTerm2);
        proCtcQuestion4.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item4.setProCtcQuestion(proCtcQuestion4);
        item4.setDisplayOrder(4);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfPageItem(item1);
        ProCtcValidValue proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue(0);
        studyParticipantCrfItem1.setProCtcValidValue(proCtcValidValue2);

        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
        studyParticipantCrfItem2.setCrfPageItem(item2);

        StudyParticipantCrfItem studyParticipantCrfItem3 = new StudyParticipantCrfItem();
        studyParticipantCrfItem3.setCrfPageItem(item3);

        StudyParticipantCrfItem studyParticipantCrfItem4 = new StudyParticipantCrfItem();
        studyParticipantCrfItem4.setCrfPageItem(item4);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem3);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem4);

        proCtcQuestion5 = new ProCtcQuestion();
        proCtcQuestion6 = new ProCtcQuestion();
        proCtcQuestion7 = new ProCtcQuestion();
        proCtcQuestion8 = new ProCtcQuestion();

        proCtcQuestion1.setId(1);
        proCtcQuestion2.setId(2);
        proCtcQuestion3.setId(3);
        proCtcQuestion4.setId(4);

        proCtcQuestion5.setId(5);
        proCtcQuestion5.setProCtcTerm(proCtcTerm3);
        proCtcQuestion6.setId(6);
        proCtcQuestion6.setProCtcTerm(proCtcTerm3);
        proCtcQuestion7.setId(7);
        proCtcQuestion7.setProCtcTerm(proCtcTerm3);
        proCtcQuestion8.setId(8);
        proCtcQuestion8.setProCtcTerm(proCtcTerm3);

        questions.add(proCtcQuestion1);
        questions.add(proCtcQuestion2);
        questions.add(proCtcQuestion3);
        questions.add(proCtcQuestion4);
        questions.add(proCtcQuestion5);
        questions.add(proCtcQuestion6);
        questions.add(proCtcQuestion7);
        questions.add(proCtcQuestion8);
        command.setProCtcQuestions(questions);

        crf = Fixture.createCrf();
        crf.setId(1);
        crf.addCrfPage(proCtcQuestion1);
        crf.addCrfPage(proCtcQuestion2);
        crf.addCrfPage(proCtcQuestion3);
        crf.addCrfPage(proCtcQuestion4);

        study = Fixture.createStudyWithStudySite("short", "long", "assigned", Fixture.createOrganization("test", "test"));

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));

        crf.setStudy(study);
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();

        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion1 = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion1.setProCtcQuestion(proCtcQuestion1);

        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion2 = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion2.setProCtcQuestion(proCtcQuestion5);

        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion1);
        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion2);

        studyParticipantCrf.setCrf(crf);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);

        newCrf = Fixture.createCrf();
        newCrf.setCrfVersion("2.0");
        newCrf.setId(2);
        newCrf.setStatus(CrfStatus.RELEASED);
        newCrf.setEffectiveStartDate(new Date());
        newCrf.addCrfPage(proCtcQuestion1);

        newCrf.setStudy(study);
        StudyParticipantCrf newStudyParticipantCrf = new StudyParticipantCrf();
        newStudyParticipantCrf.setCrf(newCrf);
        studyParticipantAssignment.addStudyParticipantCrf(newStudyParticipantCrf);

    }

    public void testConstructor() {

        assertNull(command.getStudyParticipantCrfSchedule());
        assertEquals("", command.getDirection());
        assertNull(command.getFlashMessage());
        assertEquals(0, command.getCurrentPageIndex());
        assertEquals(0, command.getTotalPages());
        assertNull(command.getDeletedQuestions());
        assertEquals(0, command.getDisplayRules().size());
    }

    public void testSetStudyParticipantCrfSchedule() {
        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        assertEquals(studyParticipantCrfSchedule.getId(), command.getStudyParticipantCrfSchedule().getId());
        assertEquals(2, command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size());
        assertEquals(command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getCrf().getId(), crf.getId());
        assertEquals(2, command.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions().size());


        crf.setNextVersionId(2);


        expect(finderRepository.findById(CRF.class, new Integer(2))).andReturn(newCrf);
        expect(genericRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(null);
        expect(genericRepository.save(isA(StudyParticipantCrf.class))).andReturn(null);
        genericRepository.delete(isA(StudyParticipantCrfSchedule.class));
        replayMocks();

        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

        assertNull(command.getStudyParticipantCrfSchedule().getId());
        assertEquals(command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getCrf().getId(), newCrf.getId());
        assertEquals(1, command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size());
        assertEquals(1, command.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions().size());

    }

    public void testgetCurrentPageIndex() {
        command.setCurrentPageIndex(2);
        command.setTotalPages(10);

        command.setDirection("back");
        assertEquals(1, command.getCurrentPageIndex());
        assertEquals("", command.getDirection());

        command.setDirection("continue");
        assertEquals(2, command.getCurrentPageIndex());
        assertEquals("", command.getDirection());

        command.setDirection("back_review");
        assertEquals(10, command.getCurrentPageIndex());
        assertEquals("", command.getDirection());

    }

    public void testGetArrangedQuestions() {
        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, 1)).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(2);
        replayMocks();
        Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = command.getArrangedQuestions();
        assertEquals(1, arrangedQuestions.size());
        assertFalse(arrangedQuestions.containsKey("Fatigue"));
        assertFalse(arrangedQuestions.containsKey("Pain"));
        assertTrue(arrangedQuestions.containsKey("Cough"));
        assertEquals(4, arrangedQuestions.get("Cough").size());

        command.setHasParticipantAddedQuestions(true);
        arrangedQuestions = command.getArrangedQuestions();
        assertEquals(1, arrangedQuestions.size());
        assertFalse(arrangedQuestions.containsKey("Fatigue"));
        assertFalse(arrangedQuestions.containsKey("Pain"));
        assertTrue(arrangedQuestions.containsKey("Cough"));
        assertEquals(3, arrangedQuestions.get("Cough").size());

        verifyMocks();
    }

    public void testInitialize() {

        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        command.initialize();
        assertEquals(6, command.getTotalPages());
        assertEquals(1, command.getCurrentPageIndex());
        assertEquals(5, command.getParticipantAddedQuestionIndex());
        assertTrue(command.getDisplayRules().containsKey(1));
        assertEquals("~-10", command.getDisplayRules().get(1));
        assertEquals(studyParticipantCrfSchedule, command.getStudyParticipantCrfSchedule());
    }


}