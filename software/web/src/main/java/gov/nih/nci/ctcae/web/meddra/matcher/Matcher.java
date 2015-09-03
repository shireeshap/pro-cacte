package gov.nih.nci.ctcae.web.meddra.matcher;

import gov.nih.nci.ctcae.web.storage.InMemoryStorageKey;

import java.util.List;

/**
 * Created by royzuniga on 8/20/15.
 */
public interface Matcher {

    //TODO: Should this return a more complete obj for more efficient MedDRA term matching?
    List<String> getMatchesFromStorageWithThreshold(String input, InMemoryStorageKey inMemoryStorageKey);
}
