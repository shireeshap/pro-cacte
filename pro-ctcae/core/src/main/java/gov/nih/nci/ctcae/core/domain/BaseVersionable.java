package gov.nih.nci.ctcae.core.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for all the persistable entities
 *
 * @author , Created on July, 20th, 2007
 */
@MappedSuperclass
public abstract class BaseVersionable extends BasePersistable {

    @Version
    private Integer version = 0;

    ///CLOVER:OFF
    public Integer getVersion() {
        return version;
    }
    ///CLOVER:ON


}