package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_pages_id")})

public class CRFCalendar extends BasePersistable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFCalendar.class);

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
    private String repeatEveryAmount;

    @Column(name = "due_date_unit")
    private String dueDateUnit;

    @Column(name = "due_date_amount")
    private String dueDateAmount;

    @Column(name = "repeat_until_unit")
    private String repeatUntilUnit;

    @Column(name = "repeat_until_amount")
    private String repeatUntilAmount;

    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRF crf;


    /**
     * Instantiates a new cRF calendar.
     */
    public CRFCalendar() {
        super();
    }


    /**
     * Gets the crf.
     *
     * @return the crf
     */
    public CRF getCrf() {
        return crf;
    }

    /**
     * Sets the crf.
     *
     * @param crf the new crf
     */
    public void setCrf(final CRF crf) {
        this.crf = crf;
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

    public String getRepeatEveryAmount() {
        return repeatEveryAmount;
    }

    public void setRepeatEveryAmount(String repeatEveryAmount) {
        this.repeatEveryAmount = repeatEveryAmount;
    }

    public String getDueDateUnit() {
        return dueDateUnit;
    }

    public void setDueDateUnit(String dueDateUnit) {
        this.dueDateUnit = dueDateUnit;
    }

    public String getDueDateAmount() {
        return dueDateAmount;
    }

    public void setDueDateAmount(String dueDateAmount) {
        this.dueDateAmount = dueDateAmount;
    }

    public String getRepeatUntilUnit() {
        return repeatUntilUnit;
    }

    public void setRepeatUntilUnit(String repeatUntilUnit) {
        this.repeatUntilUnit = repeatUntilUnit;
    }

    public String getRepeatUntilAmount() {
        return repeatUntilAmount;
    }

    public void setRepeatUntilAmount(String repeatUntilAmount) {
        this.repeatUntilAmount = repeatUntilAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRFCalendar that = (CRFCalendar) o;

        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;
        if (dueDateAmount != null ? !dueDateAmount.equals(that.dueDateAmount) : that.dueDateAmount != null)
            return false;
        if (dueDateUnit != null ? !dueDateUnit.equals(that.dueDateUnit) : that.dueDateUnit != null) return false;
        if (repeatEveryAmount != null ? !repeatEveryAmount.equals(that.repeatEveryAmount) : that.repeatEveryAmount != null)
            return false;
        if (repeatEveryUnit != null ? !repeatEveryUnit.equals(that.repeatEveryUnit) : that.repeatEveryUnit != null)
            return false;
        if (repeatUntilAmount != null ? !repeatUntilAmount.equals(that.repeatUntilAmount) : that.repeatUntilAmount != null)
            return false;
        if (repeatUntilUnit != null ? !repeatUntilUnit.equals(that.repeatUntilUnit) : that.repeatUntilUnit != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = repeatEveryUnit != null ? repeatEveryUnit.hashCode() : 0;
        result = 31 * result + (repeatEveryAmount != null ? repeatEveryAmount.hashCode() : 0);
        result = 31 * result + (dueDateUnit != null ? dueDateUnit.hashCode() : 0);
        result = 31 * result + (dueDateAmount != null ? dueDateAmount.hashCode() : 0);
        result = 31 * result + (repeatUntilUnit != null ? repeatUntilUnit.hashCode() : 0);
        result = 31 * result + (repeatUntilAmount != null ? repeatUntilAmount.hashCode() : 0);
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        return result;
    }
}