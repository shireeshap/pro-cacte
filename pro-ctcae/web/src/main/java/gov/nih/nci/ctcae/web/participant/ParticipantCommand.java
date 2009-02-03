package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticipantCommand.
 */
public class ParticipantCommand {

    /** The participant. */
    private Participant participant;
    
    /** The site id. */
    private int siteId;
    
    /** The study id. */
    private int[] studyId;
    
    /** The site name. */
    private String siteName;

    /**
     * Instantiates a new participant command.
     */
    public ParticipantCommand() {
        participant = new Participant();
    }

    /**
     * Gets the participant.
     * 
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     * 
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Gets the site id.
     * 
     * @return the site id
     */
    public int getSiteId() {
        return siteId;
    }

    /**
     * Sets the site id.
     * 
     * @param siteId the new site id
     */
    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    /**
     * Gets the study id.
     * 
     * @return the study id
     */
    public int[] getStudyId() {
        return studyId;
    }

    /**
     * Sets the study id.
     * 
     * @param studyId the new study id
     */
    public void setStudyId(int[] studyId) {
        this.studyId = studyId;
    }

    /**
     * Gets the site name.
     * 
     * @return the site name
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * Sets the site name.
     * 
     * @param siteName the new site name
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
