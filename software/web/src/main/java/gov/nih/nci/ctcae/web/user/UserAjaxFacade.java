package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.domain.QueryStrings;

import java.util.List;

/**
 * @author mehul
 * Date: 2/9/12
 */
public class UserAjaxFacade {
    private UserRepository userRepository;

    public List<UserNotification> searchNotifications(Integer startIndex, Integer results, String sortField, String direction, String userName) {
        UserQuery userQuery = new UserQuery(QueryStrings.UN_QUERY_BASIC);
        userQuery.setFirstResult(startIndex);
        userQuery.setMaximumResults(results);
        userQuery.setSortBy("un.notification." + sortField);
        userQuery.setSortDirection(direction);
        userQuery.filterNotificationByUserName(userName);
        List<UserNotification> notifications = (List<UserNotification>) userRepository.findNotificationForUser(userQuery);
        return notifications;
    }

    public Long resultCount(String userName) {
        UserQuery userQuery = new UserQuery(QueryStrings.UN_QUERY_COUNT);
        userQuery.filterNotificationByUserName(userName);
        return userRepository.findWithCount(userQuery);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
