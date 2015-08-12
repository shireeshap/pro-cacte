package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import org.springframework.context.ApplicationEvent;


/**
 * Created by royzuniga on 7/24/15.
 */
public class NewStudyParticipantNotification extends ApplicationEvent {


    private String craEmail;
    private String participantUsername;
    private String studySite;



    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @param craEmail
     * @param participantUsername
     * @param studySite @throws IllegalArgumentException if source is null.
     */
    public NewStudyParticipantNotification(Object source, String craEmail, String participantUsername, String studySite) {
        super(source);
        setCraEmail(craEmail);
        setParticipantUsername(participantUsername);
        setStudySite(studySite);
    }


    public String getCraEmail() {
        return craEmail;
    }

    public void setCraEmail(String craEmail) {
        this.craEmail = craEmail;
    }

    public String getParticipantUsername() {
        return participantUsername;
    }

    public void setParticipantUsername(String participantUsername) {
        this.participantUsername = participantUsername;
    }

    public String getStudySite() {
        return studySite;
    }

    public void setStudySite(String studySite) {
        this.studySite = studySite;
    }
}
