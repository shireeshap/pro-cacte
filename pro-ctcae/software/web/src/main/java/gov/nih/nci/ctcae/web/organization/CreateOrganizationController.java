package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueIdentifierForOrganizationValidator;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * CreateOrganizationController class.
 *
 */
public class CreateOrganizationController extends CtcAeSimpleFormController {
	private static final String ORGANIZATION_ID = "organizationId";
    private GenericRepository genericRepository;
    private OrganizationRepository organizationRepository;
    private UniqueIdentifierForOrganizationValidator uniqueOrgIdValidator;
	private final static String IS_EDIT = "isEdit";

    public CreateOrganizationController() {
        setFormView("organization/createOrganization");
        setCommandClass(CreateOrganizationCommand.class);
        setCommandName("createOrganizationCommand");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	CreateOrganizationCommand command = new CreateOrganizationCommand();
    	String organizationId = request.getParameter(ORGANIZATION_ID);
    	if(organizationId != null) {
    		Organization organization = organizationRepository.findById(new Integer(organizationId));
    		command.setOrganization(organization);
    		request.setAttribute(IS_EDIT, true);
    	}
        return command;
    }
    
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
    	CreateOrganizationCommand createOrgCommand = (CreateOrganizationCommand) command;
    	super.onBindAndValidate(request, command, errors);
    	
    	
		boolean hasUniqueIdentifier = uniqueOrgIdValidator.validate(createOrgCommand.getOrganization().getId(), createOrgCommand.getOrganization().getNciInstituteCode());
		if(hasUniqueIdentifier) {
			errors.reject("nciInstituteCode_validation");
		}
    	
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors) throws Exception {
    	CreateOrganizationCommand createOrganizationCommand = (CreateOrganizationCommand) command;
    	save(createOrganizationCommand);
    	
    	String searchString = (String) request.getSession().getAttribute(FetchOrganizationController.ORGANIZATION_SEARCH_STRING);
    	request.setAttribute(FetchOrganizationController.ORGANIZATION_SEARCH_STRING, searchString);
    	request.setAttribute("createOrganizationCommand", command);
    	return showForm(request, errors, "organization/confirmOrganization");
    }

    private void save(CreateOrganizationCommand createOrganizationCommand) {
	   Organization organization = organizationRepository.saveOrUpdate(createOrganizationCommand.getOrganization());
	   createOrganizationCommand.setOrganization(organization);
   }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
    
    @Required
	public void setUniqueOrgIdValidator(UniqueIdentifierForOrganizationValidator uniqueOrgIdValidator) {
		this.uniqueOrgIdValidator = uniqueOrgIdValidator;
	}
}
