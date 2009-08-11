package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

//
/**
 * The Class CRFCalendar.
 *
 * @author Harsh Agarwal
 * @created Feb 1, 2008
 */

@Entity
@Table(name = "CRF_CALENDARS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_calendars_id")})

public class CRFCalendar extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "repeat_every_unit")
    private String repeatEveryUnit;

    @Column(name = "repeat_every_amount")
    private String repeatEveryValue;

    @Column(name = "due_date_unit")
    private String dueDateUnit;

    @Column(name = "due_date_amount")
    private String dueDateValue;

    @Column(name = "repeat_until_unit")
    private String repeatUntilUnit;

    @Column(name = "repeat_until_amount")
    private String repeatUntilValue;

    @JoinColumn(name = "form_arm_schedules_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private FormArmSchedule formArmSchedule;


    /**
     * Instantiates a new cRF calendar.
     */
    public CRFCalendar() {
        super();
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


    public String getRepeatEveryUnit() {
        return repeatEveryUnit;
    }

    public void setRepeatEveryUnit(String repeatEveryUnit) {
        this.repeatEveryUnit = repeatEveryUnit;
    }

    public String getRepeatEveryValue() {
        return repeatEveryValue;
    }

    public void setRepeatEveryValue(String repeatEveryValue) {
        this.repeatEveryValue = repeatEveryValue;
    }

    public String getDueDateUnit() {
        return dueDateUnit;
    }

    public void setDueDateUnit(String dueDateUnit) {
        this.dueDateUnit = dueDateUnit;
    }

    public String getDueDateValue() {
        return dueDateValue;
    }

    public void setDueDateValue(String dueDateValue) {
        this.dueDateValue = dueDateValue;
    }

    public String getRepeatUntilUnit() {
        return repeatUntilUnit;
    }

    public void setRepeatUntilUnit(String repeatUntilUnit) {
        this.repeatUntilUnit = repeatUntilUnit;
    }

    public String getRepeatUntilValue() {
        return repeatUntilValue;
    }

    public void setRepeatUntilValue(String repeatUntilValue) {
        this.repeatUntilValue = repeatUntilValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRFCalendar that = (CRFCalendar) o;

        if (dueDateValue != null ? !dueDateValue.equals(that.dueDateValue) : that.dueDateValue != null)
            return false;
        if (dueDateUnit != null ? !dueDateUnit.equals(that.dueDateUnit) : that.dueDateUnit != null) return false;
        if (repeatEveryValue != null ? !repeatEveryValue.equals(that.repeatEveryValue) : that.repeatEveryValue != null)
            return false;
        if (repeatEveryUnit != null ? !repeatEveryUnit.equals(that.repeatEveryUnit) : that.repeatEveryUnit != null)
            return false;
        if (repeatUntilValue != null ? !repeatUntilValue.equals(that.repeatUntilValue) : that.repeatUntilValue != null)
            return false;
        if (repeatUntilUnit != null ? !repeatUntilUnit.equals(that.repeatUntilUnit) : that.repeatUntilUnit != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = repeatEveryUnit != null ? repeatEveryUnit.hashCode() : 0;
        result = 31 * result + (repeatEveryValue != null ? repeatEveryValue.hashCode() : 0);
        result = 31 * result + (dueDateUnit != null ? dueDateUnit.hashCode() : 0);
        result = 31 * result + (dueDateValue != null ? dueDateValue.hashCode() : 0);
        result = 31 * result + (repeatUntilUnit != null ? repeatUntilUnit.hashCode() : 0);
        result = 31 * result + (repeatUntilValue != null ? repeatUntilValue.hashCode() : 0);
        return result;
    }

    public FormArmSchedule getFormArmSchedule() {
        return formArmSchedule;
    }

    public void setFormArmSchedule(FormArmSchedule formArmSchedule) {
        this.formArmSchedule = formArmSchedule;
    }

    public boolean isValid() {
        return !StringUtils.isBlank(getRepeatEveryUnit())
                && !StringUtils.isBlank(getRepeatEveryValue())
                && !StringUtils.isBlank(getDueDateUnit())
                && !StringUtils.isBlank(getDueDateValue())
                && !StringUtils.isBlank(getRepeatUntilUnit())
                && !StringUtils.isBlank(getRepeatUntilValue())
                && formArmSchedule != null;

    }

    public void makeInvalid() {
        setRepeatEveryUnit("");
        setRepeatEveryValue("");
        setDueDateUnit("");
        setDueDateValue("");
        setRepeatUntilUnit("");
        setRepeatUntilValue("");
    }
}