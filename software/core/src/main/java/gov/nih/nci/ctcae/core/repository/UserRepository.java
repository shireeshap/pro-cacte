package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.exception.UsernameAlreadyExistsException;
import gov.nih.nci.ctcae.core.query.*;
import gov.nih.nci.ctcae.core.security.DomainObjectPrivilegeGenerator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;
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

import java.sql.Timestamp;
import java.util.*;

//

/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class UserRepository implements UserDetailsService, Repository<User, UserQuery> {

    private SaltSource saltSource;
    private PasswordEncoder passwordEncoder;
    protected Log logger = LogFactory.getLog(getClass());
    protected Properties proCtcAEProperties;
    private GenericRepository genericRepository;
    private boolean checkAccountLockout = true;
    PasswordPolicyService passwordPolicyService;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

        DomainObjectPrivilegeGenerator privilegeGenerator = new DomainObjectPrivilegeGenerator();
        List<User> users = findByUserName(userName);

        if (users.isEmpty() || users.size() > 1) {
            String errorMessage = String.format("Could not find user %s", userName);
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

        ClinicalStaff clinicalStaff = findClinicalStaffForUser(user);

        if (clinicalStaff != null) {
            List<OrganizationClinicalStaff> organizationClinicalStaffs = clinicalStaff.getOrganizationClinicalStaffs();
            for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
                Set<String> privileges = privilegeGenerator.generatePrivilege(organizationClinicalStaff);
                privileges.addAll(privilegeGenerator.generatePrivilege(organizationClinicalStaff.getOrganization()));
                for (String privilege : privileges) {
                    instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                }
            }
            
            //To allow CCA's access to only studies that are associated with their organization
            if(roles.contains(Role.CCA)){
            	for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            		List<StudyOrganization> studyOrganizationList = findStudyOrganizations(organizationClinicalStaff);
            		for (StudyOrganization studyOrganization : studyOrganizationList) {
            			Set<String> privileges = privilegeGenerator.generatePrivilege(studyOrganization.getStudy());
                        for (String privilege : privileges) {
                            instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                        }
            		}
                }
            }

            StudyOrganizationClinicalStaffQuery studyOrganizationClinicalStaffQuery = new StudyOrganizationClinicalStaffQuery();

            if (SecuredQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
                throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
            }
            studyOrganizationClinicalStaffQuery.filterByClinicalStaffId(clinicalStaff.getId());
            studyOrganizationClinicalStaffQuery.filterByActiveStatus();
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
        Participant participant = findParticipantForUser(user);
        if (participant != null) {
            Set<String> privileges = privilegeGenerator.generatePrivilege(participant);
            for (String privilege : privileges) {
                instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
            }
        }

        PasswordPolicy passwordPolicy;
        if (!roles.isEmpty()) {
            passwordPolicy = passwordPolicyService.getPasswordPolicy(roles.iterator().next());

            RolePrivilegeQuery rolePrivilegeQuery = new RolePrivilegeQuery();
            if (RolePrivilegeQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
                throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
            }
            if (!user.isAdmin()) {
                rolePrivilegeQuery.filterByRoles(roles);
            } else {
            	rolePrivilegeQuery.filterForAdmin();
            }
            List<Privilege> privileges = genericRepository.find(rolePrivilegeQuery);

            for (Privilege privilege : privileges) {
                GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(privilege.getPrivilegeName());
                grantedAuthorities.add(grantedAuthority);
            }
        } else {
            passwordPolicy = passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy());
        }
        long passwordAge = user.getPasswordAge();
        if (passwordAge > passwordPolicy.getLoginPolicy().getMaxPasswordAge()) {
            throw new MyException("Password expired");
        }
        checkAccountLock(user, passwordPolicy);

        grantedAuthorities.addAll(instanceGrantedAuthorities);
        user.setGrantedAuthorities(grantedAuthorities.toArray(new GrantedAuthority[]{}));
        if (clinicalStaff == null && participant == null && !user.isAdmin()) {
            throw new MyException("User is inactive");
        }

//        if (participant != null){
//            for(StudyParticipantMode mode : participant.getStudyParticipantAssignments().get(0).getStudyParticipantModes()) {
//                if (mode.getMode().equals(AppMode.IVRS)){
//                      throw new MyException("Use of web not activated");
//                }
//            }
//        }
      
        return findByUserName(user.getUsername()).get(0);
    }

    public List<User> findByUserName(String userName) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName);
        List<User> users = new ArrayList<User>(find(userQuery));
        return users;
    }

    private void checkAccountLock(User user, PasswordPolicy passwordPolicy) {
        if (checkAccountLockout) {
            Integer numOfAttempts = user.getNumberOfAttempts();
            if (numOfAttempts == null) {
                numOfAttempts = 0;
            }
            int lockout = passwordPolicy.getLoginPolicy().getAllowedFailedLoginAttempts();
            int allowedAttempts = lockout;
            user.setAccountNonLocked(true);
            if (numOfAttempts >= allowedAttempts) {
                user.setAccountNonLocked(false);
            }
            user.setNumberOfAttempts(numOfAttempts + 1);
            DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
            DataAuditInfo.setLocal(auditInfo);
            user = genericRepository.save(user);
        }
    }

    public User findByUserToken(String token) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserToken(token);
        return genericRepository.findSingle(userQuery);
    }


    public User findById(Integer id) {
        return genericRepository.findById(User.class, id);


    }
    //TODO:SA Needs to remove after Clinical Staff refracted
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User save(User user) {
        if (user.getUsername() == null) {
            throw new CtcAeSystemException("Username cannot be empty. Please provide a valid username.");
        }
        user.setUsername(user.getUsername().toLowerCase());
        if (user.getId() == null) {
            List<User> users = findByUserName(user.getUsername());
            if (users.size() > 0) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
            String encoded = getEncodedPassword(user);
            user.setPassword(encoded);
        } else {
            String myPassword = user.getPassword();
            if (myPassword != null) {
                User temp = findById(user.getId());
                String currentPassword = temp.getPassword();
                if (!myPassword.equals(currentPassword)) {
                    user.setPassword(getEncodedPassword(user));
                }
            }
        }

        user.setPasswordLastSet(new Timestamp(new Date().getTime()));
        user = genericRepository.save(user);
        user.setConfirmPassword(user.getConfirmPassword());
        return user;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOrUpdate(User _user) {
        if (_user.getUsername() == null) {
            throw new CtcAeSystemException("Username cannot be empty. Please provide a valid username.");
        }
        
        User user = _user;
        
        if (_user.getId() == null) {
            _user.setUsername(_user.getUsername().toLowerCase());
            List<User> users = findByUserName(_user.getUsername());
            if (users.size() > 0) {
                throw new UsernameAlreadyExistsException(_user.getUsername());
            }
            String encoded = getEncodedPassword(_user);
            user.setPassword(encoded);
            user.setConfirmPassword(_user.getPassword());
            user.setPasswordLastSet(new Timestamp(new Date().getTime()));
            
        } else {
            String myPassword = _user.getPassword();
            if (myPassword != null) {
                user = findById(_user.getId());
                String currentPassword = user.getPassword();
                if (!myPassword.equals(currentPassword)) {
                    String encoded = getEncodedPassword(_user);
                    _user.setPassword(encoded);
                    _user.setConfirmPassword(encoded);
                    user.setPassword(encoded);
                    user.setConfirmPassword(encoded);
                    user.setPasswordLastSet(new Timestamp(new Date().getTime()));
                    _user.setPasswordLastSet(user.getPasswordLastSet());
                }
            }
        }

        genericRepository.saveOrUpdate(user);

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User saveWithoutCheck(User user) {
        user.setPassword(getEncodedPassword(user));
        user.setPasswordLastSet(new Timestamp(new Date().getTime()));
        user = genericRepository.save(user);
        user.setConfirmPassword(user.getPassword());
        return user;
    }

    public void delete(User user) {
        genericRepository.delete(user);
    }


    public List<User> find(UserQuery query) {
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

    public String getEncodedPassword(final User user) {
        if (!StringUtils.isBlank(user.getPassword())) {
            Object salt = saltSource.getSalt(user);
            return passwordEncoder.encodePassword(user.getPassword(), salt);
        }
        return null;
    }

    public ClinicalStaff findClinicalStaffForUser(User user) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserName(user.getUsername());
        return genericRepository.findSingle(clinicalStaffQuery);

    }
    
    /**
     * Find study organizations associated with the given OrganizationClinicalStaff.
     *
     * @param organizationClinicalStaff the organization clinical staff
     * @return the list
     */
    public List<StudyOrganization> findStudyOrganizations(OrganizationClinicalStaff organizationClinicalStaff){
    	StudyOrganizationQuery organizationClinicalStaffQuery = new StudyOrganizationQuery();
    	organizationClinicalStaffQuery.filterByDataCoordinatingCenterId(organizationClinicalStaff.getOrganization().getId());
    	return genericRepository.find(organizationClinicalStaffQuery);
    }

    public Participant findParticipantForUser(User user) {
        ParticipantQuery query = new ParticipantQuery();
        query.filterByUsername(user.getUsername());
        return genericRepository.findSingle(query);
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

    @Required
    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }

    public void setCheckAccountLockout(boolean checkAccountLockout) {
        this.checkAccountLockout = checkAccountLockout;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }
}

class MyException extends DataAccessException {

    public MyException(String s) {
        super(s);
    }

}