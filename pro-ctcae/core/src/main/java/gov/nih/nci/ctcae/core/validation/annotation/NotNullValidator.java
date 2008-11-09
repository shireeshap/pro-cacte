package gov.nih.nci.ctcae.core.validation.annotation;

/**
 * Checks a not null restriction on an object
 *
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
public class NotNullValidator implements Validator<NotNull> {

    String message;

    public boolean validate(final Object value) {
        return value != null;
    }

    public void initialize(final NotNull parameters) {
        this.message = parameters.message();
    }

    public String message() {
        return this.message;
    }
}
