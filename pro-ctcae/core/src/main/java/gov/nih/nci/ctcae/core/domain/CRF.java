package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    @Enumerated(value = EnumType.STRING)
    private CrfStatus status;

    @Column(name = "crf_version", nullable = false)
    private String crfVersion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
    private List<CrfItem> crfItems = new ArrayList<CrfItem>();

    public StudyCrf getStudyCrf() {
        return studyCrf;
    }

    public void setStudyCrf(StudyCrf studyCrf) {
        this.studyCrf = studyCrf;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
    private StudyCrf studyCrf ;

    public CRF() {
    }

    public CRF(Integer id) {
        this.id = id;
    }

    public CRF(Integer id, String title, CrfStatus status, String crfVersion) {
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

    public CrfStatus getStatus() {
        return status;
    }

    public void setStatus(CrfStatus status) {
        this.status = status;
    }

    public String getCrfVersion() {
        return crfVersion;
    }

    public void setCrfVersion(String crfVersion) {
        this.crfVersion = crfVersion;
    }

    public List<CrfItem> getCrfItems() {
        Collections.sort(crfItems, new DisplayOrderComparator());
        return crfItems;
    }

    /**
     * this will work only in create form flow
     *
     * @param crfItem
     */
    public void addCrfItem(CrfItem crfItem) {
        if (crfItem != null) {
            //check if it already exists
            for (CrfItem existingCrfItem : getCrfItems()) {
                if (existingCrfItem.getProCtcQuestion() != null
                        && (existingCrfItem.getProCtcQuestion().equals(crfItem.getProCtcQuestion()))) {
                    //probably we are updating order only
                    existingCrfItem.setDisplayOrder(crfItem.getDisplayOrder());
                    return;
                }
            }
            if (crfItem.getDisplayOrder() == null || crfItem.getDisplayOrder() == 0) {
                crfItem.setDisplayOrder(getCrfItems().size() + 1);

            }
            crfItem.setCrf(this);
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

    @Override
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

    @Override
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

    public boolean isReleased() {
        return getStatus().equals(CrfStatus.RELEASEED);
    }
}
