package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mehul
 * Date: 6/1/11
 */
public class UpdateMeddraSpanishLoader extends HibernateDaoSupport {

    MeddraLoaderRepository meddraLoaderRepository;

    private static final String LANGUAGE = "language";
    private static final String MEDDRA_CODE = "llt_code";
    private static final String MEDDRA_TERM = "llt_name";
    private static final String MEDDRA_PT_ID = "pt_code";
    private static final String CURRENCY = "llt_currency";

    public void updateMeddraTerms() throws IOException {
        CsvReader reader;
        ClassPathResource classPathResource = new ClassPathResource("llt_spanish.csv");
        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader.readHeaders();
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        Query query = session.createQuery(new String("select meddraCode from LowLevelTerm llt"));
        List<String> existingMeddra = query.list();
        List<String> updateTerms = new ArrayList<String>();
        while (reader.readRecord()) {
            String language = reader.get(LANGUAGE).trim();
            String meddraCode = reader.get(MEDDRA_CODE).trim();
            String meddraTerm = StringEscapeUtils.escapeSql(reader.get(MEDDRA_TERM).trim());
            if (existingMeddra.contains(meddraCode)) {
                updateTerms.add("update meddra_llt_vocab set meddra_term_spanish='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            }
        }

        meddraLoaderRepository.batchExecute(updateTerms);

        tx.commit();
        session.close();
    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }



}
