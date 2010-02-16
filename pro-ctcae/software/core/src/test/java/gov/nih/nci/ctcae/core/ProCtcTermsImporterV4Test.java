package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.csv.loader.ProCtcTermsImporterV4;

import java.io.IOException;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class ProCtcTermsImporterV4Test extends AbstractTransactionalDataSourceSpringContextTests {

    public void testLoadProCtcTerms() {
        try {
            new ProCtcTermsImporterV4().loadProCtcTerms(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
    };

    //    @Override
    protected String[] getConfigLocations() {
        return context;
    }

}