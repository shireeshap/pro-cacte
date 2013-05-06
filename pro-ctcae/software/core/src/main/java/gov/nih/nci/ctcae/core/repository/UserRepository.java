package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.domain.UserPasswordHistory;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.exception.UsernameAlreadyExistsException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.RolePrivilegeQuery;
import gov.nih.nci.ctcae.core.query.SecuredQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.security.DomainObjectPrivilegeGenerator;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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


/**
 * The Class CRFRepository.
 *
 * @author Vinay Gangoli
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
        Set<Role> roles = new HashSet<Role>();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        List<GrantedAuthority> instanceGrantedAuthorities = new ArrayList<GrantedAuthority>();

        User user = getUserByUserName(userName);
        addRolesToUser(user, roles);

        if (ClinicalStaffQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
            throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
        }

        ClinicalStaff clinicalStaff = generateInstanceGrantedAuthoritiesForNonAdminUsers(user, privilegeGenerator, roles, instanceGrantedAuthorities);
        Participant participant = generateInstanceGrantedAuthoritiesForParticipants(user, privilegeGenerator, instanceGrantedAuthorities);
        generateGrantedAuthoritiesForRole(user, roles, grantedAuthorities);
        
        checkPasswordExpirationAndLock(user, roles);
        
        grantedAuthorities.addAll(instanceGrantedAuthorities);
        user.setGrantedAuthorities(grantedAuthorities.toArray(new GrantedAuthority[]{}));
        
        for(Role role: roles){
        	user.addUserRole(new UserRole(role));
        }        
        if (clinicalStaff == null && participant == null && !user.isAdmin()) {
            throw new MyException("User is inactive");
        }
        //return user;
        return findByUserName(user.getUsername()).get(0);
    }

    private void checkPasswordExpirationAndLock(User user, Set<Role> roles) {
    	PasswordPolicy passwordPolicy;
        if (!roles.isEmpty()) {
            passwordPolicy = passwordPolicyService.getPasswordPolicy(roles.iterator().next());
        } else {
            passwordPolicy = passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy());
        }
        
        long passwordAge = user.getPasswordAge();
        if (passwordAge > passwordPolicy.getLoginPolicy().getMaxPasswordAge()) {
            throw new MyException("Password expired");
        }
        checkAccountLock(user, passwordPolicy);		
	}

	private List<GrantedAuthority> generateGrantedAuthoritiesForRole(User user, Set<Role> roles, List<GrantedAuthority> grantedAuthorities) {
    	if (roles.isEmpty()) {
    		return grantedAuthorities;
    	}
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
        return grantedAuthorities;
	}

	private Participant generateInstanceGrantedAuthoritiesForParticipants(User user, DomainObjectPrivilegeGenerator privilegeGenerator, List<GrantedAuthority> instanceGrantedAuthorities) {
        //check for participant
        Participant participant = findParticipantForUser(user);
        if (participant != null) {
            Set<String> privileges = privilegeGenerator.generatePrivilege(participant);
            for (String privilege : privileges) {
                instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
            }
        }
        return participant;
	}

	private ClinicalStaff generateInstanceGrantedAuthoritiesForNonAdminUsers(User user, DomainObjectPrivilegeGenerator privilegeGenerator, Set<Role> roles, List<GrantedAuthority> instanceGrantedAuthorities) {
        ClinicalStaff clinicalStaff = null;
        //only need to load orgs and studies for non admin staff
        if (!roles.contains(Role.ADMIN)) {
            clinicalStaff = findClinicalStaffForUser(user);
            if (clinicalStaff != null) {
            	//assign all org & staff access to lead roles and CCA
            	if(roles.contains(Role.CCA) || roles.contains(Role.LEAD_CRA) || roles.contains(Role.PI)){
            		instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilegeGenerator.generateGroupPrivilege(Organization.class)));
            		instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilegeGenerator.generateGroupPrivilege(ClinicalStaff.class)));
            	}
            	
                //assign all the orgs the staff can access
                List<OrganizationClinicalStaff> organizationClinicalStaffs = clinicalStaff.getOrganizationClinicalStaffs();
                for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
                    Set<String> privileges = privilegeGenerator.generatePrivilege(organizationClinicalStaff);
                    privileges.addAll(privilegeGenerator.generatePrivilege(organizationClinicalStaff.getOrganization()));
                    for (String privilege : privileges) {
                        instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                    }
                }

                //To allow CCA's access to only studies that are associated with their organization
                List<Study> studyList;
                if (roles.contains(Role.CCA)) {
                    for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
                    	studyList = findAllStudiesAssociatedWithOrganization(organizationClinicalStaff);
                        for (Study study : studyList) {
                            Set<String> privileges = privilegeGenerator.generatePrivilege(study);
                            for (String privilege : privileges) {
                                instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                            }
                        }
                    }
                }

                //for staff that also have non-cca or non-admin roles, assign studies based on their studyOrg assignments
                StudyOrganizationClinicalStaffQuery studyOrganizationClinicalStaffQuery = new StudyOrganizationClinicalStaffQuery();
                if (SecuredQuery.class.isAssignableFrom(StudyOrganizationClinicalStaffQuery.class)) {
                    throw new CtcAeSystemException("query used to retrieve user must not be secured query. ");
                }
                studyOrganizationClinicalStaffQuery.filterByClinicalStaffId(clinicalStaff.getId());
                studyOrganizationClinicalStaffQuery.filterByActiveStatus();
                List<StudyOrganizationClinicalStaff> StudyOrganizationClinicalStaffs = genericRepository.find(studyOrganizationClinicalStaffQuery);
                Set<String> privileges;
                for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : StudyOrganizationClinicalStaffs) {
                    roles.add(studyOrganizationClinicalStaff.getRole());
                    privileges = privilegeGenerator.generatePrivilege(studyOrganizationClinicalStaff);
                    for (String privilege : privileges) {
                        instanceGrantedAuthorities.add(new GrantedAuthorityImpl(privilege));
                    }
                }
            }
        }	
        return clinicalStaff;
	}

	private void addRolesToUser(User user, Set<Role> roles) {
        boolean isAdminOrCCAOrParticipant = user.hasRole(Role.ADMIN, Role.CCA, Role.PARTICIPANT);  
        for (UserRole userRole : user.getUserRoles()) {
        	//for all roles (except ADMIN and CCA and Participant) check if user is Active on atleast one of the assigned studies.
        	// then only add the roles which are active on atleast one study
        	if(!isAdminOrCCAOrParticipant){
	            if(isRoleActive(user.getId(), userRole.getRole()))
	            	roles.add(userRole.getRole());
        	} else {
        		roles.add(userRole.getRole());
        	}
        }		
	}

	private User getUserByUserName(String userName) {
        List<User> users = findByUserName(userName);
        if (users.isEmpty() || users.size() > 1) {
            String errorMessage = String.format("Could not find user %s", userName);
            throw new UsernameNotFoundException(errorMessage);
        }
        return users.get(0);
	}

	public List<User> findByUserName(String userName) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName);
        List<User> users = new ArrayList<User>(find(userQuery));
        return users;
    }

    private boolean isRoleActive(Integer userId, Role roleName){
    	UserQuery query = new UserQuery(QueryStrings.SOCS_QUERY_COUNT);
    	query.filterInActiveRoles(userId, roleName);
        return ((genericRepository.findWithCount(query) > 0 )? true : false);
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
    	PasswordPolicy passwordPolicy = passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy());
        String encoded = getEncodedPassword(user);

        if (user.getId() == null) {
            List<User> users = findByUserName(user.getUsername());
            if (users.size() > 0) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
    		user.setUserPasswordWithSalting(passwordPolicy, encoded);
    	} else {
            String myPassword = user.getPassword();
            if (myPassword != null) {
                User temp = findById(user.getId());
                String currentPassword = temp.getPassword();
                if (!myPassword.equals(currentPassword)) {
            		user.setUserPasswordWithSalting(passwordPolicy, encoded);                
            	}
            }
        }
        user.setPasswordLastSet(new Timestamp(new Date().getTime()));
        user = genericRepository.save(user);
        user.setConfirmPassword(user.getConfirmPassword());
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOrUpdate(User user, String newEncodedPassword) {
        String existingPwdEncoded = user.getPassword();
        String username = user.getUsername();

        if (existingPwdEncoded != null) {
            if (!existingPwdEncoded.equals(newEncodedPassword)) {
            	PasswordPolicy passwordPolicy = passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy());
        		user.setUserPasswordWithSalting(passwordPolicy, newEncodedPassword);
        		
        		user.setConfirmPassword(newEncodedPassword);
                user.setPasswordLastSet(new Timestamp(new Date().getTime()));
            }
            if (!StringUtils.isEmpty(username)) {
                user.setUsername(username.toLowerCase());
            }
        }
        genericRepository.saveOrUpdate(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User saveWithoutCheck(User user, boolean updateHistory) {
    	if(updateHistory){
        	PasswordPolicy passwordPolicy = passwordPolicyService.getPasswordPolicy(user.getRoleForPasswordPolicy());
    		user.setUserPasswordWithSalting(passwordPolicy, getEncodedPassword(user));
    	}
    	user.setPasswordLastSet(new Timestamp(new Date().getTime()));
        user = genericRepository.save(user);
        user.setConfirmPassword(user.getPassword());
        return user;
    }
    
    public String getEncodedPassword(final User user) {
        return getEncodedPassword(user, user.getPassword());
    }
    
    public String getEncodedPassword(final User user, String password) {
        if (!StringUtils.isBlank(password)) {
            Object salt = saltSource.getSalt(user);
            return passwordEncoder.encodePassword(password, salt);
        }
        return null;
    }
    
    public ClinicalStaff findClinicalStaffForUser(User user) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserName(user.getUsername());
        return genericRepository.findSingle(clinicalStaffQuery);

    }
    
    public void delete(User user) {
        genericRepository.delete(user);
    }


    public List<User> find(UserQuery query) {
        return genericRepository.find(query);
    }

    public List<UserNotification> findNotificationForUser(UserQuery query) {
        return genericRepository.find(query);
    }

    public Long findWithCount(UserQuery query) {
        return genericRepository.findWithCount(query);
    }

    public Collection<User> getByRole(Role role) {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserRole(role);
        return find(userQuery);
    }

    public User findSingle(UserQuery query) {
        return genericRepository.findSingle(query);
    }

    /**
     * Find study organizations associated with the given OrganizationClinicalStaff.
     *
     * @param organizationClinicalStaff the organization clinical staff
     * @return the list
     */
    public List<StudyOrganization> findStudyOrganizations(OrganizationClinicalStaff organizationClinicalStaff) {
        StudyOrganizationQuery organizationClinicalStaffQuery = new StudyOrganizationQuery();
        organizationClinicalStaffQuery.filterByDataCoordinatingCenterId(organizationClinicalStaff.getOrganization().getId());
        return genericRepository.find(organizationClinicalStaffQuery);
    }

    public Participant findParticipantForUser(User user) {
        ParticipantQuery query = new ParticipantQuery(false);
        query.filterByUsername(user.getUsername());
        return genericRepository.findSingle(query);
    }
    
    public List<Study> findAllStudiesAssociatedWithOrganization(OrganizationClinicalStaff organizationClinicalStaff){
    	Integer siteId = organizationClinicalStaff.getOrganization().getId();
    	StudyQuery studyQuery = new StudyQuery(QueryStrings.STUDY_QUERY_BASIC, false);
    	studyQuery.filterStudiesForStudySite(siteId);
    	return genericRepository.find(studyQuery);
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