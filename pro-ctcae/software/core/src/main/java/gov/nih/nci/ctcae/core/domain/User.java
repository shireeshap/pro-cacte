package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "password", nullable = false)
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

    @Transient
    private GrantedAuthority[] grantedAuthorities;

    @Transient
    private String confirmPassword;

    public User(final String username, final String password, final boolean enabled,
                final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked) {

        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;

    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<UserRole> userRoles = new ArrayList<UserRole>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<UserNotification> userNotifications = new ArrayList<UserNotification>();

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
        if (!userRole.isPersisted() && !getUserRoles().contains(userRole)) {
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

}