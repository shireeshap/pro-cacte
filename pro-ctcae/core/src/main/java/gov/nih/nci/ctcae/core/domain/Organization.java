package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueNciIdentifierForOrganization;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "Organizations")
public class Organization extends BaseVersionable {

    public static final String DEFAULT_SITE_NAME = "default";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nci_institute_code", nullable = false)
    private String nciInstituteCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private List<SiteClinicalStaff> siteClinicalStaffs = new ArrayList<SiteClinicalStaff>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private Collection<StudyOrganization> studyOrganizations = new ArrayList<StudyOrganization>();

    public String getDisplayName() {
        return getName()
                + (getNciInstituteCode() == null ? "" : " ( "
                + getNciInstituteCode() + " ) ");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @UniqueNciIdentifierForOrganization
    public String getNciInstituteCode() {
        return nciInstituteCode;
    }

    public void setNciInstituteCode(String nciInstituteCode) {
        this.nciInstituteCode = nciInstituteCode;
    }

    public List<SiteClinicalStaff> getSiteClinicalStaffs() {
        return siteClinicalStaffs;
    }

    public Collection<StudyOrganization> getStudyOrganizations() {
        return studyOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Organization that = (Organization) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (nciInstituteCode != null ? !nciInstituteCode
                .equals(that.nciInstituteCode) : that.nciInstituteCode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result
                + (nciInstituteCode != null ? nciInstituteCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + nciInstituteCode + ")";
    }
}
