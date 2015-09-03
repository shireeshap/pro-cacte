package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.csvreader.CsvReader;

/**
 * @author mehul
 * Date: 6/1/11
 */
public class UpdateMeddraSpanishLoader {

    MeddraLoaderRepository meddraLoaderRepository;
    GenericRepository genericRepository;

    private static final String MEDDRA_CODE = "llt_code";
    private static final String MEDDRA_TERM = "llt_name";

    public void updateMeddraTerms() throws Exception {
        CsvReader reader;
       /* Resource resource = new FileSystemResource("web/src/main/resources/");
    	Resource resource1 = resource.createRelative("MedDRA12_symtoms_EN_prelim_updated_08.04.2011.csv");
        File f = new File(resource1.getFile().getCanonicalPath());
        System.out.println(f.getCanonicalPath());
        reader = new CsvReader(new FileInputStream(f), Charset.forName("ISO-8859-1"));*/
        ClassPathResource classPathResource = new ClassPathResource("MedDRA12_symtoms_ES_prelim_updated_08.04.2011.csv");
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF-8"));
        reader.readHeaders();

        MeddraQuery meddraQuery = new MeddraQuery(true, "es");
        List existingMeddraCodes = genericRepository.find(meddraQuery);
        List<String> existingMeddra = (List<String>) existingMeddraCodes;
        List<String> updateTerms = new ArrayList<String>();
        while (reader.readRecord()) {
            String meddraCode = reader.get(MEDDRA_CODE).trim();
            String meddraTerm = StringEscapeUtils.escapeSql(reader.get(MEDDRA_TERM).trim());
            if (existingMeddra.contains(meddraCode)) {
                updateTerms.add("update meddra_llt_vocab set meddra_term_spanish='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            }
        }
        meddraLoaderRepository.batchExecute(updateTerms);
        reader.close();

    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
