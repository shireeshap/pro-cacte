package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

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

     public boolean validate(Object value) {
        return validate(value, true, true);
    }

    public boolean validate(Object value, boolean validateFirstName, boolean validateLastName) {
        if (value instanceof ClinicalStaff) {
            ClinicalStaff staff = (ClinicalStaff) value;
            if (validateFirstName) {
                if(staff.getFirstName().equals(null)){
                    message = "Firstname cannot be empty.";
                    return false;
                }
                if(!validateName(staff.getFirstName())){
                    message = "Firstname should contain only alphabets or special character ' .";
                    return false;
                }
            }
            if (validateLastName) {
               if(staff.getLastName().equals(null)){
                   message = "Lastname cannot be empty.";
                    return false;
                }
                if(!validateName(staff.getLastName())) {
                    message = "Lastname should contain only alphabets or special character ' .";
                    return false;
                }
            }
        }
        return true;
    }
    public boolean validateName(String name)
    {
        if(name == null)
            return false;

        Pattern pattern = Pattern.compile("^[a-zA-Z ]+[a-zA-Z'']+$");
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

    public String message() {
        return message;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
