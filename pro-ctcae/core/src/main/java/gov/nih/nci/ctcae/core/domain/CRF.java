package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CRFS")
public class CRF extends BaseVersionable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "crf_version", nullable = false)
    private String crfVersion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crf")
    private List<CrfItem> crfItems = new ArrayList<CrfItem>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
    private List<StudyCrf> studyCrfs = new ArrayList<StudyCrf>();

    public CRF() {
    }

    public CRF(Integer id) {
        this.id = id;
    }

    public CRF(Integer id, String title, String status, String crfVersion) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.crfVersion = crfVersion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCrfVersion() {
        return crfVersion;
    }

    public void setCrfVersion(String crfVersion) {
        this.crfVersion = crfVersion;
    }

    public List<CrfItem> getCrfItems() {
        Collections.sort(crfItems,new DisplayOrderComparator());
        return crfItems;
    }

    public void addCrfItem(CrfItem crfItem) {
        if (crfItem != null) {
            crfItem.setCRF(this);
            crfItem.setDisplayOrder(getCrfItems().size());
            crfItems.add(crfItem);
        }
    }

    public void addCrfItems(Collection<CrfItem> crfItems) {
        for (CrfItem crfItem : crfItems) {
            addCrfItem(crfItem);
        }
    }

    public void removeCrfItem(CrfItem crfItem) {
        if (crfItem != null) {
            crfItems.remove(crfItem);
        }
    }

    public void removeCrfItems(Collection<CrfItem> crfItems) {
        for (CrfItem crfItem : crfItems) {
            removeCrfItem(crfItem);
        }
    }

    public List<StudyCrf> getStudyCrfs() {
        return studyCrfs;
    }

    public void addStudyCrf(StudyCrf studyCrf) {
        if (studyCrf != null) {
            studyCrf.setCrf(this);
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

    public void addStudy(Study study) {
        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setStudy(study);
        this.addStudyCrf(studyCrf);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CRF crf = (CRF) o;

        if (crfItems != null ? !crfItems.equals(crf.crfItems)
                : crf.crfItems != null)
            return false;
        if (crfVersion != null ? !crfVersion.equals(crf.crfVersion)
                : crf.crfVersion != null)
            return false;
        if (description != null ? !description.equals(crf.description)
                : crf.description != null)
            return false;
        if (id != null ? !id.equals(crf.id) : crf.id != null)
            return false;
        if (status != null ? !status.equals(crf.status) : crf.status != null)
            return false;
        if (title != null ? !title.equals(crf.title) : crf.title != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result
                + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (crfVersion != null ? crfVersion.hashCode() : 0);
        result = 31 * result + (crfItems != null ? crfItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return title;
    }

}
