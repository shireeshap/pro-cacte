package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * @author Mehul Gulati
 *         Date: Dec 3, 2009
 */
public class EffectiveStaffController extends CtcAeSimpleFormController {

    ClinicalStaffRepository clinicalStaffRepository;
    private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";

    protected EffectiveStaffController() {
        super();
        setCommandClass(ClinicalStaff.class);
        setFormView("clinicalStaff/effectiveStaff");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Integer cId = Integer.parseInt(request.getParameter("cId"));
        ClinicalStaff clinicalStaff = clinicalStaffRepository.findById(cId);
        lazyInitializeClinicalStaff(clinicalStaff);
        clinicalStaff.getUser().getUserRoles().size();
        return clinicalStaff;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ClinicalStaff clinicalStaff = (ClinicalStaff) command;
        RoleStatus currentStatus = clinicalStaff.getStatus();

        if (currentStatus.equals(RoleStatus.ACTIVE)) {
            clinicalStaff.deactivateClinicalStaff();
            
        } else {
            clinicalStaff.activateClinicalStaff(null);
        }

        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        String searchString = (String) request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING);
        RedirectView redirectView ;
        if(searchString!= null && !searchString.equals("")){
             redirectView = new RedirectView("../searchClinicalStaff?searchString="+searchString);
        }else{
             redirectView = new RedirectView("../searchClinicalStaff");
        }

        return new ModelAndView(redirectView);
    }
    
    public void lazyInitializeClinicalStaff(ClinicalStaff clinicalStaff){
    	for(OrganizationClinicalStaff ocs : clinicalStaff.getOrganizationClinicalStaffs()){
    		for(StudyOrganizationClinicalStaff socs : ocs.getStudyOrganizationClinicalStaff()){
    			socs.getId();
    		}
    	}
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}
