package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

//
/**
 * The Class UniqueIdentifierForStudyValidator.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class UniqueUserNameValidator extends AbstractValidator<UniqueUserName> {

    /**
     * The message.
     */
    private String message;

    /**
     * The study repository.
     */
    private UserRepository userRepository;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
    public boolean validate(Object value) {
        if (value instanceof User) {
            User user = (User) value;
            if (user.getId() == null) {
                UserQuery userQuery = new UserQuery();
                userQuery.filterByUserName(user.getUsername());
                List<User> users = new ArrayList<User>(userRepository.find(userQuery));
                return (users == null || users.isEmpty()) ? true : false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(UniqueUserName parameters) {
        message = parameters.message();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
     */
    public String message() {
        return message;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}