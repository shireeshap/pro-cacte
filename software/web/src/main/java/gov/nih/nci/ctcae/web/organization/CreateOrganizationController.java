package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author Amey
 * CreateOrganizationController class.
 *
 */
public class CreateOrganizationController extends SimpleFormController {
	private static final String ORGANIZATION_ID = "organizationId";
    private GenericRepository genericRepository;
    private OrganizationRepository organizationRepository;

    public CreateOrganizationController() {
        setFormView("organization/createOrganization");
        setCommandClass(CreateOrganizationCommand.class);
        setCommandName("createOrganizationCommand");
        setBindOnNewForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	CreateOrganizationCommand command = new CreateOrganizationCommand();
    	String organizationId = request.getParameter(ORGANIZATION_ID);
    	if(organizationId != null) {
    		Organization organization = organizationRepository.findById(new Integer(organizationId));
    		command.setOrganization(organization);
    		request.setAttribute("isEdit", true);
    	}
        return command;
    }
    
    @Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
    			HttpServletResponse response, Object command, BindException errors) throws Exception {
    	
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
}
