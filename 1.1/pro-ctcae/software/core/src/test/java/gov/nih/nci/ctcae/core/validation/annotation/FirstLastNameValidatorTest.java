package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 5, 2011
 * Time: 4:37:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstLastNameValidatorTest extends AbstractTestCase {
    private FirstAndLastNameValidator validator;
    private ClinicalStaff clinicalStaff;

    public void FirstLastNameValidatorTest(){

    }

    public void setUp() throws Exception{
        super.setUp();
        validator = new FirstAndLastNameValidator();
        clinicalStaff = new ClinicalStaff();

    }

    public void testValidate(){
        clinicalStaff.setFirstName("reshma");
        clinicalStaff.setLastName("koganti");
        assertTrue("True",validator.validate(clinicalStaff,true,true));
    }

    public void testValidateFalse(){
        clinicalStaff.setFirstName("reshma1");
        clinicalStaff.setLastName("kogant1i");
        assertFalse("false",validator.validate(clinicalStaff,true,true));

    }

}
