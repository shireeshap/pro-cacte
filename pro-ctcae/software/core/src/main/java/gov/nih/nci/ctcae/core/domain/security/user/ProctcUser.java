package gov.nih.nci.ctcae.core.domain.security.user;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "proctc_user")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_caaers_user_id") })
public class ProctcUser {
    private String name;

    private String salt;

    private String token;

    private Timestamp tokenTime;

    private Timestamp passwordLastSet;

    private String passwordHistory;

    private int numFailedLogins;

    public static ProctcUser getUser(String userName) throws Exception {
        // throw exception if doesn't exist
        return null;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "token_time")
    public Timestamp getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Timestamp tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Column(name = "password_last_set")
    private Timestamp getPasswordLastSet() {
        return passwordLastSet;
    }

    private void setPasswordLastSet(Timestamp passwordLastSet) {
        this.passwordLastSet = passwordLastSet;
    }

    @Transient
    public long getPasswordAge() {
        // current time - last set
        return new Date().getTime() - getPasswordLastSet().getTime();
    }

    @Column(name = "password_history")
    private String getPasswordHistory() {
        return passwordHistory;
    }

    private void setPasswordHistory(String passwordHistory) {
        this.passwordHistory = passwordHistory;
    }

    public void addPasswordToHistory(int maxHistorySize) {
        // expand password history
        // if >= length -> chop to length - 1
        // add password
    }

    @Column(name = "num_failed_logins")
    public int getFailedLoginAttempts() {
        return numFailedLogins;
    }

    public void setFailedLoginAttempts(int numFailedLogins) {
        this.numFailedLogins = numFailedLogins;
    }

    public void setPassword(String hashedPassword) {
        // csm_user
        this.setPasswordLastSet(new Timestamp(new Date().getTime()));
    }

    public boolean isPassword(String hashedPassword) {
        // compare to csm_user
        return false;
    }
}
