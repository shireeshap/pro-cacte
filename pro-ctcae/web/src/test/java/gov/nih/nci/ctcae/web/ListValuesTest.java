package gov.nih.nci.ctcae.web;

import junit.framework.TestCase;

import java.util.List;

import gov.nih.nci.ctcae.web.ListValues;

/**
 * @author Vinay Kumar
 * @crated Dec 11, 2008
 */
public class ListValuesTest extends TestCase {

	private ListValues listValues;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		listValues = new ListValues();

	}

	public void testParticipantSearchType() {
		List<ListValues> participantSearchType = listValues.getParticipantSearchType();
		assertEquals("must be 3 search type", Integer.valueOf(3), Integer.valueOf(participantSearchType.size()));
		ListValues values = participantSearchType.get(0);
	}

	public void testGetterAndSetter() {
		listValues.setCode("code");
		listValues.setDesc("desc");

		assertEquals("code", listValues.getCode());
		assertEquals("desc", listValues.getDesc());
	}
}
