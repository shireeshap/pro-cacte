package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "study_participant_crf_grades")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_crf_grades_id")})
public class StudyParticipantCrfGrades extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "sp_crf_schedule_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    
    @JoinColumn(name = "grade_mapping_version_id", referencedColumnName = "id")
    @ManyToOne
    private ProctcaeGradeMappingVersion gradeMappingVersion;
    
    @JoinColumn(name = "pro_ctc_terms_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcTerm proCtcTerm;

	@JoinColumn(name = "meddra_llt_id", referencedColumnName = "id")
    @ManyToOne
    private LowLevelTerm lowLevelTerm;
    
	@Column(name = "grade_evaluation_date", nullable = true)
    private Date gradeEvaluationDate;


	@Column(name = "grade", nullable = true)
    private String grade;
	
    @Column(name="proctcae_verbatim", nullable = true)
    private String proctcaeVerbatim;
    

	public StudyParticipantCrfGrades() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
    	return studyParticipantCrfSchedule;
    }
    
    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
    	this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
    }
    
    public ProCtcTerm getProCtcTerm() {
    	return proCtcTerm;
    }
    
    public void setProCtcTerm(ProCtcTerm proCtcTerm) {
    	this.proCtcTerm = proCtcTerm;
    }
    
    public LowLevelTerm getLowLevelTerm() {
    	return lowLevelTerm;
    }
    
    public void setLowLevelTerm(LowLevelTerm lowLevelTerm) {
    	this.lowLevelTerm = lowLevelTerm;
    }
    
    public ProctcaeGradeMappingVersion getGradeMappingVersion() {
    	return gradeMappingVersion;
    }
    
    public void setGradeMappingVersion(ProctcaeGradeMappingVersion gradeMappingVersion) {
    	this.gradeMappingVersion = gradeMappingVersion;
    }

    public Date getGradeEvaluationDate() {
    	return gradeEvaluationDate;
    }
    
    public void setGradeEvaluationDate(Date gradeEvaluationDate) {
    	this.gradeEvaluationDate = gradeEvaluationDate;
    }
   
    public String getGrade() {
    	return grade;
    }
    
    public void setGrade(String grade) {
    	this.grade = grade;
    }
    
	public String getProctcaeVerbatim() {
		return proctcaeVerbatim;
	}

	public void setProctcaeVerbatim(String proctcaeVerbatim) {
		this.proctcaeVerbatim = proctcaeVerbatim;
	}
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StudyParticipantCrfGrades that = (StudyParticipantCrfGrades) o;

        if(studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
        	return false;
        if(gradeMappingVersion != null ? !gradeMappingVersion.equals(that.gradeMappingVersion) : that.gradeMappingVersion != null)
        	return false;
        if(proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm) : that.proCtcTerm != null) return false;
        if(lowLevelTerm != null ? !lowLevelTerm.equals(that.lowLevelTerm) : that.lowLevelTerm != null) return false;
        if(grade != null ? !grade.equals(that.grade) : that.grade != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
        result = 31 * result + (gradeMappingVersion != null ? gradeMappingVersion.hashCode() : 0);
        result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
        result = 31 * result + (lowLevelTerm != null ? lowLevelTerm.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        return result;
    }
}
