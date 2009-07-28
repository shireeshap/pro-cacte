package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Mehul Gulati
 *         Date: Jul 8, 2009
 */
@Entity
@Table(name = "ARMS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_arms_id")})
public class Arm extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(name = "DEFAULT_ARM", nullable = false)
    private boolean defaultArm = false;

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

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arm arm = (Arm) o;

        if (defaultArm != arm.defaultArm) return false;
        if (description != null ? !description.equals(arm.description) : arm.description != null) return false;
        if (study != null ? !study.equals(arm.study) : arm.study != null) return false;
        if (title != null ? !title.equals(arm.title) : arm.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (study != null ? study.hashCode() : 0);
        result = 31 * result + (defaultArm ? 1 : 0);
        return result;
    }

    public boolean isDefaultArm() {
        return defaultArm;
    }

    public void setDefaultArm(boolean defaultArm) {
        this.defaultArm = defaultArm;
    }
}
