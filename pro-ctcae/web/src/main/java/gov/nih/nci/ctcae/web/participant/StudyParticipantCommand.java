package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class StudyParticipantCommand {

    private Participant participant;
    private Study study;
    StudyParticipantAssignment studyParticipantAssignment;
    private String objectsIndexesToRemove;

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

    public String getObjectsIndexesToRemove() {
        return objectsIndexesToRemove;
    }

    public void setObjectsIndexesToRemove(String objectsIndexesToRemove) {
        this.objectsIndexesToRemove = objectsIndexesToRemove;
    }

    public void removeCrfSchedules() {
        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIndexesToRemove);
        HashMap<StudyParticipantCrf, List<StudyParticipantCrfSchedule>> schedulesToRemove = new HashMap<StudyParticipantCrf, List<StudyParticipantCrfSchedule>>();
        for (String index : indexes) {
            String crfIndex = index.substring(0, index.indexOf('-'));
            String scheduleIndex = index.substring(index.indexOf('-') + 1);

            StudyParticipantCrf crf = studyParticipantAssignment.getStudyParticipantCrfs().get(Integer.parseInt(crfIndex));
            StudyParticipantCrfSchedule crfSchedule = crf.getStudyParticipantCrfSchedules().get(Integer.parseInt(scheduleIndex));

            if (schedulesToRemove.get(crf) == null) {
                schedulesToRemove.put(crf, new ArrayList<StudyParticipantCrfSchedule>());
            }
            schedulesToRemove.get(crf).add(crfSchedule);
        }

        for (StudyParticipantCrf crf : schedulesToRemove.keySet()) {
            for (StudyParticipantCrfSchedule crfSchedule : schedulesToRemove.get(crf)) {
                crf.removeCrfSchedule(crfSchedule);
            }
        }
    }
}