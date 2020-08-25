package elitonlais.model;

import java.util.Objects;

public class Pair <T1 extends Comparable<T1>, T2 extends Comparable<T2> > implements Comparable<Pair<T1, T2>> {

    private T1 fi;
    private T2 se;

    public Pair(T1 fi, T2 se) {
        this.fi = fi;
        this.se = se;
    }

    public T1 getFi() {
        return fi;
    }

    public void setFi(T1 fi) {
        this.fi = fi;
    }

    public T2 getSe() {
        return se;
    }

    public void setSe(T2 se) {
        this.se = se;
    }

    public Pair<T2, T1> swap() {
        return new Pair<>(this.se, this.fi);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(fi, pair.fi) &&
                Objects.equals(se, pair.se);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fi, se);
    }

    @Override
    public int compareTo(Pair<T1, T2> p) {
        if (fi.equals(p.fi)) return se.compareTo(p.se);
        return fi.compareTo(p.fi);
    }

    @Override
    public String toString() {
        return "{" + fi + ", " + se + '}';
    }
}
