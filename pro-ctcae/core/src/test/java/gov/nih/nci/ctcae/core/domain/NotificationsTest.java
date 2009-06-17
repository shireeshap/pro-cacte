package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.*;

/**
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class NotificationsTest extends TestDataManager {

    public void testSaveFindDelete() {
        Date date = new Date();
        Notification notification = new Notification();
        User user = ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser();
        notification.setText("Test Notification");
        notification.setDate(date);
        UserNotification userNotification = new UserNotification();
        userNotification.setNew(true);
        userNotification.setUser(user);
        notification.addUserNotification(userNotification);

        Notification savedNotification = genericRepository.save(notification);
        commitAndStartNewTransaction();
        Notification found = genericRepository.findById(Notification.class, savedNotification.getId());

        assertNotNull(found);
        assertEquals("Test Notification", found.getText());
        assertEquals(date, found.getDate());
        assertEquals(1, found.getUserNotifications().size());

        UserNotification un = found.getUserNotifications().get(0);
        assertEquals(true, un.isNew());
        assertEquals(user, userNotification.getUser());

        genericRepository.delete(found);
        found = genericRepository.findById(Notification.class, savedNotification.getId());
        assertNull(found);


    }

}