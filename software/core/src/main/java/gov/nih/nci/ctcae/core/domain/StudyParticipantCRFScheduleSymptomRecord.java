package gov.nih.nci.ctcae.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The Class StudyParticipantCRFScheduleSymptomRecord.
 * 
 * @author Vinay Gangoli
 */
@Entity
@Table(name = "sp_crf_sch_sympt_records")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_SP_CRF_SCH_SYMPT_RECORDS_ID")})
public class StudyParticipantCRFScheduleSymptomRecord extends BasePersistable {
	
    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;
    
	@Column(name = "filename", nullable = false)
	private String fileName;
	
	@Column(name = "filepath", nullable = false)
	private String filePath;

	@Column(name = "creation_date", nullable = true)
	private Date creationDate;
	
    @ManyToOne
    @JoinColumn(name = "sp_crf_schedules_id", referencedColumnName = "id", nullable = false)
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public void setStudyParticipantCrfSchedule(
			StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
