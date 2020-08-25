package elitonlais.model;

import java.util.*;

public class Minimizador {

    private final AFD afd;

    private final List<String> passoTexto;
    private final List<Map<Pair<String, String>, String>> passoTabela;
    private final List<Map<Pair<String, String>, List<Pair<String, String>>>> passoListas;

    public Minimizador(AFD afd) {
        this.afd = afd;

        passoTexto = new ArrayList<>();
        passoTabela = new ArrayList<>();
        passoListas = new ArrayList<>();

        minimiza();
    }

    private void minimiza() {
        List<String> estados = new ArrayList<>(afd.getEstados());
        Set<String> finais = afd.getEstadosFinais();
        Set<Character> alfa = afd.getAlfabeto();
        Grafo grafo = afd.getGrafo();

        Map<Pair<String, String>, List<Pair<String, String>>> lista = new TreeMap<>();
        Map<Pair<String, String>, String> m = new TreeMap<>();

        // passo 0
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                String qi = estados.get(i);
                String qj = estados.get(j);

                boolean iFinal = finais.contains(qi);
                boolean jFinal = finais.contains(qj);
                if ((iFinal && !jFinal) || (!iFinal && jFinal)) {
                    m.put(new Pair<>(qi, qj), "X");
                } else {
                    m.put(new Pair<>(qi, qj), "");
                }
            }
        }

        passoTexto.add("Marcação dos estados do tipo:\n" +
                "{Estado final, Estado não final}");
        addPassoTabela(m);
        addPassoListas(lista);

        // Passo 1..N:
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                String qi = estados.get(i);
                String qj = estados.get(j);
                Pair<String, String> qiqj = new Pair<>(qi, qj);

                if (m.get(qiqj).equals("")) {
                    // Novo Passo: Analisando par nao marcado
                    String s = qiqj + ":\n";

                    for (Character c : alfa) {
                        String pi = grafo.getNode(qi, c);
                        String pj = grafo.getNode(qj, c);

                        if (pi.compareTo(pj) > 0) {
                            String aux = pi;
                            pi = pj;
                            pj = aux;
                        }
                        Pair<String, String> pipj = new Pair<>(pi, pj);

                        s += "Delta(" + qi + ", " + c + ") = " + pi + ";\n";
                        s += "Delta(" + qj + ", " + c + ") = " + pj + ";\n";

                        if (pi.equals(pj)) {
                            s += "Trivialmente equivalente\n";
                        } else if (m.get(pipj).equals("X")) {
                            s += "O par " + pipj + " é marcado\n";
                            s += "Logo " + qiqj + " é marcado\n";
                            m.put(qiqj, "X");

                            if (lista.containsKey(qiqj)) {
                                s += "Marcando lista de " + pipj + ":\n";
                                for (Pair<String, String> pair : lista.get(qiqj)) {
                                    m.put(pair, "X");
                                    s += "\t" + pair + " for marcado!";
                                }
                            }
                            break;
                        } else {
                            if (!lista.containsKey(pipj)) lista.put(pipj, new ArrayList<>());
                            if (!lista.get(pipj).contains(qiqj)) lista.get(pipj).add(qiqj);
                            s += "O par " + pipj + " é não marcado\n";
                            s += "Adicionando " + qiqj + " na lista de " + pipj + ";\n";
                        }
                        s += '\n';
                    }
                    addPassoTabela(m);
                    addPassoListas(lista);
                    passoTexto.add(s);
                }
            }
        }
        System.out.println("Terminou");
    }

    private void addPassoTabela(Map<Pair<String, String>, String> m) {
        int atual = passoTabela.size();
        passoTabela.add(new TreeMap<>());
        for (Pair<String, String> pair : m.keySet()) {
            Pair<String, String> np = new Pair<>(pair.getFi(), pair.getSe());
            passoTabela.get(atual).put(np, m.get(pair));
        }
    }

    private void addPassoListas(Map<Pair<String, String>, List<Pair<String, String>>> lista) {
        int atual = passoListas.size();
        passoListas.add(new TreeMap<>());
        for (Pair<String, String> pair : lista.keySet()) {
            Pair <String, String> np = new Pair<>(pair.getFi(), pair.getSe());
            passoListas.get(atual).put(np, new ArrayList<>());
            for (Pair<String, String> vals : lista.get(pair)) {
                Pair <String, String> nv = new Pair<>(vals.getFi(), vals.getSe());
                passoListas.get(atual).get(np).add(nv);
            }
        }
    }

    public String getPassoTexto(int i) {
        return passoTexto.get(i);
    }

    public Map<Pair<String, String>, String> getPassoTabela(int i) {
        return passoTabela.get(i);
    }

    public Map<Pair<String, String>, List<Pair<String, String>>> getPassoListas(int i) {
        return passoListas.get(i);
    }
}
