package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;

import java.util.ArrayList;
import java.util.List;

public class StudyParticipantCommand {

    private Participant participant;
    private Study study;
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();


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

    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    public void setStudyParticipantCrfs(List<StudyParticipantCrf> studyParticipantCrfs) {
        this.studyParticipantCrfs = studyParticipantCrfs;
    }
}