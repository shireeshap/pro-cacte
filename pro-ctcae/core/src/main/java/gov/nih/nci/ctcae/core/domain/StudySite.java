package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
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
 * @author Vinay Kumar
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

            Organization expectedOrganization = studySiteClinicalStaff.getSiteClinicalStaff().getOrganization();
            if (!expectedOrganization.equals(this.getOrganization())) {
                String errorMessage = String.format("clinical staff belongs to %s. It does not belongs to study site %s %s. So this clincal staff can not be added",
                        expectedOrganization.getDisplayName(), this.getStudy().getAssignedIdentifier(), this.getOrganization().getDisplayName());
                logger.error(errorMessage);
                throw new CtcAeSystemException(errorMessage);

            }

            studySiteClinicalStaff.setStudySite(this);
            if (!getStudySiteClinicalStaffs().contains(studySiteClinicalStaff)) {
                getStudySiteClinicalStaffs().add(studySiteClinicalStaff);
                logger.debug(String.format("added study site clinical staff %s to study site %s", studySiteClinicalStaff.toString(), toString()));
                return;
            }
            logger.debug(String.format("Skipping the adding because study site %s already has this study site clinical staff %s", studySiteClinicalStaff.getId(), getId()));

        }

    }


    public List<StudySiteClinicalStaff> getStudySiteClinicalStaffs() {
        return studySiteClinicalStaffs;
    }
}