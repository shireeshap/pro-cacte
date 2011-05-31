package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
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
public class UpdateMeddraLoader extends HibernateDaoSupport {

    MeddraLoaderRepository meddraLoaderRepository;

    private static final String LANGUAGE = "language";
    private static final String MEDDRA_CODE = "llt_code";
    private static final String MEDDRA_TERM = "llt_name";
    private static final String MEDDRA_PT_ID = "pt_code";
    private static final String CURRENCY = "llt_currency";

    public void updateMeddraTerms() throws IOException {
        CsvReader reader;
        ClassPathResource classPathResource = new ClassPathResource("llt.csv");
        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader.readHeaders();
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        Query query = session.createQuery(new String("select meddraCode from LowLevelTerm llt"));
        List<String> existingMeddra = query.list();
        List<String> updateTerms = new ArrayList<String>();
        List<String> insertTerms = new ArrayList<String>();
        int i = 0;
        while (reader.readRecord()) {
            String language = reader.get(LANGUAGE).trim();
            String meddraCode = reader.get(MEDDRA_CODE).trim();
            Integer meddraPtId = Integer.parseInt(reader.get(MEDDRA_PT_ID).trim());
            String meddraTerm = StringEscapeUtils.escapeSql(reader.get(MEDDRA_TERM).trim());
            String currency = reader.get(CURRENCY).trim();
            if (existingMeddra.contains(meddraCode)) {
                updateTerms.add("update meddra_llt set meddra_term='" + meddraTerm + "', meddra_code='"+meddraCode+"', currency='" + currency + "', meddra_pt_id='" + meddraPtId + "', participant_added='FALSE' where meddra_code='" + meddraCode + "'");
                updateTerms.add("update meddra_llt_vocab set meddra_term_english='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            } else {
                insertTerms.add("insert into meddra_llt (meddra_code, meddra_term, currency, meddra_pt_id, participant_added) values ('" + meddraCode + "','" + meddraTerm + "','" + currency + "','" + meddraPtId + "','FALSE')");
                insertTerms.add("insert into meddra_llt_vocab (meddra_llt_id, meddra_term_english) values ((select meddra_llt.id from meddra_llt where meddra_llt.meddra_code='" + meddraCode + "'),'"+meddraTerm+"')");
            }
        }

        meddraLoaderRepository.batchExecute(updateTerms);
        meddraLoaderRepository.batchExecute(insertTerms);

        tx.commit();
        session.close();
    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

}
