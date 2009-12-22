package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.UserNotificationQuery;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.List;

/**
 * User: Harsh
 * Date: Jun 24, 2009
 * Time: 2:53:03 PM
 */
public class RemoveAlertControllerTest extends AbstractWebTestCase {

    public void testRemoveAlert() throws Exception {
        request.setMethod("GET");
        List<UserNotification> list = genericRepository.find(new UserNotificationQuery());
        String uuid = "";
        UserNotification un = null;
        for (UserNotification a : list) {
            if (!a.getMarkDelete()) {
                uuid = a.getUuid();
                un = a;
                break;
            }
        }

        if (uuid.equals("")) {
            fail("No notification found for deletion");
        }

        RemoveAlertController controller = new RemoveAlertController();
        controller.setGenericRepository(genericRepository);

        request.setParameter("uuid", uuid);
        controller.handleRequest(request, response);

        un = genericRepository.findById(UserNotification.class, un.getId());
        assertTrue(un.isMarkDelete());


    }

}
