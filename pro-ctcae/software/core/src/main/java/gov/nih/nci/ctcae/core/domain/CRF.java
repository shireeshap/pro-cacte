package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.ItemBank;
import gov.nih.nci.ctcae.core.domain.rules.CRFNotificationRule;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRule;
import gov.nih.nci.ctcae.core.domain.rules.SiteCRFNotificationRule;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

//

/**
 * The Class CRF.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CRFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crfs_id")})

public class CRF extends BaseVersionable implements Comparable<CRF>{


    /**
     * The Constant INITIAL_ORDER.
     */
    private static final Integer INITIAL_ORDER = 0;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The title.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * The description.
     */
    @Column(name = "description")
    private String description;

    /**
     * The status.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrfStatus status = CrfStatus.DRAFT;

    /**
     * The crf version.
     */
    @Column(name = "crf_version", nullable = false)
    private String crfVersion;

    /**
     * The effective start date.
     */
    @Column(name = "effective_start_date", nullable = true)
    private Date effectiveStartDate;

    /**
     * The effective end date.
     */
    @Column(name = "effective_end_date", nullable = true)
    private Date effectiveEndDate;

    @JoinColumn(name = "next_version_id", referencedColumnName = "id", nullable = true)
    @OneToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private CRF childCrf;

    @JoinColumn(name = "parent_version_id", referencedColumnName = "id", nullable = true)
    @OneToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private CRF parentCrf;

    /**
     * The study participant crfs.
     */
    @OneToMany(mappedBy = "crf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    @OneToMany(mappedBy = "crf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<SiteCRFNotificationRule> siteCRFNotificationRules = new ArrayList();

    /**
     * The crf pages.
     */
    @OneToMany(mappedBy = "crf", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRFPage> crfPages = new LinkedList<CRFPage>();

    @OneToMany(mappedBy = "crf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRFNotificationRule> crfNotificationRules = new LinkedList<CRFNotificationRule>();


    /**
     * The study.
     */
    @JoinColumn(name = "study_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Study study;

    /**
     * The recall period.
     */
    @Column(name = "recall_period", nullable = false)
    private String recallPeriod = RecallPeriod.WEEKLY.getDisplayName();

    /**
     * The crf creation mode.
     */
    @Column(name = "crf_creation_mode", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrfCreationMode crfCreationMode = CrfCreationMode.BASIC;

    @OneToMany(mappedBy = "crf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<FormArmSchedule> formArmSchedules = new LinkedList<FormArmSchedule>();

    @Column(name = "activity_date", nullable = true)
    private Date activityDate;

    @Column(name = "is_baseline", nullable = true)
    private Boolean createBaseline = true;

    @Column(name = "is_hidden", nullable = false)
    private Boolean hidden = false;

    @Column(name = "is_eq5d", nullable = false)
    private Boolean eq5d = false;
    
    public Boolean isEq5d() {
        return eq5d;
    }
    
    public Boolean getEq5d() {
        return eq5d;
    }
    
    public void setEq5d(Boolean eq5d) {
        this.eq5d = eq5d;
    }
    
    public Boolean isHidden() {
        return hidden;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isCreateBaseline() {
        if (createBaseline == null) {
            return true;
        }
        return createBaseline;
    }

    public Boolean getCreateBaseline() {
        return isCreateBaseline();
    }

    public void setCreateBaseline(Boolean createBaseline) {
        this.createBaseline = createBaseline;
    }

    /**
     * Gets the recall period.
     *
     * @return the recall period
     */
    public String getRecallPeriod() {
        return recallPeriod.trim();
    }

    public Boolean isEq5d3L() {
    	if(getCrfPagesSortedByPageNumber().isEmpty()){
    		return false;
    	}
         for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
             for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
            	 ProCtcTerm proCtcTerm = crfPageItem.getProCtcQuestion().getProCtcTerm();
            	 if( !proCtcTerm.getCtcTerm().getCategoryTermSets().get(0).getCategory().getName().equalsIgnoreCase(ItemBank.EQ5D3L.getCode())){
            		 return false;
            	 }
             }
         }
         return true;
    }
    
    public Boolean isEq5d5L() {
    	if(getCrfPagesSortedByPageNumber().isEmpty()){
    		return false;
    	}
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
	           	 ProCtcTerm proCtcTerm = crfPageItem.getProCtcQuestion().getProCtcTerm();
	           	 if( !proCtcTerm.getCtcTerm().getCategoryTermSets().get(0).getCategory().getName().equalsIgnoreCase(ItemBank.EQ5D5L.getCode())){
	           		 return false;
	           	 }
            }
        }
        return true;
    }
    
    public boolean checkForParent(CRF crf) {
        if (crf.getParentCrf() != null) {
            CRF tempCrf = crf.getParentCrf();
            if (this.equals(tempCrf)) {
                return true;
            } else {
                return checkForParent(tempCrf);
            }
        }
        return false;
    }

    /**
     * Gets the recall period in spanish. Temporary arrangement. Only returns spanish for 7 days/30 days...everything else is mapped to
     * "since your last cancer treatment".
     *
     * @return the recall period
     */
    public String getRecallPeriodInSpanish() {
        String recallPeriodInSpanish = "";
        if (recallPeriod.trim().contains("7")) {
            recallPeriodInSpanish = "en los últimos 7 días";
        } else if (recallPeriod.trim().contains("30")) {
            recallPeriodInSpanish = "en los últimos 30 días";
        } else if (recallPeriod.trim().contains("last cancer treatment")) {
            recallPeriodInSpanish = "desde su último tratamiento para el cáncer";
        } else {
            recallPeriodInSpanish = recallPeriod.trim();
        }
        return recallPeriodInSpanish;
    }

    /**
     * Sets the recall period.
     *
     * @param recallPeriod the new recall period
     */
    public void setRecallPeriod(String recallPeriod) {
        this.recallPeriod = recallPeriod;
    }

    /**
     * Gets the study participant crfs.
     *
     * @return the study participant crfs
     */
    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    public List<FormArmSchedule> getNonDefaultFormArmSchedules() {
        List<FormArmSchedule> ndformarms = new ArrayList<FormArmSchedule>();
        for (FormArmSchedule formarm : getFormArmSchedules()) {
            if (!formarm.getArm().isDefaultArm()) {
                ndformarms.add(formarm);
            }
        }
        return ndformarms;
    }

    /**
     * Adds the study participant crf.
     *
     * @param studyParticipantCrf the study participant crf
     */
    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        if (studyParticipantCrf != null) {
            studyParticipantCrf.setCrf(this);
            studyParticipantCrfs.add(studyParticipantCrf);
        }
    }

    /**
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Sets the study.
     *
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * Instantiates a new cRF.
     */
    public CRF() {
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

    /**
     * Gets the title.
     *
     * @return the title
     */
    @NotEmpty(message = "Missing Title")
    @UniqueTitleForCrf
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Gets the status.
     *
     * @return the status
     */
    public CrfStatus getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(CrfStatus status) {
        this.status = status;
    }

    /**
     * Gets the crf version.
     *
     * @return the crf version
     */
    public String getCrfVersion() {
        return crfVersion;
    }

    /**
     * Sets the crf version.
     *
     * @param crfVersion the new crf version
     */
    public void setCrfVersion(String crfVersion) {
        this.crfVersion = crfVersion;
    }

    public List<SiteCRFNotificationRule> getSiteCRFNotificationRules() {
        return siteCRFNotificationRules;
    }

    public void setSiteCRFNotificationRules(List<SiteCRFNotificationRule> siteCRFNotificationRules) {
        this.siteCRFNotificationRules = siteCRFNotificationRules;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRF crf = (CRF) o;

        if (crfVersion != null ? !crfVersion.equals(crf.crfVersion) : crf.crfVersion != null) return false;
        if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
        if (effectiveEndDate != null ? !DateUtils.format(effectiveEndDate).equals(DateUtils.format(crf.effectiveEndDate)) : crf.effectiveEndDate != null)
            return false;
        if (effectiveStartDate != null ? !DateUtils.format(effectiveStartDate).equals(DateUtils.format(crf.effectiveStartDate)) : crf.effectiveStartDate != null)
            return false;
        if (status != crf.status) return false;
        if (study != null ? !study.equals(crf.study) : crf.study != null) return false;
        if (title != null ? !title.equals(crf.title) : crf.title != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (crfVersion != null ? crfVersion.hashCode() : 0);
        result = 31 * result + (effectiveStartDate != null ? effectiveStartDate.hashCode() : 0);
        result = 31 * result + (effectiveEndDate != null ? effectiveEndDate.hashCode() : 0);
        result = 31 * result + (study != null ? study.hashCode() : 0);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Checks if is released.
     *
     * @return true, if is released
     */
    public boolean isReleased() {
        return getStatus().equals(CrfStatus.RELEASED);
    }

    /**
     * Gets the effective start date.
     *
     * @return the effective start date
     */
    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    /**
     * Sets the effective start date.
     *
     * @param effectiveStartDate the new effective start date
     */
    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    /**
     * Gets the effective end date.
     *
     * @return the effective end date
     */
    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    /**
     * Sets the effective end date.
     *
     * @param effectiveEndDate the new effective end date
     */
    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }


    /**
     * Gets the crf pages sorted by page number.
     *
     * @return the crf pages sorted by page number
     */
    public List<CRFPage> getCrfPagesSortedByPageNumber() {
        List<CRFPage> sortedCrfPages = new ArrayList<CRFPage>(crfPages);

        Collections.sort(sortedCrfPages, new DisplayOrderComparator());
        return sortedCrfPages;
    }


    /**
     * Removes the crf page by page number.
     *
     * @param crfPageNumber the crf page number
     */
    public void removeCrfPageByPageNumber(final Integer crfPageNumber) {

        CRFPage crfPage = getCrfPageByPageNumber(crfPageNumber);
        removeCrfPage(crfPage);

    }

    /**
     * it is required to make sure crf page numbers stars from  INITIAL_ORDER not from 2,3,4.
     */
    public void updatePageNumberOfCrfPages() {
        List<CRFPage> crfPages = getCrfPagesSortedByPageNumber();
        for (int i = 0; i < crfPages.size(); i++) {
            CRFPage crfPage = crfPages.get(i);
            crfPage.setPageNumber(INITIAL_ORDER + i);
        }

    }

    /**
     * Removes the crf page.
     *
     * @param crfPage the crf page
     */
    private void removeCrfPage(final CRFPage crfPage) {
        if (crfPage != null) {
            crfPages.remove(crfPage);
        } else {
            logger.error("can not remove crf page because crf page is null");
        }
    }
    
    public void removeAllCrfPages(){
    	for(CRFPage crfPage: crfPages){
    		crfPage.getCrfPageItems().clear();
    	}
    	crfPages.clear();
    }

    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public CRF copy() {
        CRF copiedCrf = new CRF();
        copiedCrf.setTitle("Copy of " + title + "_" + System.currentTimeMillis());
        copiedCrf.setDescription(description);
        copiedCrf.setStatus(CrfStatus.DRAFT);
        copiedCrf.setCrfVersion(crfVersion);
        copiedCrf.setStudy(getStudy());
        copiedCrf.setEq5d(eq5d);
        for (CRFPage crfPage : crfPages) {
            copiedCrf.addCrfPage(crfPage.copy());
        }
        for (FormArmSchedule formArmSchedule : formArmSchedules) {
            copiedCrf.addFormArmSchedule(formArmSchedule.copy());
        }
        return copiedCrf;


    }

    private void addFormArmSchedule(FormArmSchedule formArmSchedule) {
        formArmSchedule.setCrf(this);
        formArmSchedules.add(formArmSchedule);
    }


    /**
     * Adds the crf page.
     *
     * @param crfPage the crf page
     */
    public void addCrfPage(final CRFPage crfPage) {
        if (crfPage != null) {
            crfPage.setCrf(this);
            crfPages.add(crfPage);
            crfPage.setPageNumber(getCrfPageNumber());
        }


    }

    /**
     * Gets the crf page number.
     *
     * @return the crf page number
     */
    private int getCrfPageNumber() {
        return crfPages.size() - 1;
    }


    /**
     * Gets the crf page by page number.
     *
     * @param crfPageNumber the crf page number
     * @return the crf page by page number
     */
    public CRFPage getCrfPageByPageNumber(final Integer crfPageNumber) {
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            if (crfPage.getPageNumber().equals(crfPageNumber)) {
                return crfPage;
            }
        }
        return null;

    }


    /**
     * Gets the crf page by question.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page by question
     */
    private CRFPage getCrfPageByQuestion(final ProCtcQuestion proCtcQuestion) {
        for (CRFPage crfPage : crfPages) {
            CrfPageItem crfPageItem = crfPage.getCrfPageItemByQuestion(proCtcQuestion);
            if (crfPageItem != null) {
                return crfPage;
            }
        }
        return null;
    }

    /**
     * Gets the crf page by question.
     *
     * @param questionId the question id
     * @return the crf page by question
     */
    private CRFPage getCrfPageByQuestion(final Integer questionId) {
        for (CRFPage crfPage : crfPages) {
            CrfPageItem crfPageItem = crfPage.getCrfPageItemByQuestion(questionId);
            if (crfPageItem != null) {
                return crfPage;
            }
        }
        return null;
    }


    /**
     * Gets the all crf page items.
     *
     * @return the all crf page items
     */
    public List<CrfPageItem> getAllCrfPageItems() {
        List<CrfPageItem> crfPageItems = new ArrayList<CrfPageItem>();
        for (CRFPage crfPage : crfPages) {
            crfPageItems.addAll(crfPage.getCrfPageItems());
        }
        return crfPageItems;

    }

    /**
     * Gets the crf page item by question.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item by question
     */
    public CrfPageItem getCrfPageItemByQuestion(final ProCtcQuestion proCtcQuestion) {
        CRFPage crfPage = getCrfPageByQuestion(proCtcQuestion);
        return crfPage != null ? crfPage.getCrfPageItemByQuestion(proCtcQuestion) : null;


    }

    /**
     * Gets the crf page item by question.
     *
     * @param questionId the question id
     * @return the crf page item by question
     */
    public CrfPageItem getCrfPageItemByQuestion(final Integer questionId) {
        CRFPage crfPage = getCrfPageByQuestion(questionId);
        return crfPage != null ? crfPage.getCrfPageItemByQuestion(questionId) : null;


    }


    /**
     * Update crf page item display rules.
     *
     * @param proCtcQuestion the pro ctc question
     */
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

    /**
     * Update crf page item display rules.
     *
     * @param selectedCrfPageNumber the selected crf page number
     */
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


    /**
     * Adds the pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     * @return the object
     */
    public Object addProCtcTerm(ProCtcTerm proCtcTerm) {
        //first check if pro ctc term exists
        if (!getAdvance()) {
            CRFPage crfPage = getCrfPageByProCtcTerm(proCtcTerm);
            if (crfPage == null) {
                crfPage = new CRFPage();
                addCrfPage(crfPage);
                crfPage.addProCtcTerm(proCtcTerm);
                crfPage.setProCtcTerm(proCtcTerm);
                return crfPage;
            } else {
                return crfPage.addProCtcTerm(proCtcTerm);
            }
        }
        return null;
    }

    /**
     * Gets the crf page by pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     * @return the crf page by pro ctc term
     */
    private CRFPage getCrfPageByProCtcTerm(ProCtcTerm proCtcTerm) {
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            if (crfPage.checkIfProCtcTermExists(proCtcTerm)) {
                return crfPage;
            }
        }

        return null;

    }

    /**
     * Gets the crf creation mode.
     *
     * @return the crf creation mode
     */
    public CrfCreationMode getCrfCreationMode() {
        return crfCreationMode;
    }

    /**
     * Sets the crf creation mode.
     *
     * @param crfCreationMode the new crf creation mode
     */
    public void setCrfCreationMode(CrfCreationMode crfCreationMode) {
        this.crfCreationMode = crfCreationMode;
    }

    /**
     * Gets the advance.
     *
     * @return the advance
     */
    public Boolean getAdvance() {
        return getCrfCreationMode().equals(CrfCreationMode.ADVANCE);


    }

    /**
     * This is used when user deletes a pro csetCrtc question.
     *
     * @param proCtcQuestion the proCtcQuestion to remove
     */
    public void removeCrfPageItemByQuestion(ProCtcQuestion proCtcQuestion) {
        CrfPageItem crfPageItem = getCrfPageItemByQuestion(proCtcQuestion);
        CRFPage crfPage = getCrfPageByQuestion(proCtcQuestion);
        if (crfPage != null) {
            crfPage.removeCrfPageItem(crfPageItem);
        } else {
            logger.error("can not find crf page for the question:" + proCtcQuestion);
        }
        updateCrfPageItemDisplayRules(proCtcQuestion);

    }


    /**
     * Clear empty pages.
     */
    public void clearEmptyPages() {
        List<CRFPage> emptyCrfPages = new ArrayList<CRFPage>();
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            if (crfPage.getCrfPageItems().isEmpty()) {
                emptyCrfPages.add(crfPage);
            }
        }
        for (CRFPage crfPage : emptyCrfPages) {
            removeCrfPage(crfPage);
        }


    }


    /**
     * Update crf page instructions.
     */
    public void updateCrfPageInstructions() {
        for (CRFPage crfPage : crfPages) {
            crfPage.updateInstructions();
        }

    }


    public FormArmSchedule addFormArmSchedule(Arm arm) {
        FormArmSchedule formArmSchedule = new FormArmSchedule();
        formArmSchedule.setArm(arm);
        formArmSchedule.setCrf(this);
        formArmSchedules.add(formArmSchedule);
        return formArmSchedule;
    }

    public List<FormArmSchedule> getFormArmSchedules() {
        return formArmSchedules;
    }

    public void setFormArmSchedules(List<FormArmSchedule> formArmSchedules) {
        this.formArmSchedules = formArmSchedules;
    }

    public FormArmSchedule getFormArmScheduleForArm(Arm arm) {
        for (FormArmSchedule formArmSchedule : getFormArmSchedules()) {
            if (formArmSchedule.getArm().equals(arm)) {
                return formArmSchedule;
            }
        }
        return null;
    }

    public CRF getChildCrf() {
        return childCrf;
    }

    public CRF getParentCrf() {
        return parentCrf;
    }

    public void setChildCrf(CRF childCrf) {
        this.childCrf = childCrf;
    }

    public void setParentCrf(CRF parentCrf) {
        this.parentCrf = parentCrf;
    }

    public Set<ProCtcTerm> getAllProCtcTermsInCrf() {
        Set<ProCtcTerm> proCtcTerms = new LinkedHashSet<ProCtcTerm>();
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                proCtcTerms.add(crfPageItem.getProCtcQuestion().getProCtcTerm());
            }
        }
        return proCtcTerms;
    }

    public Set<ProCtcQuestionType> getAllQuestionTypes() {
        Set<ProCtcQuestionType> questionTypes = new LinkedHashSet<ProCtcQuestionType>();
        for (CRFPage crfPage : getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                ProCtcQuestionType questionType = crfPageItem.getProCtcQuestion().getProCtcQuestionType();
                if (questionType.equals(ProCtcQuestionType.SEVERITY)
                        || questionType.equals(ProCtcQuestionType.INTERFERENCE)
                        || questionType.equals(ProCtcQuestionType.FREQUENCY)
                        || questionType.equals(ProCtcQuestionType.PRESENT))
                    questionTypes.add(questionType);
            }
        }
        return questionTypes;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public List<CRFPage> getCrfPages() {
        return crfPages;
    }

    public void setCrfPages(List<CRFPage> crfPages) {
        this.crfPages = crfPages;
    }

    public List<CRFNotificationRule> getCrfNotificationRules() {
        return crfNotificationRules;
    }

    public void setCrfNotificationRules(List<CRFNotificationRule> crfNotificationRules) {
        this.crfNotificationRules = crfNotificationRules;
    }

    public void addCrfNotificationRule(CRFNotificationRule crfNotificationRule) {
        if (crfNotificationRule != null) {
            crfNotificationRule.setCrf(this);
            crfNotificationRules.add(crfNotificationRule);
        }
    }

    public List<NotificationRule> getNotificationRules() {
        List<NotificationRule> notificationRules = new ArrayList<NotificationRule>();
        for (CRFNotificationRule crfNotificationRule : getCrfNotificationRules()) {
            notificationRules.add(crfNotificationRule.getNotificationRule());
        }
        return notificationRules;

    }

	@Override
	public int compareTo(CRF o) {
		return this.getTitle().toLowerCase().compareTo(o.getTitle().toLowerCase());
	}
}
