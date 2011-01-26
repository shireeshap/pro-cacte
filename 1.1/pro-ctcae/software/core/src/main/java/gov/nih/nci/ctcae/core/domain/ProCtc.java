package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//
/**
 * The Class ProCtc.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_id")})
public class ProCtc extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The pro ctc version.
     */
    @Column(name = "pro_ctc_version", nullable = false, unique = true)
    private String proCtcVersion;

    /**
     * The release date.
     */
    @Column(name = "release_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    /**
     * The pro ctc terms.
     */
    @OneToMany(mappedBy = "proCtc")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>();

    /**
     * Instantiates a new pro ctc.
     */
    public ProCtc() {
        super();
    }


    /**
     * Instantiates a new pro ctc.
     *
     * @param id            the id
     * @param proCtcVersion the pro ctc version
     * @param releaseDate   the release date
     */
    public ProCtc(Integer id, String proCtcVersion, Date releaseDate) {
        this.id = id;
        this.proCtcVersion = proCtcVersion;
        this.releaseDate = releaseDate;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
     */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the pro ctc version.
     *
     * @return the pro ctc version
     */
    public String getProCtcVersion() {
        return proCtcVersion;
    }

    /**
     * Sets the pro ctc version.
     *
     * @param proCtcVersion the new pro ctc version
     */
    public void setProCtcVersion(String proCtcVersion) {
        this.proCtcVersion = proCtcVersion;
    }

    /**
     * Gets the release date.
     *
     * @return the release date
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the release date.
     *
     * @param releaseDate the new release date
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


    /**
     * Gets the pro ctc terms.
     *
     * @return the pro ctc terms
     */
    public List<ProCtcTerm> getProCtcTerms() {
        return proCtcTerms;
    }

    /**
     * Adds the pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     */
    public void addProCtcTerm(ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            proCtcTerm.setProCtc(this);
            proCtcTerms.add(proCtcTerm);
        }
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Version: " + proCtcVersion + ", Released: " + releaseDate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtc)) return false;

        ProCtc proCtc = (ProCtc) o;

        if (proCtcVersion != null ? !proCtcVersion.equals(proCtc.proCtcVersion) : proCtc.proCtcVersion != null)
            return false;
        if (releaseDate != null ? !releaseDate.equals(proCtc.releaseDate) : proCtc.releaseDate != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result;
        result = (proCtcVersion != null ? proCtcVersion.hashCode() : 0);
        result = 31 * result + (proCtcVersion != null ? proCtcVersion.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }
}
