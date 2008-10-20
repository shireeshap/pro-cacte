package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;


/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_diseases")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "term_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ABSTRACT_TERM")
public abstract class StudyDisease extends BasePersistable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false, insertable = false, updatable = false)
    private Study study;

    //  private T term;


    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    /*
    * this is only transient here -- subclasses need to override it and specify what it refers to
    * This should work: @ManyToOne @JoinColumn(name = "cause_id", nullable = false)
    */
//    public T getTerm() {
//        return term;
//    }
//
//    public void setTerm(T term) {
//        this.term = term;
//    }

    @Transient
    public abstract String getTermName();

    @Transient
    public void setTermName(String name) {
    }
}