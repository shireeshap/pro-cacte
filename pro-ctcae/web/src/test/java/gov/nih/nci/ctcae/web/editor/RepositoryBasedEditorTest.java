package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.TestBean;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import static org.easymock.EasyMock.expect;


/**
 * @author
 */
public class RepositoryBasedEditorTest extends AbstractTestCase {
    private static final Integer ID = 13;
    private static final TestBean city = new TestBean();

    private FinderRepository finderRepository;
    private RepositoryBasedEditor editor;

    @Override
    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();
        city.setId(ID);
        finderRepository = registerMockFor(FinderRepository.class);
        editor = new RepositoryBasedEditor(finderRepository, TestBean.class);
    }

    public void testSetAsTextWithValidId() throws Exception {
        expect(finderRepository.findById(TestBean.class, ID)).andReturn(city);

        replayMocks();
        editor.setAsText(ID.toString());
        verifyMocks();

        assertSame(city, editor.getValue());
    }

    public void testSetAsTextWithInvalidId() throws Exception {
        Integer expectedId = 23;
        expect(finderRepository.findById(TestBean.class, expectedId)).andReturn(null);

        replayMocks();
        try {
            editor.setAsText(expectedId.toString());
            fail("Exception not thrown");
        } catch (IllegalArgumentException iae) {
            verifyMocks();
            assertEquals("There is no " + TestBean.class.getSimpleName() + " with id=" + expectedId, iae.getMessage());
        }
    }

    public void testSetAsTextNull() throws Exception {
        replayMocks();
        editor.setAsText(null);
        verifyMocks();
        assertNull(editor.getValue());
    }

    public void testGetAsText() throws Exception {
        editor.setValue(city);
        assertEquals(ID.toString(), editor.getAsText());
    }

    public void testSetValueWithoutId() throws Exception {
        editor.setStrictIdChecking(true);
        try {
            editor.setValue(new TestBean());
            fail("Exception not thrown");
        } catch (IllegalArgumentException iae) {
            assertEquals("This editor can't handle values without IDs", iae.getMessage());
        }
    }

    public void testSetValueNull() throws Exception {
        replayMocks();
        editor.setValue(null);
        verifyMocks();
        assertNull(editor.getAsText());
    }
}
