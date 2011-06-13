package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
        ClassPathResource classPathResource = new ClassPathResource("lt.csv");
        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader.readHeaders();

        MeddraQuery meddraQuery = new MeddraQuery(true, "es");
        List existingMeddraCodes = genericRepository.find(meddraQuery);
        List<String> existingMeddra = (List<String>) existingMeddraCodes;
        List<String[]> updateTerms = new ArrayList<String[]>();
        List<String[]> insertTerms = new ArrayList<String[]>();
        int i = 0;
        while (reader.readRecord()) {

            String meddraCode = reader.get(MEDDRA_CODE).trim();
            String meddraPtId = reader.get(MEDDRA_PT_ID).trim();
            String meddraTerm = StringEscapeUtils.escapeSql(reader.get(MEDDRA_TERM).trim());
            String currency = reader.get(CURRENCY).trim();

            if (existingMeddra.contains(meddraCode)) {

                updateTerms.add(new String[]{meddraCode, meddraTerm, meddraPtId, currency});
//                updateTerms.add("update meddra_llt set meddra_code='"+meddraCode+"', currency='" + currency + "', meddra_pt_id='" + meddraPtId + "', participant_added='FALSE' where meddra_code='" + meddraCode + "'");
//                updateTerms.add("update meddra_llt_vocab set meddra_term_english='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            } else {
                insertTerms.add(new String[]{meddraCode, meddraTerm, meddraPtId, currency});
//                insertTerms.add("insert into meddra_llt (meddra_code, currency, meddra_pt_id, participant_added) values ('" + meddraCode + "','" + currency + "','" + meddraPtId + "','FALSE')");
//                insertTerms.add("insert into meddra_llt_vocab (meddra_llt_id, meddra_term_english) values ((select meddra_llt.id from meddra_llt where meddra_llt.meddra_code='" + meddraCode + "'),'"+meddraTerm+"')");
            }
        }

        meddraLoaderRepository.batchExecute(updateTerms, "update meddra_llt set meddra_code=?, currency=?, meddra_pt_id=?, participant_added='FALSE' where meddra_code=?", true);
        meddraLoaderRepository.batchExecute(updateTerms, "update meddra_llt_vocab set meddra_term_english=? where meddra_llt_id=(select id from meddra_llt where meddra_code=?)", false);
        meddraLoaderRepository.batchExecute(insertTerms, "insert into meddra_llt (meddra_code, currency, meddra_pt_id, participant_added) values (?,?,?,'FALSE')", true);
        meddraLoaderRepository.batchExecute(insertTerms, "insert into meddra_llt_vocab (meddra_term_english, meddra_llt_id) values (?, (select meddra_llt.id from meddra_llt where meddra_llt.meddra_code=?))", false);

    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
