package pinganalyzer;

import java.io.Serializable;
import java.util.Objects;

/** Tuple.java
 * Generic class for returning multiple objects from functions.
 * @param <T1> type of the first object
 * @param <T2> type of the second object
 *
 * ChangeLog:
 *   04-Aug-2008: init version
 *
 * @author caha
 */
public class Tuple<T1 extends Comparable<? super T1>, T2 extends Comparable<? super T2>> implements Serializable, Comparable<Tuple<T1, T2>> {

    private static final long serialVersionUID = 1L;
    
    public T1 first;

    public T2 second;

    public Tuple() {
        this(null, null);
    }

    public Tuple(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tuple){
            Tuple tuple = (Tuple) obj;
            return equals(this.first, tuple.first) && equals(this.second, tuple.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.first);
        hash = 41 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public String toString() {
        return "Tuple{" + "first=" + first + ", second=" + second + '}';
    }
    
    public Object[] toArray(){
        return new Object[]{first,second};
    }    
    
    
    /**
     * 
     * @param a
     * @param b
     * @return a.equals(b) s vyresenim pripadu kdy a je null
     */
    public static boolean equals(Object a,Object b) {
        if (a==null && b==null){
            return true;
        } else if (a==null || b==null) {
            return false;
        } else {
            return a.equals(b);
        }
    }    

    public static <T extends Comparable<? super T>> int compare( T a, T b) {
        if (a==null && b==null)
            return 0;
        else if (a==null)
            return 1;
        else if (b==null)
            return -1;
        return a.compareTo(b);
    }    

    @Override
    public int compareTo(Tuple<T1, T2> o) {
        int c = compare(this.first, o.first);
        if(c!=0){
            return c;
        }
        return compare(this.second, o.second);
    }

    
    
}
