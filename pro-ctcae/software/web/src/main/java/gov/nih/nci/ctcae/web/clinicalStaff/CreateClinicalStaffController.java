package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.validation.annotation.FirstAndLastNameValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueStaffEmailAddressValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.clinicalStaff.notifications.ClinicalStaffNotificationPublisher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

//

/**
 * The Class CreateClinicalStaffController.
 *
 * @author Mehul Gulati
 */
public class CreateClinicalStaffController extends CtcAeSimpleFormController {

    // same controller for edit clinicalstaff

    /**
     * The clinical staff repository.
     */
    private ClinicalStaffRepository clinicalStaffRepository;
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";
    private UserNameAndPasswordValidator userNameAndPasswordValidator;
    private FirstAndLastNameValidator firstAndLastNameValidator;
    private GenericRepository genericRepository;
    private UserRepository userRepository;
    private UniqueStaffEmailAddressValidator uniqueStaffEmailAddressValidator;
    private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
    private ClinicalStaffNotificationPublisher proctcaeEventPublisher;

    /**
     * Instantiates a new creates the clinical staff controller.
     */
    public CreateClinicalStaffController() {
        super();
        setCommandClass(ClinicalStaffCommand.class);
        setCommandName("clinicalStaffCommand");
        setFormView("clinicalStaff/createClinicalStaff");
        setSuccessView("clinicalStaff/confirmClinicalStaff");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map map = super.referenceData(request, command, errors);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean cca = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.CCA)) {
                cca = true;
            }
        }
        if (errors.hasErrors())
        map.put("error", true);
        map.put("cca", cca);
        map.put("isAdmin", user.isAdmin());
        map.put("isCCA", user.isCCA());
        return map;


    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        boolean hasExistingUserAccount = false;
    	ClinicalStaffCommand clinicalStaffCommand = (ClinicalStaffCommand) oCommand;
    	if(isEditFlow(request)){
    		hasExistingUserAccount = hasUserAccount(clinicalStaffCommand.getClinicalStaff().getId());
    	}
        clinicalStaffCommand.apply();
        save(clinicalStaffCommand);
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("clinicalStaffCommand", clinicalStaffCommand);
        if (clinicalStaffCommand.getUserAccount() && !hasExistingUserAccount) {
        	ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        	String link = StringUtils.replace(request.getRequestURL().toString(), "pages/admin/createClinicalStaff", "public/resetPassword");
        	proctcaeEventPublisher.publishClinicalStaffNotification(link, clinicalStaff);
        }
		String searchString = (String) request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING);
		if(StringUtils.isEmpty(searchString)){
			searchString = "%";
		}
		 modelAndView.addObject(CLINICAL_STAFF_SEARCH_STRING, searchString);
		
        return modelAndView;
    }

    private void save(ClinicalStaffCommand clinicalStaffCommand) {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);
    }
    
    public boolean hasUserAccount(Integer id){
    	ClinicalStaff cs = clinicalStaffRepository.findById(id);
    	if(cs.getUser() != null){
    		return true;
    	}
    	return false;
    }
    
    public boolean isEditFlow(HttpServletRequest request){
    	return (request.getParameter("isEdit") != null) && "true".equals(request.getParameter("isEdit"));
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String clinicalStaffId = request.getParameter(CLINICAL_STAFF_ID);
        ClinicalStaffCommand clinicalStaffCommand = new ClinicalStaffCommand();
        if (clinicalStaffId != null) {
            ClinicalStaff clinicalStaff = clinicalStaffRepository.findById(new Integer(clinicalStaffId));
            clinicalStaffCommand.setClinicalStaff(clinicalStaff);
        }
        return clinicalStaffCommand;
    }


    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        ClinicalStaffCommand command = (ClinicalStaffCommand) o;
        removeDeletedOrganizationalClinicalStaff(command, e);
        super.onBindAndValidate(request, o, e);

        if (command.getClinicalStaff().getOrganizationClinicalStaffs().size() == 0) {
            if (!command.isAdmin()) {
                e.rejectValue("clinicalStaff.organizationClinicalStaffs", "clinicalStaff.no_organization", "clinicalStaff.no_organization");
            }
        }

        // checking for first name and last name
        boolean validName = firstAndLastNameValidator.validate(command.getClinicalStaff(), true, true);
        if (!validName) {
            if (firstAndLastNameValidator.message().contains("First name")) {
                e.rejectValue("clinicalStaff.firstName", "firstName_validation", "firstName_validation");
            } else if (firstAndLastNameValidator.message().contains("Last name")) {
                e.rejectValue("clinicalStaff.lastName", "lastName_validation", "lastName_validation");
            }
        }

        // checking for unique email address
        if (command.getClinicalStaff().getEmailAddress() != null) {
            boolean validEmail = uniqueStaffEmailAddressValidator.validateStaffEmail(command.getClinicalStaff().getEmailAddress(), command.getClinicalStaff().getId());
            if (validEmail) {
                e.rejectValue("clinicalStaff.emailAddress", "clinicalStaff.unique_emailAddress", "clinicalStaff.unique_emailAddress");
            }
        }
        if (command.getUserAccount()) {
            User user = command.getClinicalStaff().getUser();
            if (user == null) {
                user = new User();
                command.getClinicalStaff().setUser(user);
            }
            user.setUsername(command.getUsername());
            boolean validUser = userNameAndPasswordValidator.validate(user, true, false);
            if (!validUser) {
                e.rejectValue("username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
            }
        }
        
        // checking for duplicate study site
        HashSet<OrganizationClinicalStaff> organizationClinicalStaffs = new HashSet<OrganizationClinicalStaff>();
        int index = 0;
        for(OrganizationClinicalStaff ocs : command.getClinicalStaff().getOrganizationClinicalStaffs()){
        	if(!organizationClinicalStaffs.add(ocs)){
        		String duplicateStudySiteErrorMsg = "Duplicate site" + ": " + ocs.getOrganization().getDisplayName();
        		e.rejectValue("clinicalStaff.organizationClinicalStaffs[" + index +"]", "" , duplicateStudySiteErrorMsg);
        	}
        	index++;
        }
    }

    private void removeDeletedOrganizationalClinicalStaff(ClinicalStaffCommand command, BindException e) {
        ClinicalStaff clinicalStaff = command.getClinicalStaff();
        List<OrganizationClinicalStaff> ocsToRemove = new ArrayList<OrganizationClinicalStaff>();
        List<OrganizationClinicalStaff> ocsNonAllowedToBeRemove = new ArrayList<OrganizationClinicalStaff>();
        
        for (Integer index : command.getIndexesToRemove()) {
            ocsToRemove.add(clinicalStaff.getOrganizationClinicalStaffs().get(index));
        }
        
        for (OrganizationClinicalStaff organizationClinicalStaff : ocsToRemove) {
            OrganizationClinicalStaff temp = genericRepository.findById(OrganizationClinicalStaff.class, organizationClinicalStaff.getId());
            if (temp != null && temp.getStudyOrganizationClinicalStaff().size() > 0) {
                	ocsNonAllowedToBeRemove.add(temp);
            } else {
            	clinicalStaff.getOrganizationClinicalStaffs().remove(organizationClinicalStaff);
            }
        }
        
        for(OrganizationClinicalStaff ocs : ocsNonAllowedToBeRemove){
        	 e.reject("NON_EMPTY_SITE", "Cannot delete site " + ocs.getOrganization().getDisplayName() + ". Clinical staff is assigned to some study on this site.");
        }
        command.getIndexesToRemove().clear();
    }

    /**
     * Sets the clinical staff repository.
     *
     * @param clinicalStaffRepository the new clinical staff repository
     */
    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        this.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setUniqueStaffEmailAddressValidator(UniqueStaffEmailAddressValidator uniqueStaffEmailAddressValidator) {
        this.uniqueStaffEmailAddressValidator = uniqueStaffEmailAddressValidator;
    }
    
    @Required
    public void setProctcaeEventPublisher(ClinicalStaffNotificationPublisher proctcaeEventPublisher){
    	this.proctcaeEventPublisher = proctcaeEventPublisher;
    }

    public void setFirstAndLastNameValidator(FirstAndLastNameValidator firstAndLastNameValidator) {
        this.firstAndLastNameValidator = firstAndLastNameValidator;
    }
}
