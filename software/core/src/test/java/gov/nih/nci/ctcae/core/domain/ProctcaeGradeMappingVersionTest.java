package gov.nih.nci.ctcae.core.domain;


import junit.framework.TestCase;

public class ProctcaeGradeMappingVersionTest extends TestCase{
	private ProctcaeGradeMappingVersion proctcaeGradeMappingVersion;
	private static Integer ID = 1;
	private static String VERSION = "version";
	private static String NAME = "name";
	
	public void testGettersAndSetters(){
		proctcaeGradeMappingVersion = new ProctcaeGradeMappingVersion();
		proctcaeGradeMappingVersion.setId(ID);
		proctcaeGradeMappingVersion.setVersion(VERSION);
		proctcaeGradeMappingVersion.setName(NAME);
		
		assertEquals(ID, proctcaeGradeMappingVersion.getId());
		assertEquals(VERSION, proctcaeGradeMappingVersion.getVersion());
		assertEquals(NAME, proctcaeGradeMappingVersion.getName());
	}
	
	public void testEqualsAndHashCode(){
		proctcaeGradeMappingVersion = new ProctcaeGradeMappingVersion();
		proctcaeGradeMappingVersion.setId(ID);
		proctcaeGradeMappingVersion.setVersion(VERSION);
		proctcaeGradeMappingVersion.setName(NAME);
		
		ProctcaeGradeMappingVersion proGradeMappingVersion = new ProctcaeGradeMappingVersion();
		proGradeMappingVersion.setId(ID);
		proGradeMappingVersion.setVersion(VERSION);
		
		assertFalse(proGradeMappingVersion.equals(proctcaeGradeMappingVersion));
		assertFalse(proGradeMappingVersion.hashCode() == proctcaeGradeMappingVersion.hashCode());
		
		proGradeMappingVersion.setName(NAME);
		assertTrue(proGradeMappingVersion.equals(proctcaeGradeMappingVersion));
		assertTrue(proGradeMappingVersion.hashCode() == proctcaeGradeMappingVersion.hashCode());
	}

}
