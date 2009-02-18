package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//
/**
 * The Class CRFCycle.
 *
 * @author Harsh Agarwal
 * @created Feb 16, 2008
 */

@Entity
@Table(name = "CRF_CYCLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_pages_id")})

public class CRFCycle extends BasePersistable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFCycle.class);

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "cycle_length", nullable = false)
    private Integer cycleLength;

    @Column(name = "cycle_days")
    private String cycleDays;

    @Column(name = "repeat_times")
    private Integer repeatTimes;

    @Column(name = "cycle_length_unit" , nullable = false)
    private String cycleLengthUnit;
    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRF crf;


    /**
     * Instantiates a new cRF calendar.
     */
    public CRFCycle() {
        super();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(Integer cycleLength) {
        this.cycleLength = cycleLength;
    }

    public String getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(String cycleDays) {
        this.cycleDays = cycleDays;
    }

    public Integer getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(Integer repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }

    public String getCycleLengthUnit() {
        return cycleLengthUnit;
    }

    public void setCycleLengthUnit(String cycleLengthUnit) {
        this.cycleLengthUnit = cycleLengthUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CRFCycle)) return false;

        CRFCycle crfCycle = (CRFCycle) o;

        if (crf != null ? !crf.equals(crfCycle.crf) : crfCycle.crf != null) return false;
        if (cycleDays != null ? !cycleDays.equals(crfCycle.cycleDays) : crfCycle.cycleDays != null) return false;
        if (cycleLength != null ? !cycleLength.equals(crfCycle.cycleLength) : crfCycle.cycleLength != null)
            return false;
        if (cycleLengthUnit != null ? !cycleLengthUnit.equals(crfCycle.cycleLengthUnit) : crfCycle.cycleLengthUnit != null)
            return false;
        if (repeatTimes != null ? !repeatTimes.equals(crfCycle.repeatTimes) : crfCycle.repeatTimes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cycleLength != null ? cycleLength.hashCode() : 0;
        result = 31 * result + (cycleDays != null ? cycleDays.hashCode() : 0);
        result = 31 * result + (repeatTimes != null ? repeatTimes.hashCode() : 0);
        result = 31 * result + (cycleLengthUnit != null ? cycleLengthUnit.hashCode() : 0);
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        return result;
    }
}