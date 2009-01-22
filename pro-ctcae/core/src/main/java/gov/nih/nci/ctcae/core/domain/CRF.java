package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "crfs")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crfs_id")})

public class CRF extends BaseVersionable {


    private static final Log logger = LogFactory.getLog(CRF.class);
    private static final Integer INITIAL_ORDER = 0;

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

    @JoinColumn(name = "study_id", referencedColumnName = "id")
    @ManyToOne
    private Study study;

    @Column(name = "recall_period", nullable = false)
    private String recallPeriod = RecallPeriod.WEEKLY.getDisplayName();

    @Column(name = "crf_creation_mode", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrfCreationMode crfCreationMode = CrfCreationMode.BASIC;

    public String getRecallPeriod() {
        return recallPeriod;
    }

    public void setRecallPeriod(String recallPeriod) {
        this.recallPeriod = recallPeriod;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
    private Collection<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    public Collection<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        if (studyParticipantCrf != null) {
            studyParticipantCrf.setCrf(this);
            studyParticipantCrfs.add(studyParticipantCrf);
        }
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRF crf = (CRF) o;

        if (crfVersion != null ? !crfVersion.equals(crf.crfVersion) : crf.crfVersion != null) return false;
        if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
        if (effectiveEndDate != null ? !effectiveEndDate.equals(crf.effectiveEndDate) : crf.effectiveEndDate != null)
            return false;
        if (effectiveStartDate != null ? !effectiveStartDate.equals(crf.effectiveStartDate) : crf.effectiveStartDate != null)
            return false;
        if (nextVersionId != null ? !nextVersionId.equals(crf.nextVersionId) : crf.nextVersionId != null) return false;
        if (parentVersionId != null ? !parentVersionId.equals(crf.parentVersionId) : crf.parentVersionId != null)
            return false;
        if (status != crf.status) return false;
        if (study != null ? !study.equals(crf.study) : crf.study != null) return false;
        if (title != null ? !title.equals(crf.title) : crf.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (crfVersion != null ? crfVersion.hashCode() : 0);
        result = 31 * result + (effectiveStartDate != null ? effectiveStartDate.hashCode() : 0);
        result = 31 * result + (effectiveEndDate != null ? effectiveEndDate.hashCode() : 0);
        result = 31 * result + (nextVersionId != null ? nextVersionId.hashCode() : 0);
        result = 31 * result + (parentVersionId != null ? parentVersionId.hashCode() : 0);
        result = 31 * result + (study != null ? study.hashCode() : 0);
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
        Collections.sort(crfPages, new DisplayOrderComparator());
        return crfPages;
    }




    public void removeCrfPageByPageNumber(final Integer crfPageNumber) {

        CRFPage crfPage = getCrfPageByPageNumber(crfPageNumber);
        removeCrfPage(crfPage);

    }

    /**
     * it is required to make sure crf page numbers stars from  INITIAL_ORDER not from 2,3,4
     */
    public void updatePageNumberOfCrfPages() {
        List<CRFPage> crfPages = getCrfPages();
        for (int i = 0; i < crfPages.size(); i++) {
            CRFPage crfPage = crfPages.get(i);
            crfPage.setPageNumber(INITIAL_ORDER + i);
        }

    }

    private void removeCrfPage(final CRFPage crfPage) {
        if (crfPage != null) {
            crfPages.remove(crfPage);
        } else {
            logger.error("can not remove crf page because crf page is null");
        }
    }

    public CRF getCopy() {
        CRF copiedCrf = new CRF();
        copiedCrf.setTitle("Copy of " + title + "_" + System.currentTimeMillis());
        copiedCrf.setDescription(description);
        copiedCrf.setStatus(CrfStatus.DRAFT);
        copiedCrf.setCrfVersion(crfVersion);
        copiedCrf.setStudy(getStudy());
        for (CRFPage crfPage : crfPages) {
            copiedCrf.addCrfPage(crfPage.getCopy());
        }

        return copiedCrf;


    }


    public void addCrfPage(final CRFPage crfPage) {
        if (crfPage != null) {
            crfPage.setCrf(this);
            crfPages.add(crfPage);
            crfPage.setPageNumber(getCrfPageNumber());
        }


    }

    private int getCrfPageNumber() {
        return crfPages.size() - 1;
    }

    public void addOrUpdateCrfItemInCrfPage(final int crfPageNumber, final ProCtcQuestion proCtcQuestion, final Integer displayOrder) {
        CRFPage crfPage = getCrfPageByQuestion(proCtcQuestion);

        CRFPage anotherCrfPage = getCrfPageByPageNumber(crfPageNumber);

        CrfPageItem existingCrfPageItem = null;
        if (crfPage != null && !anotherCrfPage.equals(crfPage)) {
            existingCrfPageItem = crfPage.removeExistingButDoNotAddNewCrfItem(proCtcQuestion);
        }
        if (existingCrfPageItem != null) {
            anotherCrfPage.addOrUpdateCrfItem(existingCrfPageItem);
        } else {
            anotherCrfPage.addOrUpdateCrfItem(proCtcQuestion, displayOrder);
        }

    }

    public CRFPage getCrfPageByPageNumber(final Integer crfPageNumber) {
        for (CRFPage crfPage : getCrfPages()) {
            if (crfPage.getPageNumber().equals(crfPageNumber)) {
                return crfPage;
            }
        }
        return null;

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

    private CRFPage getCrfPageByQuestion(final Integer questionId) {
        for (CRFPage crfPage : crfPages) {
            CrfPageItem crfPageItem = crfPage.getCrfPageItemByQuestion(questionId);
            if (crfPageItem != null) {
                return crfPage;
            }
        }
        return null;
    }

    public boolean isVersioned() {
        if (this.getParentVersionId() != null || this.getNextVersionId() != null) {
            return true;
        }
        return false;
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

    public CrfPageItem getCrfPageItemByQuestion(final Integer questionId) {
        CRFPage crfPage = getCrfPageByQuestion(questionId);
        return crfPage != null ? crfPage.getCrfPageItemByQuestion(questionId) : null;


    }


    public void updateCrfPageItemDisplayRules(final ProCtcQuestion proCtcQuestion) {
        Set<Integer> proCtcValidValues = new HashSet<Integer>();

        for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
            proCtcValidValues.add(proCtcValidValue.getId());
        }

        //now remove the  display rules
        for (CrfPageItem crfPageItem : getAllCrfPageItems()) {
            crfPageItem.removeCrfPageItemDisplayRulesByIds(proCtcValidValues);


        }

    }

    public void updateCrfPageItemDisplayRules(final Integer selectedCrfPageNumber) {
        CRFPage crfPage = getCrfPageByPageNumber(selectedCrfPageNumber);
        if (crfPage != null) {
            List<CrfPageItem> crfPageItems = crfPage.getCrfPageItems();
            for (CrfPageItem crfPageItem : crfPageItems) {
                updateCrfPageItemDisplayRules(crfPageItem.getProCtcQuestion());
            }
        } else {
            logger.error("can not find crf page for given page number:" + selectedCrfPageNumber);
        }

    }

    public CRFPage addCrfPage(final ProCtcQuestion proCtcQuestion) {
        CRFPage crfPage = addCrfPage();
        addOrUpdateCrfItemInCrfPage(getCrfPageNumber(), proCtcQuestion, 0);
        return crfPage;

    }

    public CRFPage addCrfPage() {
        if (getAdvance()) {
            CRFPage crfPage = new CRFPage();
            addCrfPage(crfPage);
            return crfPage;

        }
        throw new CtcAeSystemException("You can not add new page in basic form creation mode.");


    }


    public Object addProCtcTerm(ProCtcTerm proCtcTerm) {
        //first check if pro ctc term exists

        if (!getAdvance()) {
            CRFPage crfPage = getCrfPageByProCtcTerm(proCtcTerm);

            if (crfPage == null) {
                crfPage = new CRFPage();
                addCrfPage(crfPage);
                crfPage.addProCtcTerm(proCtcTerm);
                return crfPage;
            } else {

                List<CrfPageItem> addedCrfPageItems = crfPage.addProCtcTerm(proCtcTerm);
                return addedCrfPageItems;
            }
        }
        return null;

    }

    private CRFPage getCrfPageByProCtcTerm(ProCtcTerm proCtcTerm) {
        for (CRFPage crfPage : getCrfPages()) {
            if (crfPage.checkIfProCtcTermExists(proCtcTerm)) {
                return crfPage;
            }
        }

        return null;

    }

    public CrfCreationMode getCrfCreationMode() {
        return crfCreationMode;
    }

    public void setCrfCreationMode(CrfCreationMode crfCreationMode) {
        this.crfCreationMode = crfCreationMode;
    }

    public Boolean getAdvance() {
        return getCrfCreationMode().equals(CrfCreationMode.ADVANCE);


    }

    /**
     * This is used when user deletes a crf page
     *
     * @param questionIds
     */
    public void removeCrfPageItemByQuestionIds(Set<Integer> questionIds) {
        for (Integer questionId : questionIds) {
            CrfPageItem crfPageItem = getCrfPageItemByQuestion(questionId);
            CRFPage crfPage = getCrfPageByQuestion(questionId);
            if (crfPage != null) {
                crfPage.removeCrfPageItem(crfPageItem);
            } else {
                logger.error("can not find crf page for the question:" + questionId);
            }

        }

    }

    public void clearEmptyPages() {
        List<CRFPage> emptyCrfPages = new ArrayList<CRFPage>();
        for (CRFPage crfPage : getCrfPages()) {
            if (crfPage.getCrfPageItems().isEmpty()) {
                emptyCrfPages.add(crfPage);
            }
        }
        for (CRFPage crfPage : emptyCrfPages) {
            removeCrfPage(crfPage);
        }


    }

    public void updateDisplayOrderOfCrfPageItems() {
        int i = CrfPageItem.INITIAL_ORDER;
        for (CRFPage crfPage : getCrfPages()) {
            crfPage.updateDisplayOrderOfCrfPageItems(i);
            i = i + crfPage.getCrfPageItems().size();
        }

    }

    public void updateCrfPageInstructions() {
        for (CRFPage crfPage : crfPages) {
            crfPage.updateInstructions();
        }

    }
}
