package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.User;

/**
 * @author mehul gulati
 * Date: May 20, 2010
 */
public class ResetPasswordCommand {

    private String username;
    private User user;
    private String password;
    private String confirmPassword;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
