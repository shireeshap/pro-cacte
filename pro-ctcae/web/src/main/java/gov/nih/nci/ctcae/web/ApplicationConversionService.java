package gov.nih.nci.ctcae.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.binding.convert.converters.PropertyEditorConverter;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;

/**
 * @author Vinay Kumar
 * @crated Oct 16, 2008
 */
public class ApplicationConversionService extends DefaultConversionService implements InitializingBean {
    private FinderRepository finderRepository;

    public ApplicationConversionService(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }


    public void afterPropertiesSet() throws Exception {
        addConverter(ControllerTools.getDateConverter());
        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(finderRepository, Organization.class);
        RepositoryBasedEditor studyEditor = new RepositoryBasedEditor(finderRepository, Study.class);
        addConverter(new PropertyEditorConverter(organizationEditor, Organization.class));
        addConverter(new PropertyEditorConverter(studyEditor, Study.class));


    }

}
