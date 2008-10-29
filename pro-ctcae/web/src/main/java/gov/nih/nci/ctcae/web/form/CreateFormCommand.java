package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;

import java.io.Serializable;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable{

    private CRF crf;


    public CreateFormCommand() {
        this.crf = new CRF();
    }


    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }
}
