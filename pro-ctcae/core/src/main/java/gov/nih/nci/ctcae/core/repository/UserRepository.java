package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.RolePrivilegeQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.security.DomainObjectPrivilegeGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.util.*;

//
/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserRepository implements UserDetailsService, Repository<User, UserQuery> {

    private SaltSource saltSource;
    private PasswordEncoder passwordEncoder;
    protected Log logger = LogFactory.getLog(getClass());

    private GenericRepository genericRepository;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

        DomainObjectPrivilegeGenerator privilegeGenerator = new DomainObjectPrivilegeGenerator();
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
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        List<GrantedAuthority> instanceGrantedAuthorities = new ArrayList<GrantedAuthority>();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserId(user.getId());
        ClinicalStaff clinicalStaff = (ClinicalStaff) genericRepository.findSingle(clinicalStaffQuery);

        List<OrganizationClinicalStaff> organizationClinicalStaffs = clinicalStaff.getOrganizationClinicalStaffs();
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            List<String> privileges = privilegeGenerator.generatePrivilege(organizationClinicalStaff);
            for (String privilege : privileges) {
                instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
            }
        }

        if (clinicalStaff != null) {

            StudyOrganizationClinicalStaffQuery studyOrganizationClinicalStaffQuery = new StudyOrganizationClinicalStaffQuery();
            studyOrganizationClinicalStaffQuery.filterByClinicalStaffId(clinicalStaff.getId());
            List<StudyOrganizationClinicalStaff> StudyOrganizationClinicalStaffs = genericRepository.find(studyOrganizationClinicalStaffQuery);

            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : StudyOrganizationClinicalStaffs) {
                Role role = studyOrganizationClinicalStaff.getRole();
                roles.add(role);
                List<String> privileges = privilegeGenerator.generatePrivilege(studyOrganizationClinicalStaff);
                for (String privilege : privileges) {
                    instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                }
            }


            if (!roles.isEmpty()) {
                RolePrivilegeQuery rolePrivilegeQuery = new RolePrivilegeQuery();
                rolePrivilegeQuery.filterByRoles(roles);
                List<Privilege> privileges = genericRepository.find(rolePrivilegeQuery);

                for (Privilege privilege : privileges) {
                    GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(privilege.getPrivilegeName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
        }
        grantedAuthorities.addAll(instanceGrantedAuthorities);
        user.setGrantedAuthorities(grantedAuthorities.toArray(new GrantedAuthority[]{}));
        return user;
    }

    public User findById(Integer id) {
        return genericRepository.findById(User.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User save(User user) {
        if (user.getId() == null) {
            String encoded = getEncodedPassword(user);
            user.setPassword(encoded);
        }

        user = genericRepository.save(user);
        return user;

    }

    public void delete(User user) {


    }

    public Collection<User> find(UserQuery query) {
        return genericRepository.find(query);


    }

    public User findSingle(UserQuery query) {
        return genericRepository.findSingle(query);


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

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}