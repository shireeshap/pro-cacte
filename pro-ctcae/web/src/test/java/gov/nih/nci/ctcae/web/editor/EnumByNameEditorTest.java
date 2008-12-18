package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.ctcae.core.domain.CrfItemAllignment;
import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Dec 18, 2008
 */
public class EnumByNameEditorTest extends TestCase {
	private EnumByNameEditor<CrfItemAllignment> editor = new EnumByNameEditor<CrfItemAllignment>(CrfItemAllignment.class);

	public void testSetAsText() throws Exception {
		editor.setAsText(CrfItemAllignment.VERTICAL.getDisplayName());
		assertSame(CrfItemAllignment.VERTICAL, editor.getValue());
	}

	public void testSetAsTextfTextDoesNotExists() throws Exception {
		editor.setAsText("invalid value");
		assertNull(editor.getValue());
	}

	public void testSetAsTextNull() throws Exception {
		editor.setAsText(null);
		assertNull(editor.getValue());
	}

	public void testSetAsTextBlank() throws Exception {
		editor.setAsText("");
		assertNull(editor.getValue());
	}

	public void testGetAsText() throws Exception {
		editor.setValue(CrfItemAllignment.HORIZONTAL);
		assertEquals(CrfItemAllignment.HORIZONTAL.getDisplayName(), editor.getAsText());
	}

	public void testGetAsTextNull() throws Exception {
		editor.setValue(null);
		assertNull(editor.getAsText());
	}


}
