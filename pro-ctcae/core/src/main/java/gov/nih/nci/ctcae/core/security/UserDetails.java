package gov.nih.nci.ctcae.core.security;

import org.springframework.security.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public class UserDetails implements org.springframework.security.userdetails.UserDetails {


    private List<AclEntry> aclEntries = new ArrayList<AclEntry>();

    public List<AclEntry> getAclEntries() {
        return aclEntries;
    }

    public void setAclEntries(List<AclEntry> aclEntries) {
        this.aclEntries = aclEntries;
    }

    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[0];
    }

    public String getPassword() {
        return null;


    }

    public String getUsername() {
        return null;


    }

    public boolean isAccountNonExpired() {
        return false;


    }

    public boolean isAccountNonLocked() {
        return false;


    }

    public boolean isCredentialsNonExpired() {
        return false;


    }

    public boolean isEnabled() {
        return false;


    }
}
