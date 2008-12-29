package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.easymock.EasyMock;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class SubmitFormCommandTest extends WebTestCase {

//    private SubmitFormCommand command;
//    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
//    FinderRepository finderRepository;
//    GenericRepository genericRepository;
//    List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>();
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        command = new SubmitFormCommand();
//        finderRepository = registerMockFor(FinderRepository.class);
//        genericRepository = registerMockFor(GenericRepository.class);
//        ProCtcTerm proCtcTerm1 = new ProCtcTerm();
//        proCtcTerm1.setTerm("Fatigue");
//
//        ProCtcTerm proCtcTerm2 = new ProCtcTerm();
//        proCtcTerm2.setTerm("Pain");
//
//        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
//        CrfItem item1 = new CrfItem();
//        ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
//        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
//        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
//        item1.setProCtcQuestion(proCtcQuestion1);
//        item1.setDisplayOrder(1);
//
//
//        CrfItem item2 = new CrfItem();
//        ProCtcQuestion proCtcQuestion2 = new ProCtcQuestion();
//        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
//        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
//        item2.setProCtcQuestion(proCtcQuestion2);
//        item2.setDisplayOrder(2);
//
//        CrfItem item3 = new CrfItem();
//        ProCtcQuestion proCtcQuestion3 = new ProCtcQuestion();
//        proCtcQuestion3.setProCtcTerm(proCtcTerm2);
//        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
//        item3.setProCtcQuestion(proCtcQuestion3);
//        item3.setDisplayOrder(3);
//
//        CrfItem item4 = new CrfItem();
//        ProCtcQuestion proCtcQuestion4 = new ProCtcQuestion();
//        proCtcQuestion4.setProCtcTerm(proCtcTerm2);
//        proCtcQuestion4.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
//        item4.setProCtcQuestion(proCtcQuestion4);
//        item4.setDisplayOrder(4);
//
//        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
//        studyParticipantCrfItem1.setCrfItem(item1);
//        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
//        proCtcValidValue.setValue(0);
//        studyParticipantCrfItem1.setProCtcValidValue(proCtcValidValue);
//
//        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
//        studyParticipantCrfItem2.setCrfItem(item2);
//
//        StudyParticipantCrfItem studyParticipantCrfItem3 = new StudyParticipantCrfItem();
//        studyParticipantCrfItem3.setCrfItem(item3);
//
//        StudyParticipantCrfItem studyParticipantCrfItem4 = new StudyParticipantCrfItem();
//        studyParticipantCrfItem4.setCrfItem(item4);
//
//
//        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
//        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
//        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem3);
//        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem4);
//
//        ProCtcQuestion proCtcQuestion5 = new ProCtcQuestion();
//        ProCtcQuestion proCtcQuestion6 = new ProCtcQuestion();
//        ProCtcQuestion proCtcQuestion7 = new ProCtcQuestion();
//        ProCtcQuestion proCtcQuestion8 = new ProCtcQuestion();
//
//        proCtcQuestion1.setId(1);
//        proCtcQuestion2.setId(2);
//        proCtcQuestion3.setId(3);
//        proCtcQuestion4.setId(4);
//
//        proCtcQuestion5.setId(5);
//        proCtcQuestion5.setProCtcTerm(proCtcTerm1);
//        proCtcQuestion6.setId(6);
//        proCtcQuestion6.setProCtcTerm(proCtcTerm1);
//        proCtcQuestion7.setId(7);
//        proCtcQuestion7.setProCtcTerm(proCtcTerm2);
//        proCtcQuestion8.setId(8);
//        proCtcQuestion8.setProCtcTerm(proCtcTerm2);
//
//
//
//        questions.add(proCtcQuestion1);
//        questions.add(proCtcQuestion2);
//        questions.add(proCtcQuestion3);
//        questions.add(proCtcQuestion4);
//        questions.add(proCtcQuestion5);
//        questions.add(proCtcQuestion6);
//        questions.add(proCtcQuestion7);
//        questions.add(proCtcQuestion8);
//        command.setProCtcQuestions(questions);
//
//
//
//    }
//
//    public void testConstructor() {
//
//        assertNull(command.getStudyParticipantCrfSchedule());
//        assertEquals("", command.getDirection());
//        assertNull(command.getFlashMessage());
//        assertEquals(0, command.getCurrentPageIndex());
//        assertEquals(0, command.getTotalPages());
//        assertNull(command.getPages());
//
//    }
//
//    public void testSetStudyParticipantCrfSchedule() {
//
//        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
//        assertEquals(2, command.getTotalPages());
//        assertEquals(1, command.getCurrentPageIndex());
//        assertEquals(studyParticipantCrfSchedule, command.getStudyParticipantCrfSchedule());
//    }
//
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
//    public void testGetArrangedQuestions(){
//
//        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
//
//       Hashtable<String, List<ProCtcQuestion>> h = command.getArrangedQuestions();
//
//        assertTrue(h.containsKey("Fatigue"));
//        assertTrue(h.containsKey("Pain"));
//        assertEquals(2,h.get("Fatigue").size());
//        assertEquals(2,h.get("Pain").size());
//
//
//    }
}