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
 *         Date: 5/24/11
 */
public class UpdateMeddraLoader  {

    MeddraLoaderRepository meddraLoaderRepository;
    GenericRepository genericRepository;

    private static final String LANGUAGE = "language";
    private static final String MEDDRA_CODE = "llt_code";
    private static final String MEDDRA_TERM = "llt_name";
    private static final String MEDDRA_PT_ID = "pt_code";
    private static final String CURRENCY = "llt_currency";

    public void updateMeddraTerms() throws Exception {
        CsvReader reader;
        ClassPathResource classPathResource = new ClassPathResource("MedDRA12_symtoms_EN_prelim_updated_08.04.2011.csv");
//        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF8"));
        reader.readHeaders();

        MeddraQuery meddraQuery = new MeddraQuery(true, "es");
        List existingMeddraCodes = genericRepository.find(meddraQuery);
        List<String> existingMeddra = (List<String>) existingMeddraCodes;
        List<String> csvMeddraCodes = new ArrayList<String>();
        List<String> updateTerms = new ArrayList<String>();
        List<String> insertTerms = new ArrayList<String>();
        List<String> updateExistingMeddra = new ArrayList<String>();
        String notCurrent = "N";
        int i = 0;
        while (reader.readRecord()) {

            String meddraCode = reader.get(MEDDRA_CODE).trim();
            String meddraPtId = reader.get(MEDDRA_PT_ID).trim();
            String meddraTerm = StringEscapeUtils.escapeSql(reader.get(MEDDRA_TERM).trim());
            String currency = reader.get(CURRENCY).trim();

            if (existingMeddra.contains(meddraCode)) {

//                updateTerms.add(new String[]{meddraCode, meddraTerm, meddraPtId, currency});
                updateTerms.add("update meddra_llt set meddra_code='"+meddraCode+"', currency='" + currency + "', meddra_pt_id='" + meddraPtId + "', participant_added='FALSE' where meddra_code='" + meddraCode + "'");
                updateTerms.add("update meddra_llt_vocab set meddra_term_english='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            } else {
//                insertTerms.add(new String[]{meddraCode, meddraTerm, meddraPtId, currency});
                insertTerms.add("insert into meddra_llt (meddra_code, currency, meddra_pt_id, participant_added) values ('" + meddraCode + "','" + currency + "','" + meddraPtId + "','FALSE')");
                insertTerms.add("insert into meddra_llt_vocab (meddra_llt_id, meddra_term_english) values ((select meddra_llt.id from meddra_llt where meddra_llt.meddra_code='" + meddraCode + "'),'"+meddraTerm+"')");
            }
            csvMeddraCodes.add(meddraCode);
        }

        for (String currentMeddra : existingMeddra) {
            if (currentMeddra != null) {
                currentMeddra.trim();
            if (!csvMeddraCodes.contains(currentMeddra)) {
                updateExistingMeddra.add("update meddra_llt set currency='"+notCurrent+"' where meddra_code='" + currentMeddra +"'");
            }
        }
        }
        meddraLoaderRepository.batchExecute(updateTerms);
        meddraLoaderRepository.batchExecute(insertTerms);
        meddraLoaderRepository.batchExecute(updateExistingMeddra);
//        meddraLoaderRepository.batchExecute(updateTerms, "update meddra_llt set meddra_code=?, currency=?, meddra_pt_id=?, participant_added='FALSE' where meddra_code=?", true);
//        meddraLoaderRepository.batchExecute(updateTerms, "update meddra_llt_vocab set meddra_term_english=? where meddra_llt_id=(select id from meddra_llt where meddra_code=?)", false);
//        meddraLoaderRepository.batchExecute(insertTerms, "insert into meddra_llt (meddra_code, currency, meddra_pt_id, participant_added) values (?,?,?,'FALSE')", true);
//        meddraLoaderRepository.batchExecute(insertTerms, "insert into meddra_llt_vocab (meddra_term_english, meddra_llt_id) values (?, (select meddra_llt.id from meddra_llt where meddra_llt.meddra_code=?))", false);

    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}