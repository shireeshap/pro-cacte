package gov.nih.nci.ctcae.web;

import org.easymock.classextension.EasyMock;
import org.springframework.binding.convert.ConversionExecutor;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
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
    private FinderRepository finderRepository;

    private Organization organization;
    private Study study;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        finderRepository = registerMockFor(FinderRepository.class);

        service = new ApplicationConversionService(finderRepository);
        service.afterPropertiesSet();

        organization = new Organization();
        study = new Study();

    }

    public void testRegisterCustomConverterForOrganization() {
        EasyMock.expect(finderRepository.findById(Organization.class, 1)).andReturn(organization);
        replayMocks();
        ConversionExecutor conversionExecutor = service.getConversionExecutor(String.class, Organization.class);
        Organization anotherOrganization = (Organization) conversionExecutor.execute("1");
        verifyMocks();
        assertEquals(organization, anotherOrganization);

    }

    public void testRegisterCustomConverterForStudy() {
        EasyMock.expect(finderRepository.findById(Study.class, 1)).andReturn(study);
        replayMocks();
        ConversionExecutor conversionExecutor = service.getConversionExecutor(String.class, Study.class);
        Study anotherStudy = (Study) conversionExecutor.execute("1");
        verifyMocks();
        assertEquals(study, anotherStudy);

    }

    public void testRegisterCustomConverterForDate() throws ParseException {
        ConversionExecutor conversionExecutor = service.getConversionExecutor(String.class, Date.class);
        Date date = (Date) conversionExecutor.execute("07/13/2012");
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("07/13/2012"), date)));

    }

}
