package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ParticipantQuery.
 *
 * @author mehul
 */

public class UserNotificationQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT un from UserNotification un";

    public UserNotificationQuery() {

        super(queryString);
    }

    public void filterByUUID(String uuid) {
        if (uuid != null) {
            andWhere("uuid =:" + "UUID");
            setParameter("UUID", uuid);
        }
    }
}