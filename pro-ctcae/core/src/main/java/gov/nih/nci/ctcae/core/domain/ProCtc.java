package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_id")})
public class ProCtc extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "pro_ctc_version", nullable = false, unique = true)
    private String proCtcVersion;

    @Column(name = "release_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtc")
    private List<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>();

    public ProCtc() {
    }

    public ProCtc(Integer id) {
        this.id = id;
    }

    public ProCtc(Integer id, String proCtcVersion, Date releaseDate) {
        this.id = id;
        this.proCtcVersion = proCtcVersion;
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProCtcVersion() {
        return proCtcVersion;
    }

    public void setProCtcVersion(String proCtcVersion) {
        this.proCtcVersion = proCtcVersion;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


    public List<ProCtcTerm> getProCtcTerms() {
        return proCtcTerms;
    }

    public void addProCtcTerm(ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            proCtcTerm.setProCtc(this);
            proCtcTerms.add(proCtcTerm);
        }
    }


    @Override
    public String toString() {
        return "Version: " + proCtcVersion + ", Released: " + releaseDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtc)) return false;

        ProCtc proCtc = (ProCtc) o;

        if (proCtcVersion != null ? !proCtcVersion.equals(proCtc.proCtcVersion) : proCtc.proCtcVersion != null)
            return false;
        if (releaseDate != null ? !releaseDate.equals(proCtc.releaseDate) : proCtc.releaseDate != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (proCtcVersion != null ? proCtcVersion.hashCode() : 0);
        result = 31 * result + (proCtcVersion != null ? proCtcVersion.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }
}
