package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mehul
 * Date: 6/1/11
 */
public class UpdateMeddraSpanishLoader {

    MeddraLoaderRepository meddraLoaderRepository;
    GenericRepository genericRepository;

    private static final String LANGUAGE = "language";
    private static final String MEDDRA_CODE = "llt_code";
    private static final String MEDDRA_TERM = "llt_name";
    private static final String MEDDRA_PT_ID = "pt_code";
    private static final String CURRENCY = "llt_currency";

    public void updateMeddraTerms() throws Exception {
        CsvReader reader;
        ClassPathResource classPathResource = new ClassPathResource("MedDRA12_symptoms_ES_prelim.csv");
//        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF8"));
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
