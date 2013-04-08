package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.core.io.ClassPathResource;

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
        ClassPathResource classPathResource = new ClassPathResource("MedDRA12_symtoms_ES_prelim_updated_08.04.2011.csv");
//        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
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
//             updateTerms.add(new String[]{meddraCode, meddraTerm});
            }
        }
//         meddraLoaderRepository.batchExecute(updateTerms, "update meddra_llt_vocab set meddra_term_spanish=? where meddra_llt_id=(select id from meddra_llt where meddra_code=?)", false);
        meddraLoaderRepository.batchExecute(updateTerms);

    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
