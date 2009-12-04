package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.RoleStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;


/**
 * @author Mehul Gulati
 *         Date: Dec 3, 2009
 */
public class EffectiveStaffController extends CtcAeSimpleFormController {

    ClinicalStaffRepository clinicalStaffRepository;

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
        return clinicalStaff;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ClinicalStaff clinicalStaff = (ClinicalStaff) command;
        String currentStatus = request.getParameter("status");

        if (currentStatus.equals(RoleStatus.ACTIVE.toString())) {
            clinicalStaff.setStatus(RoleStatus.IN_ACTIVE);
        } else {
            clinicalStaff.setStatus(RoleStatus.ACTIVE);
        }

        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        RedirectView redirectView = new RedirectView("searchClinicalStaff");
        return new ModelAndView(redirectView);
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}
