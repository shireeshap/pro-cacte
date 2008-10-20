package gov.nih.nci.ctcae.web;

import org.easymock.classextension.EasyMock;
import org.springframework.binding.convert.ConversionExecutor;
import gov.nih.nci.ctcae.core.repository.CommonRepository;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.web.ApplicationConversionService;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @crated Oct 16, 2008
 */
public class ApplicationConversionServiceTest extends AbstractTestCase {


    ApplicationConversionService service;
    private CommonRepository commonRepository;

    private Organization organization;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        commonRepository = registerMockFor(CommonRepository.class);

        service = new ApplicationConversionService(commonRepository);
        service.afterPropertiesSet();

        organization = new Organization();


    }

    public void testRegisterCustomConverterForOrganization() {
        EasyMock.expect(commonRepository.findById(Organization.class, 1)).andReturn(organization);
        replayMocks();
        ConversionExecutor conversionExecutor = service.getConversionExecutor(String.class, Organization.class);
        Organization anotherOrganization = (Organization) conversionExecutor.execute("1");
        verifyMocks();
        assertEquals(organization, anotherOrganization);

    }

    public void testRegisterCustomConverterForDate() throws ParseException {
        ConversionExecutor conversionExecutor = service.getConversionExecutor(String.class, Date.class);
        Date date = (Date) conversionExecutor.execute("07/13/2012");
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("07/13/2012"), date)));

    }

}
