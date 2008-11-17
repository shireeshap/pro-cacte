package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.DomainObject;

import javax.persistence.*;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Mehul Gulati
 *         Date: Nov 12, 2008
 */
@Entity
@Table(name = "study_diseases")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "term_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ABSTRACT_TERM")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
    @Parameter(name = "sequence", value = "seq_study_diseases_id")})
public abstract class AbstractStudyDisease<T extends BaseVersionable> extends BaseVersionable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "term_id")
    private T term;

    private Study study;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public T getTerm() {
        return term;
    }

    public void setTerm(T term) {
        this.term = term;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Transient
    public abstract String getTermName();

    @Transient
    public void setTermName(String name) {

    }
}
