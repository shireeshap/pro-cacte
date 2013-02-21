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
        Resource resource = new FileSystemResource("web/src/main/resources/");
    	Resource resource1 = resource.createRelative("MedDRA12_symtoms_EN_prelim_updated_08.04.2011.csv");
        File f = new File(resource1.getFile().getCanonicalPath());
        System.out.println(f.getCanonicalPath());
        reader = new CsvReader(new FileInputStream(f), Charset.forName("ISO-8859-1"));

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

                updateTerms.add("update meddra_llt set meddra_code='"+meddraCode+"', currency='" + currency + "', meddra_pt_id='" + meddraPtId + "', participant_added='FALSE' where meddra_code='" + meddraCode + "'");
                updateTerms.add("update meddra_llt_vocab set meddra_term_english='" + meddraTerm + "' where meddra_llt_id=(select id from meddra_llt where meddra_code='" + meddraCode + "')");
            } else {
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

    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
