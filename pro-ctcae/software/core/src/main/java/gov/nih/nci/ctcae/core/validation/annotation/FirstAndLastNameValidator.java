package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 5, 2011
 * Time: 11:24:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class FirstAndLastNameValidator extends AbstractValidator<FirstAndLastName>{
    /**
         * The message.
         */
        private String message;
    /**
     *  The pattern
     * Matches which are not alphabets and special character ' 
     */
        public static Pattern pattern =Pattern.compile("^[a-zA-Z ]+[a-zA-Z'']+$");
    /**
     * The messageSource
     */
        protected MessageSource messageSource;
    /**
     *
     * @param value
     * @return true if error exists and vise versa
     */
     public boolean validate(Object value) {

        return validate(value, true, true);
    }
    /**
     * @return boolean value
     * True if first/last name is empty or mismatch with the given pattern. 
     * @param value
     * @param validateFirstName to validate firstName
     * @param validateLastName to validate lastName
     *
     */
    public boolean validate(Object value, boolean validateFirstName, boolean validateLastName) {
        if (value instanceof ClinicalStaff) {
            ClinicalStaff staff = (ClinicalStaff) value;
            if (validateFirstName) {
                if(staff.getFirstName().equals(null)){
                    message = "Firstname cannot be empty.";
                    return false;
                }
                if(!validateName(staff.getFirstName())){
                    message =messageSource.getMessage("firstName_validation",new Object[] {}, Locale.getDefault());
                    return false;
                }
            }
            if (validateLastName) {
               if(staff.getLastName().equals(null)){
                   message = "Lastname cannot be empty.";
                    return false;
                }
                if(!validateName(staff.getLastName())) {
                    message =messageSource.getMessage("lastName_validation",new Object[] {}, Locale.getDefault());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param name The name
     * @return true if pattern matches the name and vise versa
     */     
    public boolean validateName(String name)
    {
        if(name == null)
            return false;


        Matcher matcher = pattern.matcher(name);
            if(matcher.matches())
            {
                return true;
            }
            else
                return false;
    }
    
    public void initialize(FirstAndLastName parameters) {
        message = parameters.message();
    }

    /**
	 *
	 *  the message to set
	 */
    public String message() {
        return message;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
	 * @param messageSource
	 *            the messageSource to set
	 */
    @Required
    public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
