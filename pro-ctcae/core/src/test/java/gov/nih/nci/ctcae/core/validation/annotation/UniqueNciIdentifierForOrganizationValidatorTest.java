package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class UniqueNciIdentifierForOrganizationValidatorTest extends AbstractTestCase {

    private UniqueNciIdentifierForOrganizationValidator validator;
    private OrganizationRepository organizationRepository;
    private Organization organization;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        validator = new UniqueNciIdentifierForOrganizationValidator();
        organizationRepository = registerMockFor(OrganizationRepository.class);
        validator.setOrganizationRepository(organizationRepository);
        organization = new Organization();
    }

    public void testValidateUniqueIdentifier() {

        expect(organizationRepository.find(isA(OrganizationQuery.class))).andReturn(new ArrayList());
        replayMocks();
        assertTrue("identifier does not exists", validator.validate("identifier"));
        verifyMocks();

    }

    public void testInitialzie() {

        BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(organization);
        Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor("nciInstituteCode").getReadMethod().getAnnotations();
        assertFalse("must find annotation", annotationsArray.length == 0);
        assertTrue("must find UniqueTitleForCrf annotation", annotationsArray[0].annotationType().equals(UniqueNciIdentifierForOrganization.class));


//        validator.initialize(annotationsArray[0]);
//        assertEquals("identifier does not exists", validator.message());

    }

    public void testValidateReturnTrueForWrongValue() {

        assertTrue("identifier does not exists", validator.validate(organization));

    }

    public void testValidateNonUniqueIdentifier() {

        ArrayList list = new ArrayList();
        list.add(new Organization());
        expect(organizationRepository.find(isA(OrganizationQuery.class))).andReturn(list);
        replayMocks();
        assertFalse("identifier already exists", validator.validate("identifier"));
        verifyMocks();

    }
}
