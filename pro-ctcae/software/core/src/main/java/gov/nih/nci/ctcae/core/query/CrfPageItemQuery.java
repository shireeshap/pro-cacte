package gov.nih.nci.ctcae.core.query;

import java.util.List;

/**The CrfPageItem class.
 * @author AmeyS
 */
public class CrfPageItemQuery extends AbstractQuery{
	
	private static String query = "SELECT cpi from CrfPageItem cpi";
	private static String CRF_IDS = "ids";

	public CrfPageItemQuery() {
		super(query);
	}
	
	public void filterByCrfIds(List<Integer> crfIds){
		andWhere(" cpi.crfPage.crf.id in (:"+ CRF_IDS +")");
		setParameterList(CRF_IDS, crfIds);
	}
}
