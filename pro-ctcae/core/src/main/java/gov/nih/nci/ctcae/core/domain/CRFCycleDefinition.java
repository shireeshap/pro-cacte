package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

//
/**
 * The Class CRFCycleDefinition.
 *
 * @author Harsh Agarwal
 * @created Feb 16, 2008
 */

@Entity
@Table(name = "CRF_CYCLE_DEFINITIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_definitions_id")})

public class CRFCycleDefinition extends BasePersistable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFCycleDefinition.class);

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "cycle_length", nullable = false)
    private Integer cycleLength;

    @Column(name = "cycle_name")
    private String cycleName;

    @Column(name = "cycle_length_unit", nullable = false)
    private String cycleLengthUnit;

    @Column(name = "repeat_times", nullable = false)
    private String repeatTimes;

    @Column(name = "display_order", nullable = false)
    private Integer order;
    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRF crf;

    @OneToMany(mappedBy = "crfCycleDefinition", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRFCycle> crfCycles = new ArrayList<CRFCycle>();

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * Instantiates a new cRF calendar.
     */
    public CRFCycleDefinition() {
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

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
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
        if (!(o instanceof CRFCycleDefinition)) return false;

        CRFCycleDefinition that = (CRFCycleDefinition) o;

        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;
        if (crfCycles != null ? !crfCycles.equals(that.crfCycles) : that.crfCycles != null) return false;
        if (cycleLength != null ? !cycleLength.equals(that.cycleLength) : that.cycleLength != null) return false;
        if (cycleLengthUnit != null ? !cycleLengthUnit.equals(that.cycleLengthUnit) : that.cycleLengthUnit != null)
            return false;
        if (cycleName != null ? !cycleName.equals(that.cycleName) : that.cycleName != null) return false;
        if (repeatTimes != null ? !repeatTimes.equals(that.repeatTimes) : that.repeatTimes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cycleLength != null ? cycleLength.hashCode() : 0;
        result = 31 * result + (cycleName != null ? cycleName.hashCode() : 0);
        result = 31 * result + (cycleLengthUnit != null ? cycleLengthUnit.hashCode() : 0);
        result = 31 * result + (repeatTimes != null ? repeatTimes.hashCode() : 0);
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        result = 31 * result + (crfCycles != null ? crfCycles.hashCode() : 0);
        return result;
    }

    public List<CRFCycle> getCrfCycles() {
        Collections.sort(crfCycles, new CrfCycleOrderComparator());
        return crfCycles;
    }

    public void addCrfCycle(CRFCycle crfCycle) {
        if (crfCycle != null) {
            crfCycle.setCrfDefinition(this);
            crfCycles.add(crfCycle);
        }
    }

    public String getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(String repeatTimes) {
        this.repeatTimes = repeatTimes;
    }
}