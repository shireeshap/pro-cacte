package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class StudyCrfAjaxFacade {

    private StudyRepository studyRepository;


    public String searchStudyCrf(Map parameterMap, Integer id, HttpServletRequest request) {

        List<StudyCrf> studyCrfs = getObjects(id);
        gov.nih.nci.ctcae.web.form.StudyCrfTableModel studyCrfTableModel = new gov.nih.nci.ctcae.web.form.StudyCrfTableModel();
        String table = studyCrfTableModel.buildStudyCrfTable(parameterMap, studyCrfs, request);
        return table;
    }

    private List<StudyCrf> getObjects(Integer id) {
        Study study = studyRepository.findById(id);

        return study.getStudyCrfs();
    }


    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
