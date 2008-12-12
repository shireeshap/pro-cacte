package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "crf_items")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_items_id")})
public class CrfItem extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder = 0;

	@JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = true)
	@ManyToOne
	private CRF crf;

	@JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcQuestion proCtcQuestion;

	@Column(name = "response_required")
	private Boolean responseRequired = Boolean.FALSE;

	@Column(name = "instructions")
	private String instructions;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "allignment")
	private CrfItemAllignment crfItemAllignment = CrfItemAllignment.VERTICAL;

	public CrfItem() {
	}

	public CrfItem(Integer id) {
		this.id = id;
	}

	public Boolean getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(final Boolean responseRequired) {
		this.responseRequired = responseRequired;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(final String instructions) {
		this.instructions = instructions;
	}

	public CrfItemAllignment getCrfItemAllignment() {
		return crfItemAllignment;
	}

	public void setCrfItemAllignment(final CrfItemAllignment crfItemAllignment) {
		this.crfItemAllignment = crfItemAllignment;
	}

	public CrfItem(Integer id, Integer displayOrder) {
		this.id = id;
		this.displayOrder = displayOrder;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}


	public CRF getCrf() {
		return crf;
	}

	public void setCrf(CRF crf) {
		this.crf = crf;
	}

	public ProCtcQuestion getProCtcQuestion() {
		return proCtcQuestion;
	}

	public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
		this.proCtcQuestion = proCtcQuestion;
	}

	public CrfItem getCopy() {
		CrfItem copiedCrfItem = new CrfItem();
		copiedCrfItem.setInstructions(instructions);
		copiedCrfItem.setDisplayOrder(displayOrder);
		copiedCrfItem.setResponseRequired(responseRequired);
		copiedCrfItem.setCrfItemAllignment(crfItemAllignment);
		copiedCrfItem.setProCtcQuestion(proCtcQuestion);


		return copiedCrfItem;
	}



}
