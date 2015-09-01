package gov.nih.nci.ctcae.web.meddra.matcher.impl;

import gov.nih.nci.ctcae.web.meddra.matcher.Matcher;
import gov.nih.nci.ctcae.web.storage.InMemoryStorageKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by royzuniga on 8/20/15.
 */
public class LevenshteinMatcher implements Matcher {


    private Log logger = LogFactory.getLog(LevenshteinMatcher.class);
    private Integer threshold;
    private Double multipleWordMultiplier;

    @Override
    public List<String> getMatchesFromStorageWithThreshold(String input, InMemoryStorageKey inMemoryStorageKey) {
        //TODO: implement
        return null;
    }


    @Required
    public void setMultipleWordMultiplier(Double multipleWordMultiplier) {
        this.multipleWordMultiplier = multipleWordMultiplier;
    }

    @Required
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}
