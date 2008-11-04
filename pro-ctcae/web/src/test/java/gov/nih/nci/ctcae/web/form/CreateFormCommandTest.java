package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.CRF;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CreateFormCommandTest extends WebTestCase {

    private CreateFormCommand command;
    private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion;
    FinderRepository finderRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new CreateFormCommand();
        finderRepository = registerMockFor(FinderRepository.class);
        firstQuestion = new ProCtcQuestion();
        firstQuestion.setQuestionText("sample question1");
        secondQuestion = new ProCtcQuestion();
        secondQuestion.setQuestionText("sample question2");

        fourthQuestion = new ProCtcQuestion();
        fourthQuestion.setQuestionText("sample question1");


    }

    public void testConstructor() {
        assertNotNull("study crf must not be null", command.getStudyCrf());
        assertNotNull("crf must not be null", command.getStudyCrf().getCrf());
        assertEquals("must have one crf", Integer.valueOf(1), Integer.valueOf(command.getStudyCrf().getCrf().getStudyCrfs().size()));

    }

    public void testUpdateQuestions() {
        command.updateCrfItems(finderRepository);
        assertTrue(command.getStudyCrf().getCrf().getCrfItems().isEmpty());
        command.setQuestionsIds("1,4,6");

        expect(finderRepository.findById(ProCtcQuestion.class, Integer.valueOf(1))).andReturn(firstQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 4)).andReturn(secondQuestion);
        expect(finderRepository.findById(ProCtcQuestion.class, 6)).andReturn(thirdQuestion);
        replay(finderRepository);
        command.updateCrfItems(finderRepository);
        verify(finderRepository);
        CRF crf = command.getStudyCrf().getCrf();
        assertEquals("must have only 2 questions because thirdQuestion is null and first and 4rth questions are same", 2,
                crf.getCrfItems().size());
        for (int i = 0; i < crf.getCrfItems().size(); i++) {
            assertEquals("must preserve order no", Integer.valueOf(i + 1), crf.getCrfItems().get(i).getDisplayOrder());

        }

    }
}
