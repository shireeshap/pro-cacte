package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueStaffEmailAddressValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private GenericRepository genericRepository;
    private UserRepository userRepository;
    private UniqueStaffEmailAddressValidator uniqueStaffEmailAddressValidator;

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

        return map;


    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        ClinicalStaffCommand clinicalStaffCommand = (ClinicalStaffCommand) oCommand;
        clinicalStaffCommand.apply();
        save(clinicalStaffCommand);
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("clinicalStaffCommand", clinicalStaffCommand);
        if (clinicalStaffCommand.getUserAccount() && "false".equals(request.getParameter("isEdit"))) {
                clinicalStaffCommand.sendEmailWithUsernamePasswordDetails(userRepository, request);
        }
        return modelAndView;
    }

    private void save(ClinicalStaffCommand clinicalStaffCommand) {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);
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

        // checking for unique email address
        if (command.getClinicalStaff().getId()==null && command.getClinicalStaff().getEmailAddress()!=null) {
            boolean validEmail = uniqueStaffEmailAddressValidator.validateStaffEmail(command.getClinicalStaff().getEmailAddress(),command.getClinicalStaff().getId());
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
                e.rejectValue("clinicalStaff.user.username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
            }
        }
    }

    private void removeDeletedOrganizationalClinicalStaff(ClinicalStaffCommand command, BindException e) {
        ClinicalStaff clinicalStaff = command.getClinicalStaff();
        List<OrganizationClinicalStaff> ocsToRemove = new ArrayList<OrganizationClinicalStaff>();
        for (Integer index : command.getIndexesToRemove()) {
            ocsToRemove.add(clinicalStaff.getOrganizationClinicalStaffs().get(index));
        }
        for (OrganizationClinicalStaff organizationClinicalStaff : ocsToRemove) {
            OrganizationClinicalStaff temp = genericRepository.findById(OrganizationClinicalStaff.class, organizationClinicalStaff.getId());
            if (temp != null) {
                if (temp.getStudyOrganizationClinicalStaff().size() > 0) {
                    e.reject("NON_EMPTY_SITE", "Cannot delete site " + organizationClinicalStaff.getOrganization().getDisplayName() + ". Clinical staff is assigned to some study on this site.");
                    return;
                }
            }
            clinicalStaff.getOrganizationClinicalStaffs().remove(organizationClinicalStaff);
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
}