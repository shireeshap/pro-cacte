package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Mehul Gulati
 *         Date: Jul 9, 2009
 */
@Entity
@Table(name = "FORM_ARM_SCHEDULES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_form_arm_schedules_id")})
public class FormArmSchedule extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private CRF crf;

    @ManyToOne
    @JoinColumn(name = "arm_id", nullable = false)
    private Arm arm;


    @OneToMany(mappedBy = "formArmSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRFCalendar> crfCalendars = new LinkedList<CRFCalendar>();

    @OneToMany(mappedBy = "formArmSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRFCycleDefinition> crfCycleDefinitions = new ArrayList<CRFCycleDefinition>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }

    public List<CRFCycleDefinition> getCrfCycleDefinitions() {
        Collections.sort(crfCycleDefinitions, new CrfCycleDefinitionOrderComparator());
        return crfCycleDefinitions;
    }

    public void addCrfCycleDefinition(CRFCycleDefinition crfCycleDefinition) {
        if (crfCycleDefinition != null) {
            crfCycleDefinition.setFormArmSchedule(this);
            crfCycleDefinitions.add(crfCycleDefinition);
        }
    }

    public void addCrfCalendar(CRFCalendar crfCalendar) {
        crfCalendar.setFormArmSchedule(this);
        crfCalendars.add(crfCalendar);
    }

    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }


    public List<CRFCalendar> getCrfCalendars() {
        return crfCalendars;
    }

    public void addFormCalendar(CRFCalendar crfCalendar) {
        crfCalendar.setFormArmSchedule(this);
        crfCalendars.add(crfCalendar);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormArmSchedule that = (FormArmSchedule) o;

        if (arm != null ? !arm.equals(that.arm) : that.arm != null) return false;
        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = crf != null ? crf.hashCode() : 0;
        result = 31 * result + (arm != null ? arm.hashCode() : 0);
        return result;
    }

    public FormArmSchedule copy() {
        FormArmSchedule formArmSchedule = new FormArmSchedule();
        formArmSchedule.setArm(arm);
        for (CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()) {
            formArmSchedule.addCrfCycleDefinition(crfCycleDefinition.copy());
        }
        for (CRFCalendar crfCalendar : formArmSchedule.getCrfCalendars()) {
            formArmSchedule.addCrfCalendar(crfCalendar.copy());
        }
        return formArmSchedule;
    }
}
