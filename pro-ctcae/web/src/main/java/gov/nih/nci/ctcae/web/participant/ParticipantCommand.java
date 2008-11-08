package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;

public class ParticipantCommand {

    private Participant participant;
    private int siteId;
    private int[] studyId;
    private String siteName = "";

    public ParticipantCommand() {
        participant = new Participant();
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int[] getStudyId() {
        return studyId;
    }

    public void setStudyId(int[] studyId) {
        this.studyId = studyId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
