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
    private CRF crf;
    private Study study;
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

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        CrfPageItem item1 = new CrfPageItem();
        ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item1.setProCtcQuestion(proCtcQuestion1);
        item1.setDisplayOrder(1);


        CrfPageItem item2 = new CrfPageItem();
        ProCtcQuestion proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item2.setProCtcQuestion(proCtcQuestion2);
        item2.setDisplayOrder(2);

        CrfPageItem item3 = new CrfPageItem();
        ProCtcQuestion proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcTerm(proCtcTerm2);
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item3.setProCtcQuestion(proCtcQuestion3);
        item3.setDisplayOrder(3);

        CrfPageItem item4 = new CrfPageItem();
        ProCtcQuestion proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setProCtcTerm(proCtcTerm2);
        proCtcQuestion4.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item4.setProCtcQuestion(proCtcQuestion4);
        item4.setDisplayOrder(4);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfPageItem(item1);
        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue(0);
        studyParticipantCrfItem1.setProCtcValidValue(proCtcValidValue);

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

        ProCtcQuestion proCtcQuestion5 = new ProCtcQuestion();
        ProCtcQuestion proCtcQuestion6 = new ProCtcQuestion();
        ProCtcQuestion proCtcQuestion7 = new ProCtcQuestion();
        ProCtcQuestion proCtcQuestion8 = new ProCtcQuestion();

        proCtcQuestion1.setId(1);
        proCtcQuestion2.setId(2);
        proCtcQuestion3.setId(3);
        proCtcQuestion4.setId(4);

        proCtcQuestion5.setId(5);
        proCtcQuestion5.setProCtcTerm(proCtcTerm1);
        proCtcQuestion6.setId(6);
        proCtcQuestion6.setProCtcTerm(proCtcTerm1);
        proCtcQuestion7.setId(7);
        proCtcQuestion7.setProCtcTerm(proCtcTerm2);
        proCtcQuestion8.setId(8);
        proCtcQuestion8.setProCtcTerm(proCtcTerm2);


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
        study = Fixture.createStudyWithStudySite("short", "long", "assigned",Fixture.createOrganization("test","test"));

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));

        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(crf);
        studyCrf.setStudy(study);
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStudyCrf(studyCrf);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
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

        CRF newCrf = Fixture.createCrf();
        newCrf.setCrfVersion("2.0");
        newCrf.setId(2);
        newCrf.setStatus(CrfStatus.RELEASED);
        newCrf.setEffectiveStartDate(new Date());

        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(newCrf);
        studyCrf.setStudy(study);
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStudyCrf(studyCrf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);

        crf.setNextVersionId(2);


        expect(finderRepository.findById(CRF.class, new Integer(2))).andReturn(newCrf);
        expect(genericRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(null);
        expect(genericRepository.save(isA(StudyParticipantCrf.class))).andReturn(null);
        genericRepository.delete(isA(StudyParticipantCrfSchedule.class));
        replayMocks();
        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);



    }

//    public void testInitialize() {
//
//        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
//        command.initialize();
//        assertEquals(2, command.getTotalPages());
//        assertEquals(1, command.getCurrentPageIndex());
//        assertEquals(studyParticipantCrfSchedule, command.getStudyParticipantCrfSchedule());
//    }

//    public void testSaveResponseAndGetQuestion() {
//
//        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
//
//        expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().times(3);
//
//        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId())).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().times(3);
//
//        replayMocks();
//
//        assertEquals(CrfStatus.SCHEDULED, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setCurrentPageIndex(0);
//        command.setDirection("continue");
//        command.saveResponseAndGetQuestion(finderRepository, genericRepository);
//
//        assertEquals(1, command.getCurrentPageIndex());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        command.saveResponseAndGetQuestion(finderRepository, genericRepository);
//
//        assertEquals(0, command.getCurrentPageIndex());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("save");
//        command.saveResponseAndGetQuestion(finderRepository, genericRepository);
//
//        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());
//
//        verifyMocks();
//    }
//
//    public void testGetArrangedQuestions() {
//
//        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
//
//        Hashtable<String, List<ProCtcQuestion>> h = command.getArrangedQuestions();
//
//        assertTrue(h.containsKey("Fatigue"));
//        assertTrue(h.containsKey("Pain"));
//        assertEquals(2, h.get("Fatigue").size());
//        assertEquals(2, h.get("Pain").size());
//
//
//    }
}