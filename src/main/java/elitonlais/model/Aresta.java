package elitonlais.model;

import java.util.Objects;

public class Aresta implements Comparable<Aresta> {

    private String a;
    private String b;
    private Character fi;
    private Character se;
    private Character th;

    public Aresta(String a, String b, char fi, char se, char th) {
        this.a = a;
        this.b = b;
        this.fi = fi;
        this.se = se;
        this.th = th;
    }

    public boolean compareValor(char fi, char se, char th) {
        return this.fi == fi && this.se == se && this.th == th;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public char getFi() {
        return fi;
    }

    public char getSe() {
        return se;
    }

    public char getTh() {
        return th;
    }

    public void setA(String a) {
        this.a = a;
    }

    public void setB(String b) {
        this.b = b;
    }

    public void setFi(Character fi) {
        this.fi = fi;
    }

    public void setSe(Character se) {
        this.se = se;
    }

    public void setTh(Character th) {
        this.th = th;
    }

    @Override
    public int compareTo(Aresta ar) {
        if (a.equals(ar.getA())) {
            if (b.equals(ar.getB())) {
                if (fi.equals(ar.fi)) {
                    if (se.equals(ar.se)) {
                        return th.compareTo(ar.th);
                    } else return se.compareTo(ar.se);
                } else return fi.compareTo(ar.fi);
            } else return b.compareTo(ar.b);
        } else return a.compareTo(ar.a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aresta aresta = (Aresta) o;
        return Objects.equals(a, aresta.a) &&
                Objects.equals(b, aresta.b) &&
                Objects.equals(fi, aresta.fi) &&
                Objects.equals(se, aresta.se) &&
                Objects.equals(th, aresta.th);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, fi, se, th);
    }

    @Override
    public String toString() {
        return "(" + fi + "," + se + "," + th + ")";
    }
}
