package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.easymock.EasyMock;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class SubmitFormCommandTest extends WebTestCase {

    private SubmitFormCommand command;
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    FinderRepository finderRepository;
    GenericRepository genericRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new SubmitFormCommand();
        finderRepository = registerMockFor(FinderRepository.class);
        genericRepository = registerMockFor(GenericRepository.class);
        ProCtcTerm proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Fatigue");

        ProCtcTerm proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("Pain");

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        CrfItem item1 = new CrfItem();
        ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item1.setProCtcQuestion(proCtcQuestion1);
        item1.setDisplayOrder(1);


        CrfItem item2 = new CrfItem();
        ProCtcQuestion proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item2.setProCtcQuestion(proCtcQuestion2);
        item2.setDisplayOrder(2);

        CrfItem item3 = new CrfItem();
        ProCtcQuestion proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcTerm(proCtcTerm2);
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        item3.setProCtcQuestion(proCtcQuestion3);
        item3.setDisplayOrder(3);

        CrfItem item4 = new CrfItem();
        ProCtcQuestion proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setProCtcTerm(proCtcTerm2);
        proCtcQuestion4.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        item4.setProCtcQuestion(proCtcQuestion4);
        item4.setDisplayOrder(4);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfItem(item1);
        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue(0);
        studyParticipantCrfItem1.setProCtcValidValue(proCtcValidValue);

        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
        studyParticipantCrfItem2.setCrfItem(item2);

        StudyParticipantCrfItem studyParticipantCrfItem3 = new StudyParticipantCrfItem();
        studyParticipantCrfItem3.setCrfItem(item3);

        StudyParticipantCrfItem studyParticipantCrfItem4 = new StudyParticipantCrfItem();
        studyParticipantCrfItem4.setCrfItem(item4);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem3);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem4);

    }

    public void testConstructor() {

        assertNull(command.getStudyParticipantCrfSchedule());
        assertEquals("", command.getDirection());
        assertNull(command.getFlashMessage());
        assertEquals(0, command.getCurrentPageIndex());
        assertEquals(0, command.getTotalPages());
        assertNull(command.getPages());

    }

    public void testSetStudyParticipantCrfSchedule() {

        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        assertEquals(2, command.getTotalPages());
        assertEquals(1, command.getCurrentPageIndex());
        assertEquals(studyParticipantCrfSchedule, command.getStudyParticipantCrfSchedule());
    }

    public void testSaveResponseAndGetQuestion() {

        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

        expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(3);

        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId())).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(3);

        replayMocks();

        assertEquals(CrfStatus.SCHEDULED, command.getStudyParticipantCrfSchedule().getStatus());
        
        command.setCurrentPageIndex(0);
        command.setDirection("continue");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

        assertEquals(1, command.getCurrentPageIndex());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());

        command.setDirection("back");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

        assertEquals(0, command.getCurrentPageIndex());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());

        command.setDirection("save");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());

        verifyMocks();
    }

}