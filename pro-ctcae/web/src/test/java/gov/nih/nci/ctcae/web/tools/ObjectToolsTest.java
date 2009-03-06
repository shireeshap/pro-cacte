package gov.nih.nci.ctcae.web.tools;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */

public class ObjectToolsTest extends TestCase {

    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;
    protected OrganizationClinicalStaff organizationClinicalStaff;
    protected ClinicalStaff clinicalStaff;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setStudyOrganization(new StudySite());
        studyOrganizationClinicalStaff.setRole(Role.NURSE);

        organizationClinicalStaff = new OrganizationClinicalStaff();
        clinicalStaff = Fixture.createClinicalStaff("Brian", "Brian", "123");
        organizationClinicalStaff.setClinicalStaff(clinicalStaff);

        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);
        organizationClinicalStaff.getStudyOrganizationClinicalStaffs().add(studyOrganizationClinicalStaff);

    }

    public void testBuildReduced() throws Exception {
        Bean src = new Bean("A", 1, 14L, (byte) 2);
        Bean reduced = ObjectTools.reduce(src, "string", "primitiveLong");

        assertBean(src.getString(), null, src.getPrimitiveLong(), (byte) 0, reduced);
    }

    public void testReduceAllForOrganizationClinicalStaff() throws Exception {
        List<OrganizationClinicalStaff> organizationClinicalStaffList = new ArrayList<OrganizationClinicalStaff>();
        organizationClinicalStaffList.add(organizationClinicalStaff);

        List<OrganizationClinicalStaff> reduced = ObjectTools.reduceAll(organizationClinicalStaffList, "id", "displayName");

        assertEquals("Wrong number of beans copied", 1, reduced.size());

        for (OrganizationClinicalStaff reducedOrganizationClinicalStaff : reduced) {
            assertEquals(clinicalStaff.getDisplayName(), reducedOrganizationClinicalStaff.getDisplayName());
            assertNull("must not find any clinical staff", reducedOrganizationClinicalStaff.getClinicalStaff());
        }

    }

    public void testReduceAllForNestedBean() throws Exception {
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();
        studyOrganizationClinicalStaffs.add(studyOrganizationClinicalStaff);

        List<StudyOrganizationClinicalStaff> reduced = ObjectTools.reduceAll(studyOrganizationClinicalStaffs, "id", "displayName");

        assertEquals("Wrong number of beans copied", 1, reduced.size());

        for (StudyOrganizationClinicalStaff reducedStudyOrganizationClinicalStaff : reduced) {
            assertEquals(clinicalStaff.getDisplayName(), reducedStudyOrganizationClinicalStaff.getDisplayName());
            assertNull("must not find any study organization", reducedStudyOrganizationClinicalStaff.getStudyOrganization());
        }

    }

    public void testReduceAll() throws Exception {
        List<Bean> src = Arrays
                .asList(new Bean("A", 1, 2, (byte) 3), new Bean("E", 3, 6, (byte) 9));
        List<Bean> reduced = ObjectTools.reduceAll(src, "integer", "primitiveLong");
        assertEquals("Wrong number of beans copied", 2, reduced.size());
        assertBean(null, 1, 2, (byte) 0, reduced.get(0));
        assertBean(null, 3, 6, (byte) 0, reduced.get(1));
    }

    public void testReduceThrowsCtcAeSystemException() throws Exception {
        List src = Arrays
                .asList(1);
        try {
            ObjectTools.reduceAll(src, "invalid property");
            fail("Failed to instantiate java.lang.Integer");
        } catch (CtcAeSystemException e) {

        }
    }

    private static void assertBean(String expectedString, Integer expectedInteger,
                                   long expectedLong, byte expectedByte, Bean actual) {
        assertEquals("Wrong string", expectedString, actual.getString());
        assertEquals("Wrong integer", expectedInteger, actual.getInteger());
        assertEquals("Wrong long", expectedLong, actual.getPrimitiveLong());
        assertEquals("Wrong byte", expectedByte, actual.getPrimitiveByte());
    }

    private static class Bean {
        private String string;

        private Integer integer;

        private long primitiveLong;

        private byte primitiveByte;

        public Bean() {
        }

        public Bean(String string, Integer integer, long primitive, byte primitiveByte) {
            this.string = string;
            this.integer = integer;
            this.primitiveLong = primitive;
            this.primitiveByte = primitiveByte;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public long getPrimitiveLong() {
            return primitiveLong;
        }

        public void setPrimitiveLong(long primitiveLong) {
            this.primitiveLong = primitiveLong;
        }

        public byte getPrimitiveByte() {
            return primitiveByte;
        }

        public void setPrimitiveByte(byte primitiveByte) {
            this.primitiveByte = primitiveByte;
        }
    }
}

