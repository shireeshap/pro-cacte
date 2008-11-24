package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.FormDetailsTab;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.StudyCrfAjaxFacade;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.*;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class ScheduleCrfTabTest extends WebTestCase {
    private ScheduleCrfTab tab;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tab = new ScheduleCrfTab();


    }

    public void testConstructor (){
        assertNotNull(tab);
    }

}