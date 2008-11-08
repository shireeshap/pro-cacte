package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * @author Harsh Agarwal
 * @crated Nov 3, 2008
 */
public class SubmitFormCommand implements Serializable {

    private static Log logger = LogFactory.getLog(SubmitFormCommand.class);

    private StudyParticipantCrf studyParticipantCrf;

    public SubmitFormCommand() {

    }

    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }

    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

}