package elitonlais.model;

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
    public int compareTo(Aresta a) {
        if (fi.equals(a.fi)) {
            if (se.equals(a.se)) {
                return th.compareTo(a.th);
            } else return se.compareTo(a.se);
        } else return fi.compareTo(a.fi);
    }

    @Override
    public String toString() {
        return "(" + fi + "," + se + "," + th + ")";
    }
}
