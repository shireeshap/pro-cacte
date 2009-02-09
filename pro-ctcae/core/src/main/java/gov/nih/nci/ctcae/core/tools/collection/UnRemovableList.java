package gov.nih.nci.ctcae.core.tools.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class UnRemovableList<E> extends ArrayList<E> {

    @Override
    public E remove(int i) {
        throw new UnsupportedOperationException("Remove method is not supported");


    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Remove method is not supported");


    }

    @Override
    protected void removeRange(int i, int i1) {
        throw new UnsupportedOperationException("Remove method is not supported");
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Remove method is not supported");

    }


}
