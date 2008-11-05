package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class UniqueIdentifierForStudyValidatorTest extends AbstractTestCase {

    private UniqueIdentifierForStudyValidator validator;
    private StudyRepository studyRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        validator = new UniqueIdentifierForStudyValidator();
        studyRepository = registerMockFor(StudyRepository.class);
        validator.setStudyRepository(studyRepository);
    }

    public void testValidateUniqueIdentifier() {

        expect(studyRepository.find(isA(StudyQuery.class))).andReturn(new ArrayList());
        replayMocks();
        assertTrue("identifier does not exists", validator.validate("identifier"));
        verifyMocks();

    }

    public void testValidateNonUniqueIdentifier() {

        ArrayList list = new ArrayList();
        list.add(new Study());
        expect(studyRepository.find(isA(StudyQuery.class))).andReturn(list);
        replayMocks();
        assertFalse("identifier already exists", validator.validate("identifier"));
        verifyMocks();

    }
}
