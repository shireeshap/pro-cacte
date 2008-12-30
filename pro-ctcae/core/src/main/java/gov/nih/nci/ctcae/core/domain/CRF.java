package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrf;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "crfs")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crfs_id")})

public class CRF extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "status", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private CrfStatus status = CrfStatus.DRAFT;

	@Column(name = "crf_version", nullable = false)
	private String crfVersion;

	@Column(name = "effective_start_date", nullable = true)
	private Date effectiveStartDate;

	@Column(name = "effective_end_date", nullable = true)
	private Date effectiveEndDate;

	@Column(name = "next_version_id", nullable = true)
	private Integer nextVersionId;

	@Column(name = "parent_version_id", nullable = true)
	private Integer parentVersionId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.EAGER)
	private List<CRFPage> crfPages = new LinkedList<CRFPage>();
	;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
	private StudyCrf studyCrf;

	public StudyCrf getStudyCrf() {
		return studyCrf;
	}

	public void setStudyCrf(StudyCrf studyCrf) {
		this.studyCrf = studyCrf;
	}


	public CRF() {
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotEmpty(message = "Missing Title")
	@UniqueTitleForCrf
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CrfStatus getStatus() {
		return status;
	}

	public void setStatus(CrfStatus status) {
		this.status = status;
	}

	public String getCrfVersion() {
		return crfVersion;
	}

	public void setCrfVersion(String crfVersion) {
		this.crfVersion = crfVersion;
	}


	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final CRF crf = (CRF) o;

		if (crfVersion != null ? !crfVersion.equals(crf.crfVersion) : crf.crfVersion != null) return false;
		if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
		if (status != crf.status) return false;
		if (title != null ? !title.equals(crf.title) : crf.title != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (crfVersion != null ? crfVersion.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return title;
	}

	public boolean isReleased() {
		return getStatus().equals(CrfStatus.RELEASED);
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public Integer getNextVersionId() {
		return nextVersionId;
	}

	public void setNextVersionId(Integer nextVersionId) {
		this.nextVersionId = nextVersionId;
	}

	public Integer getParentVersionId() {
		return parentVersionId;
	}

	public void setParentVersionId(Integer parentVersionId) {
		this.parentVersionId = parentVersionId;
	}


	public List<CRFPage> getCrfPages() {
		return crfPages;
	}

	public CRF getCopy() {
		CRF copiedCrf = new CRF();
		copiedCrf.setTitle("Copy of " + title + "_" + System.currentTimeMillis());
		copiedCrf.setDescription(description);
		copiedCrf.setStatus(CrfStatus.DRAFT);
		copiedCrf.setCrfVersion("1.0");
		for (CRFPage crfPage : crfPages) {
			copiedCrf.addCrfPge(crfPage.getCopy());
		}

		return copiedCrf;


	}


	public void addCrfPge(final CRFPage crfPage) {
		if (crfPage != null) {
			crfPage.setCrf(this);
			crfPages.add(crfPage);
		}


	}

	public void addOrUpdateCrfItemInCrfPage(final int crfPageIndex, final ProCtcQuestion proCtcQuestion, final int displayOrder) {
		CRFPage crfPage = getCrfPageByQuestion(proCtcQuestion);

		CRFPage anotherCrfPage = getCrfPages().get(crfPageIndex);


		if (crfPage != null && !anotherCrfPage.equals(crfPage)) {
			crfPage.removeExistingButDoNotAddNewCrfItem(proCtcQuestion);
		}
		anotherCrfPage.addOrUpdateCrfItem(proCtcQuestion, displayOrder);


	}


	private CRFPage getCrfPageByQuestion(final ProCtcQuestion proCtcQuestion) {
		for (CRFPage crfPage : crfPages) {
			CrfPageItem crfPageItem = crfPage.getCrfPageItemByQuestion(proCtcQuestion);
			if (crfPageItem != null) {
				return crfPage;
			}
		}
		return null;
	}


	public List<CrfPageItem> getAllCrfPageItems() {
		List<CrfPageItem> crfPageItems = new ArrayList<CrfPageItem>();
		for (CRFPage crfPage : crfPages) {
			crfPageItems.addAll(crfPage.getCrfPageItems());
		}
		return crfPageItems;

	}

	public CrfPageItem getCrfPageItemByQuestion(final ProCtcQuestion proCtcQuestion) {
		CRFPage crfPage = getCrfPageByQuestion(proCtcQuestion);
		return crfPage != null ? crfPage.getCrfPageItemByQuestion(proCtcQuestion) : null;


	}
}
