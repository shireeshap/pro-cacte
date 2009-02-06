package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudySiteClinicalStaff.
 *
 * @author Vinay Kumar
 */

@Entity
@Table(name = "STUDY_SITE_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "ss_clinical_staffs_id_seq")})
public class StudySiteClinicalStaff extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "site_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private SiteClinicalStaff siteClinicalStaff;

    @JoinColumn(name = "study_site_id", referencedColumnName = "id")
    @ManyToOne
    private StudySite studySite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SiteClinicalStaff getSiteClinicalStaff() {
        return siteClinicalStaff;
    }

    public void setSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        this.siteClinicalStaff = siteClinicalStaff;
    }

    public StudySite getStudySite() {
        return studySite;
    }

    public void setStudySite(StudySite studySite) {
        this.studySite = studySite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudySiteClinicalStaff that = (StudySiteClinicalStaff) o;

        if (siteClinicalStaff != null ? !siteClinicalStaff.equals(that.siteClinicalStaff) : that.siteClinicalStaff != null)
            return false;
        if (studySite != null ? !studySite.equals(that.studySite) : that.studySite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = siteClinicalStaff != null ? siteClinicalStaff.hashCode() : 0;
        result = 31 * result + (studySite != null ? studySite.hashCode() : 0);
        return result;
    }
}