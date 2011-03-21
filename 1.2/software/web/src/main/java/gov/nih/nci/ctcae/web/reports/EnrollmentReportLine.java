package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudySite;

import java.util.Date;

public class EnrollmentReportLine {
    private StudySite studySite;
    private int numberOfParticipants =0;
    private Date lastEnrollment;

    public StudySite getStudySite() {
        return studySite;
    }

    public void setStudySite(StudySite studySite) {
        this.studySite = studySite;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Date getLastEnrollment() {
        return lastEnrollment;
    }

    public void setLastEnrollment(Date lastEnrollment) {
        this.lastEnrollment = lastEnrollment;
    }
}
