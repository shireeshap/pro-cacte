package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.ctcae.core.TestBean;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import static org.easymock.EasyMock.expect;


/**
 * @author
 */
public class RepositoryBasedEditorTest extends AbstractTestCase {
	private static final Integer ID = 13;
	private static final TestBean testBean = new TestBean();

	private FinderRepository finderRepository;
	private RepositoryBasedEditor editor;

	@Override
	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception {
		super.setUp();
		testBean.setId(ID);
		finderRepository = registerMockFor(FinderRepository.class);
		editor = new RepositoryBasedEditor(finderRepository, TestBean.class);
	}

	public void testSetAsTextWithValidId() throws Exception {
		expect(finderRepository.findById(TestBean.class, ID)).andReturn(testBean);

		replayMocks();
		editor.setAsText(ID.toString());
		verifyMocks();

		assertSame(testBean, editor.getValue());
	}

	public void testSetNullForBlanks() throws Exception {
		editor.setNullForBlanks(true);
		editor.setAsText("");
		assertNull("must return null for blank if nullForBlanks is true", editor.getValue());

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

	public void testSetValueWithInvalidId() throws Exception {
		Integer expectedId = 23;
		try {
			editor.setValue(expectedId.toString());
			fail("Exception not thrown");
		} catch (IllegalArgumentException iae) {
			assertEquals("This editor only handles instances of " + TestBean.class.getName(), iae.getMessage());
		}
	}

	public void testSetAsTextNull() throws Exception {
		replayMocks();
		editor.setAsText(null);
		verifyMocks();
		assertNull(editor.getValue());
	}

	public void testGetAsText() throws Exception {
		editor.setValue(testBean);
		assertEquals(ID.toString(), editor.getAsText());
	}

	public void testGetAsTextForNullId() throws Exception {
		testBean.setId(null);
		editor.setValue(testBean);
		assertNull(editor.getAsText());
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
