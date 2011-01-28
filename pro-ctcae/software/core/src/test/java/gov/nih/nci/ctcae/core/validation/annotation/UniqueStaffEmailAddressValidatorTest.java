package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;

import java.util.ArrayList;
import java.util.Collection;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 28, 2011
 * Time: 2:19:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class UniqueStaffEmailAddressValidatorTest extends AbstractTestCase{
    private UniqueStaffEmailAddressValidator validator;
    private ClinicalStaffRepository clinicalStaffRepository;
    private ClinicalStaff clinicalStaff;
    Collection<ClinicalStaff> clinicalStaffs;
    
    public void UniqueStaffEmailAddressValidatorTest(){

    }

    public void setUp() throws Exception{
        super.setUp();
        validator = new UniqueStaffEmailAddressValidator();
        clinicalStaffRepository = registerMockFor(ClinicalStaffRepository.class);
        validator.setClinicalStaffRepository(clinicalStaffRepository);
        clinicalStaff = new ClinicalStaff();
        clinicalStaffs = new ArrayList<ClinicalStaff>();
        clinicalStaff.setEmailAddress("reshma.koganti@gmail.com");
    }

    public void testValidateStaffEmail(){
        String emailAddress ="reshma.koganti@gmail.com";
        Integer staffId=null;
        expect(clinicalStaffRepository.find(isA(ClinicalStaffQuery.class))).andReturn(clinicalStaffs).anyTimes();
        clinicalStaffs.add(clinicalStaff);
        replayMocks();
        assertTrue("Email Address already exists in database", validator.validateStaffEmail(emailAddress,staffId));
        verifyMocks();
    }
}
