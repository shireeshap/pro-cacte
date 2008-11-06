package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;

import java.util.List;
import java.util.ArrayList;

public class StudyParticipantCommand {

	private Participant participant;
	private Study study;
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    public StudyParticipantCommand(){
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

    public List<StudyParticipantCrf> getStudyParticipants() {
        return studyParticipantCrfs;
    }

    public void setStudyParticipants(List<StudyParticipantCrf> studyParticipantCrfs) {
        this.studyParticipantCrfs = studyParticipantCrfs;
    }
}