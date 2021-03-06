package gov.nih.nci.ctcae.core.domain.meddra;


import gov.nih.nci.cabig.ctms.domain.DomainObject;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.BasePersistable;
import gov.nih.nci.ctcae.core.domain.MeddraVersion;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;


@MappedSuperclass
public class AbstractMeddraDomainObject extends BasePersistable implements DomainObject {

    private Integer id;

    //moved into the lowLevelTermVocab class for LowLevelTerm.
//    private String meddraTerm;

    private String costartSymbol;

    private String hartsCode;

    private String whoArtCode;

    private String icd10Code;

    private String icd9Code;

    private String icd9CmCode;

    private String jartCode;

    private MeddraVersion meddraVersion;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "costart_symbol")
    public String getCostartSymbol() {
        return costartSymbol;
    }

    public void setCostartSymbol(String costartSymbol) {
        this.costartSymbol = costartSymbol;
    }

    @Column(name = "harts_code")
    public String getHartsCode() {
        return hartsCode;
    }

    public void setHartsCode(String hartsCode) {
        this.hartsCode = hartsCode;
    }

    @Column(name = "icd10_code")
    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    @Column(name = "icd9_cm_code")
    public String getIcd9CmCode() {
        return icd9CmCode;
    }

    public void setIcd9CmCode(String icd9CmCode) {
        this.icd9CmCode = icd9CmCode;
    }

    @Column(name = "icd9_code")
    public String getIcd9Code() {
        return icd9Code;
    }

    public void setIcd9Code(String icd9Code) {
        this.icd9Code = icd9Code;
    }

    @Column(name = "jart_code")
    public String getJartCode() {
        return jartCode;
    }

    public void setJartCode(String jartCode) {
        this.jartCode = jartCode;
    }


//    @Column(name = "meddra_term")
//    public String getMeddraTerm() {
//        return meddraTerm;
//    }
//
//    public void setMeddraTerm(String meddraTerm) {
//        this.meddraTerm = meddraTerm;
//    }

    @Column(name = "who_art_code")
    public String getWhoArtCode() {
        return whoArtCode;
    }

    public void setWhoArtCode(String whoArtCode) {
        this.whoArtCode = whoArtCode;
    }


    @ManyToOne
    @JoinColumn(name = "version_id")
    @Cascade(value = {CascadeType.LOCK})
    public MeddraVersion getMeddraVersion() {
        return meddraVersion;
    }

    public void setMeddraVersion(MeddraVersion meddraVersion) {
        this.meddraVersion = meddraVersion;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if(this instanceof LowLevelTerm){
        	String meddraTerm = ((LowLevelTerm)this).getMeddraTerm(SupportedLanguageEnum.ENGLISH);
    		result = prime * result + (meddraTerm == null ? 0 : meddraTerm.hashCode());
    	} else {
    		logger.error("hashCode() for any AbstractMeddraDomainObject other than LowLevelTerm is currently un-suppported");
    	}
        result = prime * result + (meddraVersion == null ? 0 : meddraVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof LowLevelTerm) ||  !(this instanceof LowLevelTerm)){
    		logger.error("equals() for any AbstractMeddraDomainObject other than LowLevelTerm is currently un-suppported");
    		return false;
    	}
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LowLevelTerm other = (LowLevelTerm) obj;
        String meddraTerm = ((LowLevelTerm)this).getMeddraTerm(SupportedLanguageEnum.ENGLISH);
        if (meddraTerm == null) {
            if (other.getMeddraTerm(SupportedLanguageEnum.ENGLISH) != null) {
                return false;
            }
        } else if (!meddraTerm.equals(other.getMeddraTerm(SupportedLanguageEnum.ENGLISH))) {
            return false;
        }
        if (meddraVersion == null) {
            if (other.getMeddraVersion() != null) {
                return false;
            }
        } else if (!meddraVersion.equals(other.getMeddraVersion())) {
            return false;
        }
        return true;
    }


}