package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

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
        MessageSource messageSource = new MessageSource() {
            public String getMessage(String s, Object[] objects, String s1, Locale locale) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        } ;
        validator.setMessageSource(messageSource);
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

    public void testValidateNameFalse(){
        clinicalStaff.setFirstName("reshma1");
        assertFalse("false",validator.validateName("reshma1"));
    }

    public void testValidateName(){
        clinicalStaff.setFirstName("reshma");
        assertTrue("false",validator.validateName("reshma"));
    }

}
