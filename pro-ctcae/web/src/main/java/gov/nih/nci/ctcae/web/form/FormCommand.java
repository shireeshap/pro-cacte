package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class FormCommand {

    private CRF crf;


    public FormCommand() {
        this.crf = new CRF();
    }



    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }
}
