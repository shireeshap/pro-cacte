package gov.nih.nci.ctcae.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.binding.convert.converters.PropertyEditorConverter;
import gov.nih.nci.ctcae.core.repository.CommonRepository;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;

/**
 * @author Vinay Kumar
 * @crated Oct 16, 2008
 */
public class ApplicationConversionService extends DefaultConversionService implements InitializingBean {
    private CommonRepository commonRepository;

    public ApplicationConversionService(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }


    public void afterPropertiesSet() throws Exception {
        addConverter(ControllerTools.getDateConverter());
        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(commonRepository, Organization.class);
        addConverter(new PropertyEditorConverter(organizationEditor, Organization.class));


    }

}
