package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

//
/**
 * The Class ClinicalStaff.
 *
 * @author mehul
 */

@Entity
@Table(name = "USERS")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {@Parameter(name = "sequence", value = "seq_users_id")})


public class User extends BaseVersionable implements UserDetails {


    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "password", nullable = true)
    private String password;


    @Column(name = "user_name", nullable = false)
    private String username;


    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;


    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "number_of_attempts", nullable = false)
    private Integer numberOfAttempts = 0;

    @Transient
    private GrantedAuthority[] grantedAuthorities;

    @Transient
    private String confirmPassword;

    @Transient
    private String newPassword;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<UserRole> userRoles = new ArrayList<UserRole>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<UserNotification> userNotifications = new ArrayList<UserNotification>();

    @Column(name = "salt")
    private String salt;

    @Column(name = "token")
    private String token;

    @Column(name = "token_time")
    private Timestamp tokenTime;

    @Column(name = "password_last_set")
    private Timestamp passwordLastSet = new Timestamp(new Date().getTime());


    @Column(name = "num_failed_logins")
    private Integer failedLoginAttempts;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    protected Date lastLoginAttemptTime;

    public User(final String username, final String password, final boolean enabled,
                final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked) {

        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;

    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Timestamp tokenTime) {
        this.tokenTime = tokenTime;
    }

    public Timestamp getPasswordLastSet() {
        return passwordLastSet;
    }

    public void setPasswordLastSet(Timestamp passwordLastSet) {
        this.passwordLastSet = passwordLastSet;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Transient
    public long getPasswordAge() {
        if (getPasswordLastSet() == null) {
            setPasswordLastSet(new Timestamp(new Date().getTime()));
        }
        Timestamp timestamp = getPasswordLastSet();
        return (new Date().getTime() - timestamp.getTime()) / 1000;
    }


    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer numFailedLogins) {
        this.failedLoginAttempts = numFailedLogins;
    }

    public Date getLastFailedLoginAttemptTime() {
        return lastLoginAttemptTime;
    }

    public void setLastFailedLoginAttemptTime(Date lastLoginAttemptTime) {
        this.lastLoginAttemptTime = lastLoginAttemptTime;
    }


    /**
     * Calculates the time past last failed login attempt
     * This property is used in determining the account lock out
     *
     * @return seconds past last failed login attempts
     */
    @Transient
    public long getSecondsPastLastFailedLoginAttempt() {
        if (getLastFailedLoginAttemptTime() == null) return -1;
        return (new Date().getTime() - getLastFailedLoginAttemptTime().getTime()) / 1000;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GrantedAuthority[] getAuthorities() {
        if (grantedAuthorities == null) {
            grantedAuthorities = new GrantedAuthority[]{};
        }
        return grantedAuthorities;


    }

    public GrantedAuthority[] getGrantedAuthorities() {
        return getAuthorities();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setGrantedAuthorities(GrantedAuthority[] grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    public List<Integer> findAccessibleObjectIds(Class<? extends Persistable> persistableClass) {
        List<Integer> accessableObjectIds = new ArrayList<Integer>();
        for (String grantedAuthority : findAllAuthorities()) {
            String requiredPrivilege = persistableClass.getName() + ".";
            String requiredGrouPrivilege = persistableClass.getName() + ".GROUP";
            if (grantedAuthority.contains(requiredGrouPrivilege)) {
                return new ArrayList<Integer>();
            } else if (grantedAuthority.contains(requiredPrivilege)) {
                accessableObjectIds.add(Integer.valueOf(grantedAuthority.substring(requiredPrivilege.length(), grantedAuthority.length())));
            }
        }
        return accessableObjectIds;
    }

    public List<String> findAllAuthorities() {
        List<String> allAuthorites = new ArrayList<String>();

        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            allAuthorites.add(grantedAuthority.getAuthority());

        }
        return allAuthorites;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void addUserRole(UserRole userRole) {
        if (!getUserRoles().contains(userRole)) {
            userRole.setUser(this);
            getUserRoles().add(userRole);
        }
    }

    public boolean isAdmin() {
        for (UserRole userRole : userRoles) {
            if (Role.ADMIN.equals(userRole.getRole())) {
                return true;
            }
        }
        return false;
    }


    public boolean isODCOnStudy(Study study) {
        if (study != null) {
            if (study.getOverallDataCoordinator().getOrganizationClinicalStaff() != null) {
                return this.equals(study.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser());
            }
        }
        return false;
    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public Role getRoleForPasswordPolicy() {
        Role role = null;
        if (getUserRoles().size() > 0) {
            role = getUserRoles().get(0).getRole();
        }
        return role;
    }
}



