package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author mehul gulati
 *         Date: Oct 27, 2010
 */

@Entity
@Table(name = "STUDY_MODES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_modes_id")})
public class StudyMode extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "mode", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AppMode mode = AppMode.WEB;

    @JoinColumn(name = "study_id", referencedColumnName = "id")
    @ManyToOne
    private Study study;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AppMode getMode() {
        return mode;
    }

    public void setMode(AppMode mode) {
        this.mode = mode;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
