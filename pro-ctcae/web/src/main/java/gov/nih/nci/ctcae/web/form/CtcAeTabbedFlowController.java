package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.ControllerTools;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Saurabh Agrawal
 * @crated Nov 5, 2008
 */
public abstract class CtcAeTabbedFlowController<C extends Object> extends AbstractTabbedFlowFormController<C> {
    protected StudyRepository studyRepository;
    private OrganizationRepository organizationRepository;
    protected FinderRepository finderRepository;
    protected ControllerTools controllerTools;

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, controllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        RepositoryBasedEditor studyEditor = new RepositoryBasedEditor(finderRepository, Study.class);
        binder.registerCustomEditor(Study.class, studyEditor);

        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(finderRepository, Organization.class);
        binder.registerCustomEditor(Organization.class, organizationEditor);


    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    @Required
    public void setControllerTools(ControllerTools controllerTools) {
        this.controllerTools = controllerTools;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
