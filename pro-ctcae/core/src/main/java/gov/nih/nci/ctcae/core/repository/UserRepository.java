package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.exception.UsernameAlreadyExistsException;
import gov.nih.nci.ctcae.core.query.*;
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

        for (UserRole userRole : user.getUserRoles()) {
            roles.add(userRole.getRole());
        }
        if (ClinicalStaffQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
            throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
        }

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserId(user.getId());
        ClinicalStaff clinicalStaff = (ClinicalStaff) genericRepository.findSingle(clinicalStaffQuery);


        if (clinicalStaff != null) {

            List<OrganizationClinicalStaff> organizationClinicalStaffs = clinicalStaff.getOrganizationClinicalStaffs();
            for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
                Set<String> privileges = privilegeGenerator.generatePrivilege(organizationClinicalStaff);
                privileges.addAll(privilegeGenerator.generatePrivilege(organizationClinicalStaff.getOrganization()));
                for (String privilege : privileges) {
                    instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                }
            }


            StudyOrganizationClinicalStaffQuery studyOrganizationClinicalStaffQuery = new StudyOrganizationClinicalStaffQuery();

            if (SecuredQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
                throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
            }
            studyOrganizationClinicalStaffQuery.filterByClinicalStaffId(clinicalStaff.getId());
            List<StudyOrganizationClinicalStaff> StudyOrganizationClinicalStaffs = genericRepository.find(studyOrganizationClinicalStaffQuery);

            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : StudyOrganizationClinicalStaffs) {
                Role role = studyOrganizationClinicalStaff.getRole();
                roles.add(role);
                Set<String> privileges = privilegeGenerator.generatePrivilege(studyOrganizationClinicalStaff);
                for (String privilege : privileges) {
                    instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                }
            }


        }

        //check for participant

        ParticipantQuery query = new ParticipantQuery();
        query.filterByUsername(user.getUsername());
        Participant participant = (Participant) genericRepository.findSingle(query);
        if (participant != null) {
            Set<String> privileges = privilegeGenerator.generatePrivilege(participant);
            for (String privilege : privileges) {
                instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
            }
        }


        if (!roles.isEmpty()) {
            RolePrivilegeQuery rolePrivilegeQuery = new RolePrivilegeQuery();
            if (RolePrivilegeQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
                throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
            }
            rolePrivilegeQuery.filterByRoles(roles);
            List<Privilege> privileges = genericRepository.find(rolePrivilegeQuery);

            for (Privilege privilege : privileges) {
                GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(privilege.getPrivilegeName());
                grantedAuthorities.add(grantedAuthority);
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
        user.setUsername(user.getUsername().toLowerCase());
        if (user.getId() == null) {
            UserQuery userQuery = new UserQuery();
            userQuery.filterByUserName(user.getUsername());
            List<User> users = new ArrayList<User>(find(userQuery));
            if (users.size() > 0) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
        }
        String encoded = getEncodedPassword(user);
        user.setPassword(encoded);

        user = genericRepository.save(user);
        return user;

    }

    public void delete(User user) {


    }


    public Collection<User> find(UserQuery query) {
        return genericRepository.find(query);


    }

    public Collection<User> getByRole(Role role) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserRole(role);
        return find(userQuery);


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