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
 * The Class CRFPage.
 *
 * @author Vinay Kumar
 * @created Dec 29, 2008
 */

@Entity
@Table(name = "CRF_PAGES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_pages_id")})

public class CRFPage extends BaseVersionable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFPage.class);

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    /**
     * The description.
     */
    @Column(name = "description")
    private String description;

    /**
     * The instructions.
     */
    @Column(name = "instructions")
    private String instructions;

    /**
     * The page number.
     */
    @Column(name = "page_number", nullable = false)
    private Integer pageNumber = 0;

    /**
     * The crf page items.
     */
    @OneToMany(mappedBy = "crfPage", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CrfPageItem> crfPageItems = new LinkedList<CrfPageItem>();


    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRF crf;


    /**
     * Instantiates a new cRF page.
     */
    public CRFPage() {
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

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the new page number
     */
    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the crf page items.
     *
     * @return the crf page items
     */
    public List<CrfPageItem> getCrfPageItems() {
        return crfPageItems;
    }


    /**
     * Removes the crf page item.
     *
     * @param crfPageItem the crf page item
     */
    public void removeCrfPageItem(CrfPageItem crfPageItem) {
        if (crfPageItem != null) {
            this.crfPageItems.remove(crfPageItem);

            //now reorder the crf page items
            List<CrfPageItem> crfPageItems = getCrfPageItems();
            for (int i = 0; i < crfPageItems.size(); i++) {
                CrfPageItem anotherCrfPageItem = crfPageItems.get(i);
                anotherCrfPageItem.setDisplayOrder(i + CrfPageItem.INITIAL_ORDER);
            }
        }
    }


//	@Override ///not required because two crf page will be different only if there ids are different
//	public boolean equals(final Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//
//		final CRFPage crf = (CRFPage) o;
//
//		if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
//
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		int result = description != null ? description.hashCode() : 0;
//		return result;
//	}


    /**
     * Gets the crf page item by question.
     *
     * @param questionId the question id
     * @return the crf page item by question
     */
    public CrfPageItem getCrfPageItemByQuestion(final Integer questionId) {
        if (questionId != null) {
            for (CrfPageItem existingCrfPageItem : getCrfPageItems()) {
                if (existingCrfPageItem.getProCtcQuestion() != null
                        && (existingCrfPageItem.getProCtcQuestion().getId().equals(questionId))) {
                    return existingCrfPageItem;
                }
            }

        }
        return null;

    }

    /**
     * Gets the crf page item by question.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item by question
     */
    public CrfPageItem getCrfPageItemByQuestion(final ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {
            for (CrfPageItem existingCrfPageItem : getCrfPageItems()) {
                if (existingCrfPageItem.getProCtcQuestion() != null
                        && (existingCrfPageItem.getProCtcQuestion().equals(proCtcQuestion))) {
                    return existingCrfPageItem;
                }
            }

        }
        return null;

    }

    /**
     * Adds the crf item.
     *
     * @param crfPageItem the crf page item
     */
    public void addCrfPageItem(CrfPageItem crfPageItem) {
        if (crfPageItem != null) {
            crfPageItem.setCrfPage(this);
            crfPageItem.setDisplayOrder(getCrfPageItems().size());
            crfPageItems.add(crfPageItem);
        }
    }


    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public CRFPage getCopy() {

        CRFPage copiedCrfPage = new CRFPage();
        copiedCrfPage.setDescription(description);
        for (CrfPageItem crfPageItem : crfPageItems) {
            copiedCrfPage.addCrfPageItem(crfPageItem.getCopy());
        }

        return copiedCrfPage;
    }


    /**
     * Adds the pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     * @return the list< crf page item>
     */
    public List<CrfPageItem> addProCtcTerm(final ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>(proCtcTerm.getProCtcQuestions());
            List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();
            for (int i = 0; i < questions.size(); i++) {
                ProCtcQuestion proCtcQuestion = questions.get(i);
                CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(proCtcQuestion);

                if (existingCrfPageItem == null) {
                    CrfPageItem crfPageItem = new CrfPageItem(proCtcQuestion);
                    addCrfPageItem(crfPageItem);
                    addedCrfPageItems.add(crfPageItem);
                } else {
                    logger.debug("question has already been added." + proCtcQuestion.toString());
                }


            }
            return addedCrfPageItems;
        }
        logger.error("can not add proCtcTerm because pro ctc term is null");

        return null;
    }

    /**
     * Update instructions.
     */
    public void updateInstructions() {
        if (!getCrfPageItems().isEmpty()) {
            ProCtcTerm proCtcTerm = getCrfPageItems().get(0).getProCtcQuestion().getProCtcTerm();
            setInstructions(String.format("Please think back %s:", getCrf().getRecallPeriod(), proCtcTerm.getTerm()));
        }
    }


    /**
     * Check if pro ctc term exists.
     *
     * @param proCtcTerm the pro ctc term
     * @return true, if successful
     */
    public boolean checkIfProCtcTermExists(ProCtcTerm proCtcTerm) {
        for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
            CrfPageItem crfPageItem = getCrfPageItemByQuestion(proCtcQuestion);
            if (crfPageItem != null) {
                return true;
            }
        }
        return false;

    }

    /**
     * Gets the instructions.
     *
     * @return the instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions.
     *
     * @param instructions the new instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}