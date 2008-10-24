package gov.nih.nci.ctcae.core.query;

/**
 * @author mehul
 */

public class ParticipantQuery extends AbstractQuery {

    private static String queryString = "SELECT p from Participant p order by p.id";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";
    
    private static String IDENTIFIER = "assignedIdentifier";


  public ParticipantQuery() {

        super(queryString);
        }

  public void filterByParticipantFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(p.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
  }

  public void filterByParticipantLastName(final String lastName) {
      String searchString = "%" + lastName.toLowerCase() + "%";
      andWhere("lower(p.lastName) LIKE :" + LAST_NAME);
      setParameter(LAST_NAME, searchString);
}
  public void filterByParticipantIdentifier(final String identifier) {
      String searchString = "%" + identifier.toLowerCase() + "%";
      andWhere("lower(p.assignedIdentifier) LIKE :" + IDENTIFIER);
      setParameter(IDENTIFIER, searchString);
}
}
