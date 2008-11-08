package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.form.ControllersUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.nwu.bioinformatics.commons.CollectionUtils;

public class StudyParticipantCommand {

    private Participant participant;
    private Study study;
    StudyParticipantAssignment studyParticipantAssignment;

    public StudyParticipantCommand() {
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }
}