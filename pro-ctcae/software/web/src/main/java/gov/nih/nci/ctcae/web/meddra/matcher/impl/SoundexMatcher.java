package gov.nih.nci.ctcae.web.meddra.matcher.impl;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.web.meddra.matcher.Matcher;
import gov.nih.nci.ctcae.web.storage.InMemoryStorage;
import gov.nih.nci.ctcae.web.storage.InMemoryStorageKey;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by royzuniga on 8/19/15.
 */
public class SoundexMatcher implements Matcher {

    private Integer threshold;
    private Log logger = LogFactory.getLog(SoundexMatcher.class);

    private final Soundex soundex = new Soundex();


    @SuppressWarnings(value = "unchecked")
    public List<String> getMatchesFromStorageWithThreshold(String input, InMemoryStorageKey inMemoryStorageKey) {

        final List<String> exactMatches = new ArrayList<>();
        final List<String> closeMatches = new ArrayList<>();

        final List<String> returnList = new ArrayList<>();

        final Map<String, LowLevelTerm> storedItemsByKey = (Map<String, LowLevelTerm>) InMemoryStorage.getStoredItemsByKey(inMemoryStorageKey);


        input = cleanInput(input);

        //TODO: check DB for permission to add fuzzy extension in postgres for performance enhancement
        //TODO define and strip common words when applicable

        final String encodedInput = soundex.encode(input);
        for(final String key : storedItemsByKey.keySet()) {
            try {
                int matchScore = soundex.difference(encodedInput, key);
                if(matchScore > threshold) {
                    switch (matchScore) {
                        case 4:
                            exactMatches.add(key);
                            break;
                        default:
                            closeMatches.add(key);
                            break;
                    }
                }
            } catch (EncoderException e) {
             logger.error(String.format("Unable to encode %s", key), e);
            }
        }

        returnList.addAll(exactMatches);
        returnList.addAll(closeMatches);

        return returnList;
    }

    private String cleanInput(String input) {
        //TODO: clean input
        return input;
    }


    @Required
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

}
