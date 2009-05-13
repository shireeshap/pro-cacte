package gov.nih.nci.ctcae.web.rules;


import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Rhett Sutphin
 * @author Jared Flatow
 * @author Biju Joseph
 */
public class JavaMailSender extends JavaMailSenderImpl {

    public static boolean SUPRESS_MAIL_SEND_EXCEPTION = false;
    Properties properties = new Properties();
    ClassPathResource classPathResource = new ClassPathResource("mail.properties");
    private static final String SMTP_ADDRESS = "smtp.address";
    private static final String SMTP_USER = "smtp.user";
    private static final String SMTP_PORT = "smtp.port";
    private static final String SMTP_PASSWORD = "smtp.password";
    private static final String SMTP_SSL_ENABLED = "smtp.ssl_enabled";
    private static final String SYSTEM_FROM_EMAIL = "smtp.from_email";
    private static final String IS_EMAIL_HTML = "smtp.is_html";

    public JavaMailSender() throws IOException {
        properties.load(classPathResource.getInputStream());
        Properties temp = new Properties();
        if (properties.getProperty(SMTP_SSL_ENABLED) != null && properties.getProperty(SMTP_SSL_ENABLED).equals("Y")) {
            temp.setProperty("mail.smtp.auth", "true");
            temp.setProperty("mail.smtp.starttls.enable", "true");
            temp.setProperty("mail.smtp.timeout", "8500");
            setJavaMailProperties(temp);
        }

    }

    public String getHost() {
        return properties.getProperty(SMTP_ADDRESS);
    }

    public void setHost(String host) {
        throw unsupported("host");
    }

    @Override
    public String getUsername() {
        return properties.getProperty(SMTP_USER);
    }

    @Override
    public void setUsername(String username) {
        throw unsupported("username");
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty(SMTP_PORT));

    }

    public void setPort(int port) {
        throw unsupported("port");
    }

    public String getPassword() {
        return properties.getProperty(SMTP_PASSWORD);
    }

    public void setPassword(String password) {
        throw unsupported("password");
    }


    @Override
    public void send(SimpleMailMessage message) {
        String fromAddress = properties.getProperty(SYSTEM_FROM_EMAIL);
        if (!StringUtils.isBlank(fromAddress)) message.setFrom(fromAddress);
        super.send(message);
    }

    @Override
    public void send(MimeMessage message) {

        try {
            String fromAddress = properties.getProperty(SYSTEM_FROM_EMAIL);
            if (!fromAddress.equals("")) message.setFrom(new InternetAddress(fromAddress));
            super.send(message);

        } catch (MailException e) {
            throw e;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to send an email
     */
    public void sendMail(String[] to, String subject, String content, String[] attachmentFilePaths) throws Exception {
        try {
            MimeMessage message = createMimeMessage();
            message.setSubject(subject);

            // use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(content);
            for (String attachmentPath : attachmentFilePaths) {
                if (StringUtils.isNotEmpty(attachmentPath)) {
                    File f = new File(attachmentPath);
                    FileSystemResource file = new FileSystemResource(f);
                    helper.addAttachment(file.getFilename(), file);
                }
            }
            send(message);

        } catch (Exception e) {
            if (SUPRESS_MAIL_SEND_EXCEPTION) return; //supress the excetion related to email sending
            throw e;
        }
    }

    private UnsupportedOperationException unsupported(String prop) {
        return new UnsupportedOperationException(prop
                + " is set through the application configuration");
    }


    public String getFromAddress() {
        return properties.getProperty(SYSTEM_FROM_EMAIL);
    }

    public boolean isHtml() {
        String isHTML = properties.getProperty(IS_EMAIL_HTML);
        if ("Y".equals(isHTML) || "y".equals(isHTML)) {
            return true;
        }
        return false;
    }
}
