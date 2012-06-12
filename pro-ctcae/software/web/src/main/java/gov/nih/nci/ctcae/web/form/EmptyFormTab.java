package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


public class EmptyFormTab extends SecuredTab<CreateFormCommand> {

    CRFRepository crfRepository;

    public EmptyFormTab() {
        super("form.tab.overview", "form.tab.overview", "form/confirmForm");
    	//super(longTitle, shortTitle, viewName);
    }


    @Override
    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_FORM;
    }

    @Override
    public void onDisplay(HttpServletRequest request, CreateFormCommand command) {
        CRF crf = command.getCrf();
        if (crf != null) {
            command.setCrf(crf);
            CRF savedCrf = crfRepository.save(crf);
            command.setCrf(savedCrf);
        }
    }

    @Override
    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map model = new HashMap();
        model.put("crf", command.getCrf());
        return model;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
