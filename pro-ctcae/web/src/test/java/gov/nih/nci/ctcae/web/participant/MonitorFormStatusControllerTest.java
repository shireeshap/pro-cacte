package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.helper.*;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class MonitorFormStatusControllerTest extends AbstractWebTestCase {

    public void testController(){
        Study s = StudyTestHelper.getDefaultStudy();
        
    }

}