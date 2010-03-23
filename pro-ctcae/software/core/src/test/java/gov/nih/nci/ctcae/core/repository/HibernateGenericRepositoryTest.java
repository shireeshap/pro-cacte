package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Persistable;
import org.springframework.orm.hibernate3.HibernateTemplate;

import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @since Dec 11, 2008
 */
public class HibernateGenericRepositoryTest extends AbstractTestCase {

	private GenericRepository genericRepository;
	private HibernateTemplate hibernateTemplate;
	private Integer id;
	private Organization organization;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		hibernateTemplate = registerMockFor(HibernateTemplate.class);
		genericRepository = new HibernateGenericRepository();
		((HibernateGenericRepository) genericRepository).setHibernateTemplate(hibernateTemplate);


		organization = new Organization();
	}

	public void testFindById() {

		expect(hibernateTemplate.get(Organization.class, 1)).andReturn(organization);
		replayMocks();
		Persistable persistable = genericRepository.findById(Organization.class, 1);
		verifyMocks();
		assertSame("must be same object", persistable, organization);

		assertNull("must return null", genericRepository.findById(Organization.class, id));
		id = 1;
		assertNull("must return null", genericRepository.findById(null, id));

	}

	public void testMustNotDeleteNonPersistedObject() {

		genericRepository.delete(organization);


	}

	public void testDelete() {

		organization.setId(2);
		expect(hibernateTemplate.get(Organization.class, 2)).andReturn(organization);

		hibernateTemplate.delete(organization);
		hibernateTemplate.flush();
		replayMocks();
		genericRepository.delete(organization);
		verifyMocks();


	}
}
