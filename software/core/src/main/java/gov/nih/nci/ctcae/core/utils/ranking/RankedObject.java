package gov.nih.nci.ctcae.core.utils.ranking;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankedObject<T> {
    private int rank = 0;
    private T obj;

    public RankedObject(T obj) {
        this.obj = obj;
    }

    public T getObject() {
        return obj;
    }

    public void setObject(T obj) {
        this.obj = obj;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void addToRank(int x){
        if(this.rank <= 0) this.rank+=x;
    }

    public void substractFromRank(int x){
        this.rank-=x;
    }

    @Override
    public String toString() {
        return "[" + rank + " : " + String.valueOf(obj)+ "]";
    }
}
