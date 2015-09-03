package gov.nih.nci.ctcae.web.startup;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.web.storage.InMemoryStorage;
import gov.nih.nci.ctcae.web.storage.InMemoryStorageKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by royzuniga on 8/18/15.
 */

public class MeddtraTermsLoader implements InitializingBean {

    private MeddraRepository meddraRepository;
    private Log logger = LogFactory.getLog(MeddtraTermsLoader.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);

        InMemoryStorage.initiliazeInMemoryStorage();

        final MeddraQuery findAllQuery = new MeddraQuery();

        final List<LowLevelTerm> lowLevelTerms = (List<LowLevelTerm>) meddraRepository.find(findAllQuery);

        final Map<String, LowLevelTerm> englishCache = new HashMap<>();
        final Map<String, LowLevelTerm> spanishCache = new HashMap<>();

        for(final LowLevelTerm  term : lowLevelTerms) {
            final String english = term.getLowLevelTermVocab().getMeddraTermEnglish();
            final String spanish = term.getLowLevelTermVocab().getMeddraTermSpanish();

            englishCache.put(english, term);
            spanishCache.put(spanish, term);
        }

        InMemoryStorage.putIntoStorage(InMemoryStorageKey.EN_MEDDRA_TERMS, englishCache);
        InMemoryStorage.putIntoStorage(InMemoryStorageKey.SP_MEDDRA_TERMS, spanishCache);

    }

    @Required
    public void setMeddraRepository(MeddraRepository meddraRepository) {
        this.meddraRepository = meddraRepository;
    }
}
