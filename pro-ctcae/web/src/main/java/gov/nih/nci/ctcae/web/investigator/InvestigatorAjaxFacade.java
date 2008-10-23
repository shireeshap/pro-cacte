package gov.nih.nci.ctcae.web.investigator;

import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.query.InvestigatorQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Mehul Gulati
 * Date: Oct 22, 2008
 */
public class InvestigatorAjaxFacade {
    private InvestigatorRepository investigatorRepository;

   public String searchInvestigator(Map parameterMap, String firstName, String lastName, String nciIdentifier, HttpServletRequest request) {

        List<Investigator> investigators = getObjects(firstName, lastName, nciIdentifier);
        InvestigatorTableModel investigatorTableModel = new InvestigatorTableModel();
        String table = investigatorTableModel.buildInvestigatorTable(parameterMap, investigators, request);
        return table;
       

    }

    private List<Investigator> getObjects(String firstName, String lastName, String nciIdentifier) {
        InvestigatorQuery investigatorQuery = new InvestigatorQuery();

        if (firstName != null && !"".equals(firstName)){
            investigatorQuery.filterByInvestigatorFirstName(firstName);
             }
        if (lastName != null && !"".equals(lastName)){
            investigatorQuery.filterByInvestigatorLastName(lastName);
             }
        if (nciIdentifier != null && !"".equals(nciIdentifier)){
            investigatorQuery.filterByNciIdentifier(nciIdentifier);
        }
        List<Investigator> investigators = (List<Investigator>) investigatorRepository.find(investigatorQuery);
        return investigators;
    }

    @Required
    public void setInvestigatorRepository(InvestigatorRepository investigatorRepository) {
        this.investigatorRepository = investigatorRepository;
    }

}