package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class StudySite.
 *
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "SST")
public class StudySite extends StudyOrganization {

    @OneToMany(mappedBy = "studySite", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudySiteClinicalStaff> studySiteClinicalStaffs = new ArrayList<StudySiteClinicalStaff>();

    public void addStudySiteClinicalStaff(StudySiteClinicalStaff studySiteClinicalStaff) {
        if (studySiteClinicalStaff != null) {
            studySiteClinicalStaff.setStudySite(this);
            getStudySiteClinicalStaffs().add(studySiteClinicalStaff);
        }
    }

    public List<StudySiteClinicalStaff> getStudySiteClinicalStaffs() {
        return studySiteClinicalStaffs;
    }
}