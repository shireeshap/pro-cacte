package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author mehul
 */

@Entity
@Table(name = "STUDY_INVESTGATORS")
public class StudyInvestigator extends BasePersistable {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "status_code" ,nullable = true)
    private String statusCode;

   @Column(name = "role_code" ,nullable = true)
    private String roleCode;

   @Column(name = "signature_text" ,nullable = true)
    private String signatureText;

  @JoinColumn(name = "site_investigator_id", referencedColumnName = "id")
  @ManyToOne
   private SiteInvestigator siteInvestigator;

 @JoinColumn(name = "study_organization_id", referencedColumnName = "id")
 @ManyToOne
   private StudyOrganization studyOrganization;


 public Integer getId() {
     return id;
 }

 public void setId(Integer id) {
     this.id = id;
 }

 public String getStatusCode() {
     return statusCode;
 }

 public void setStatusCode(String statusCode) {
     this.statusCode = statusCode;
 }

 public String getRoleCode() {
     return roleCode;
 }

 public void setRoleCode(String roleCode) {
     this.roleCode = roleCode;
 }

 public String getSignatureText() {
     return signatureText;
 }

 public void setSignatureText(String signatureText) {
     this.signatureText = signatureText;
 }

 public SiteInvestigator getSiteInvestigator() {
     return siteInvestigator;
 }

 public void setSiteInvestigator(SiteInvestigator siteInvestigator) {
     this.siteInvestigator = siteInvestigator;
 }

 public StudyOrganization getStudyOrganization() {
     return studyOrganization;
 }

 public void setStudyOrganization(StudyOrganization studyOrganization) {
     this.studyOrganization = studyOrganization;
 }


 public boolean equals(Object o) {
     if (this == o) return true;
     if (!(o instanceof StudyInvestigator)) return false;

     StudyInvestigator that = (StudyInvestigator) o;

     if (id != null ? !id.equals(that.id) : that.id != null) return false;
     if (roleCode != null ? !roleCode.equals(that.roleCode) : that.roleCode != null) return false;
     if (signatureText != null ? !signatureText.equals(that.signatureText) : that.signatureText != null)
         return false;
     if (siteInvestigator != null ? !siteInvestigator.equals(that.siteInvestigator) : that.siteInvestigator != null)
         return false;
     if (statusCode != null ? !statusCode.equals(that.statusCode) : that.statusCode != null) return false;
     if (studyOrganization != null ? !studyOrganization.equals(that.studyOrganization) : that.studyOrganization != null)
         return false;

     return true;
 }

 public int hashCode() {
     int result;
     result = (id != null ? id.hashCode() : 0);
     result = 31 * result + (statusCode != null ? statusCode.hashCode() : 0);
     result = 31 * result + (roleCode != null ? roleCode.hashCode() : 0);
     result = 31 * result + (signatureText != null ? signatureText.hashCode() : 0);
     result = 31 * result + (siteInvestigator != null ? siteInvestigator.hashCode() : 0);
     result = 31 * result + (studyOrganization != null ? studyOrganization.hashCode() : 0);
     return result;
 }


}
