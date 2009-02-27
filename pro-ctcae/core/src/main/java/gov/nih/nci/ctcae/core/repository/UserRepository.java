package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.RolePrivilegeQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//
/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserRepository extends AbstractRepository<User, UserQuery> implements UserDetailsService {

    private SaltSource saltSource;
    private PasswordEncoder passwordEncoder;

    protected Class<User> getPersistableClass() {
        return User.class;


    }


    public User loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName);
        List<User> users = new ArrayList<User>(find(userQuery));

        if (users.isEmpty() || users.size() > 1) {
            String errorMessage = String.format("user %s not found", userName);
            logger.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
        User user = users.get(0);

        Set<Role> roles = new HashSet<Role>();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserId(user.getId());
        ClinicalStaff clinicalStaff = (ClinicalStaff) genericRepository.findSingle(clinicalStaffQuery);
        for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {

            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaff.getStudyOrganizationClinicalStaffs()) {
                Role role = studyOrganizationClinicalStaff.getRole();
                roles.add(role);
            }

        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        if (!roles.isEmpty()) {
            RolePrivilegeQuery rolePrivilegeQuery = new RolePrivilegeQuery();
            rolePrivilegeQuery.filterByRoles(roles);
            List<Privilege> privileges = (List<Privilege>) genericRepository.find(rolePrivilegeQuery);

            for (Privilege privilege : privileges) {
                GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(privilege.getPrivilegeName());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        user.setGrantedAuthorities(grantedAuthorities.toArray(new GrantedAuthority[]{}));
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            String encoded = getEncodedPassword(user);
            user.setPassword(encoded);
        }

        user = super.save(user);
        return user;

    }

    private String getEncodedPassword(final User user) {
        if (!StringUtils.isBlank(user.getPassword())) {
            Object salt = saltSource.getSalt(user);
            String encoded = passwordEncoder.encodePassword(user.getPassword(), salt);
            return encoded;
        } else return null;
    }

    @Required
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    @Required
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}