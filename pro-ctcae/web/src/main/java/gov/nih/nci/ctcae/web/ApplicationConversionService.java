package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.convert.converters.PropertyEditorConverter;
import org.springframework.binding.convert.service.DefaultConversionService;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationConversionService.
 * 
 * @author Vinay Kumar
 * @crated Oct 16, 2008
 */
public class ApplicationConversionService extends DefaultConversionService implements InitializingBean {
    
    /** The finder repository. */
    private FinderRepository finderRepository;

    /**
     * Instantiates a new application conversion service.
     * 
     * @param finderRepository the finder repository
     */
    public ApplicationConversionService(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }


    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        addConverter(ControllerTools.getDateConverter());
        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(finderRepository, Organization.class);
        RepositoryBasedEditor studyEditor = new RepositoryBasedEditor(finderRepository, Study.class);
        addConverter(new PropertyEditorConverter(organizationEditor, Organization.class));
        addConverter(new PropertyEditorConverter(studyEditor, Study.class));


    }

}
