package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueIdentifierForStudy;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "studies")
public class Study extends BasePersistable {

    public static final String DEFAULT_SITE_NAME = "default";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "SHORT_TITLE", nullable = false)
    private String shortTitle;

    @Column(name = "LONG_TITLE", nullable = false)
    private String longTitle;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "ASSIGNED_IDENTIFIER", nullable = false)
    private String assignedIdentifier;

    @Transient
    private StudyFundingSponsor studyFundingSponsor;

    @Transient
    private StudyCoordinatingCenter studyCoordinatingCenter;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudyOrganization> studyOrganizations = new ArrayList<StudyOrganization>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "study", fetch = FetchType.LAZY)
    private List<StudyCrf> studyCrfs = new ArrayList<StudyCrf>();

    @UniqueObjectInCollection(message = "Duplicate Site")
    public List<StudySite> getStudySites() {
        List<StudySite> studySites = new ArrayList<StudySite>();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof StudySite) {
                studySites.add((StudySite) studyOrganization);
            }
        }
        return studySites;
    }

    public StudyFundingSponsor getStudyFundingSponsor() {

        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof StudyFundingSponsor) {
                studyFundingSponsor = (StudyFundingSponsor) studyOrganization;
            }
        }

        return studyFundingSponsor;

    }

    public void setStudyFundingSponsor(StudyFundingSponsor studyFundingSponsor) {
        this.studyFundingSponsor = studyFundingSponsor;
        if (!studyFundingSponsor.isPersisted()) {
            addStudyOrganization(studyFundingSponsor);
        }
    }

    public StudyCoordinatingCenter getStudyCoordinatingCenter() {
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof StudyCoordinatingCenter) {
                studyCoordinatingCenter = (StudyCoordinatingCenter) studyOrganization;
            }
        }
        return studyCoordinatingCenter;
    }

    public void setStudyCoordinatingCenter(
            StudyCoordinatingCenter studyCoordinatingCenter) {
        this.studyCoordinatingCenter = studyCoordinatingCenter;
        if (!studyCoordinatingCenter.isPersisted()) {
            addStudyOrganization(studyCoordinatingCenter);
        }
    }

    public List<StudyOrganization> getStudyOrganizations() {
        return studyOrganizations;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return getShortTitle()
                + (getAssignedIdentifier() == null ? "" : " ( "
                + getAssignedIdentifier() + " ) ");
    }

    @UniqueIdentifierForStudy(message = "Identifier already exists.")
    public String getAssignedIdentifier() {
        return assignedIdentifier;
    }

    public void setAssignedIdentifier(String assignedIdentifier) {
        this.assignedIdentifier = assignedIdentifier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addStudySite(StudySite studySite) {
        addStudyOrganization(studySite);

    }

    private void addStudyOrganization(StudyOrganization studyOrganization) {
        if (studyOrganization != null) {
            studyOrganization.setStudy(this);
            studyOrganizations.add(studyOrganization);
        }
    }

    private void removeStudyOrganization(StudyOrganization studyOrganization) {
        studyOrganizations.remove(studyOrganization);

    }

    public void removeStudySite(StudySite studySite) {
        removeStudyOrganization(studySite);

    }

    public List<StudyCrf> getStudyCrfs() {
        return studyCrfs;
    }

    public void addStudyCrf(StudyCrf studyCrf) {
        if (studyCrf != null) {
            studyCrf.setStudy(this);
            studyCrfs.add(studyCrf);
        }
    }

    public void addStudyCrfs(List<StudyCrf> studyCrfs) {
        for (StudyCrf studyCrf : studyCrfs) {
            addStudyCrf(studyCrf);
        }
    }

    public void removeStudyCrf(StudyCrf studyCrf) {
        if (studyCrf != null) {
            studyCrfs.remove(studyCrf);
        }
    }

    public void removeStudyCrfs(List<StudyCrf> studyCrfs) {
        for (StudyCrf studyCrf : studyCrfs) {
            removeStudyCrf(studyCrf);
        }
    }

    public void addCrf(CRF crf) {
        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(crf);
        this.addStudyCrf(studyCrf);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Study study = (Study) o;

        if (assignedIdentifier != null ? !assignedIdentifier.equals(study.assignedIdentifier) : study.assignedIdentifier != null)
            return false;
        if (id != null ? !id.equals(study.id) : study.id != null) return false;
        if (longTitle != null ? !longTitle.equals(study.longTitle) : study.longTitle != null) return false;
        if (shortTitle != null ? !shortTitle.equals(study.shortTitle) : study.shortTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (shortTitle != null ? shortTitle.hashCode() : 0);
        result = 31 * result + (longTitle != null ? longTitle.hashCode() : 0);
        result = 31 * result + (assignedIdentifier != null ? assignedIdentifier.hashCode() : 0);
        return result;
    }
}