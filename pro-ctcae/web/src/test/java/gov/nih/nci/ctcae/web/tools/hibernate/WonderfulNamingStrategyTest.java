package gov.nih.nci.ctcae.web.tools.hibernate;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.tools.hibernate.WonderfulNamingStrategy;
import gov.nih.nci.ctcae.web.WebTestCase;

import javax.security.auth.Subject;

/**
 * @author Vinay Kumar
 * @crated Dec 9, 2008
 */
public class WonderfulNamingStrategyTest extends WebTestCase {
	private static final String DC = "DON'T CARE";

	private WonderfulNamingStrategy strategy = new WonderfulNamingStrategy();

	public void testForeignKeyColumn() throws Exception {
		assertEquals("planned_activity_id", strategy.foreignKeyColumnName("plannedActivity", DC, DC, DC));
	}

	public void testTableName() throws Exception {
		assertEquals("subjects", strategy.classToTableName(Subject.class.getName()));
		assertEquals("study_participant_assignments", strategy.classToTableName(StudyParticipantAssignment.class.getName()));
		assertEquals("studies", strategy.classToTableName(Study.class.getName()));
		assertEquals("crfs", strategy.classToTableName(CRF.class.getName()));
		assertEquals("pro_ctc_valid_values", strategy.classToTableName(ProCtcValidValue.class.getName()));
	}
}
