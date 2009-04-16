package gov.nih.nci.ctcae.web.rules;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * User: Harsh
 * Date: Apr 16, 2009
 * Time: 10:37:24 PM
 */
public class SendMailTest extends TestCase {
    public void testSendMail() throws IOException {
        String[] mailTo = new String[2];
        mailTo[0] = "harsh.agarwal@semanticbits.com";
        mailTo[1] = "mehul.gulati@semanticbits.com";

        try {
            NotificationsEvaluationService.sendMail(mailTo, "Test email","Test Content");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
