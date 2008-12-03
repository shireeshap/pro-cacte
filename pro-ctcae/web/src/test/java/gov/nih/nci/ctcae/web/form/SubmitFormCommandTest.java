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

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        CrfItem item1 = new CrfItem();
        item1.setDisplayOrder(1);

        CrfItem item2 = new CrfItem();
        item2.setDisplayOrder(2);

        CrfItem item3 = new CrfItem();
        item3.setDisplayOrder(3);

        CrfItem item4 = new CrfItem();
        item4.setDisplayOrder(4);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfItem(item1);
        studyParticipantCrfItem1.setProCtcValidValue(new ProCtcValidValue());

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
        //assertEquals(0, command.getCurrentIndex());
        //assertEquals(0, command.getTotalQuestions());

    }

    public void testSetStudyParticipantCrfSchedule() {

        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

        //assertEquals(4, command.getTotalQuestions());
        //assertEquals(1, command.getCurrentIndex());
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
        
        //command.setCurrentIndex(0);
        command.setDirection("continue");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

       // assertEquals(1, command.getCurrentIndex());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());

        command.setDirection("back");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

       // assertEquals(0, command.getCurrentIndex());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());

        command.setDirection("save");
        command.saveResponseAndGetQuestion(finderRepository, genericRepository);

        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());

        verifyMocks();
    }

}