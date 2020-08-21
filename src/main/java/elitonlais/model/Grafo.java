package elitonlais.model;

import java.util.*;

public class Grafo {

    private int size = 0;
    private final Set<String> nodes;
    private final Map<Pair<String, String>, Set<String>> edges;
    private final Map<String, Map<String, String>> adj;

    public Grafo() {
        nodes = new TreeSet<>();
        adj = new TreeMap<>();
        edges = new TreeMap<>();
    }

    public void addNode(String s) {
        size++;
        nodes.add(s);
        adj.put(s, new TreeMap<>());
    }

    public void addNode(List<String> list) {
        list.forEach(this::addNode);
    }

    public void addNode(int n) {
        for (int i = 0; i < n; i++) addNode("Q" + i);
    }

    public void addEdge(String a, String b, String v) {
        addDirEdge(a, b, v);
        addDirEdge(b, a, v);
    }

    public void addDirEdge(String a, String b, String v) {
        if (!containNode(a)) addNode(a);
        if (!containNode(b)) addNode(b);

        Pair<String, String> p = new Pair<>(a, b);
        if (!edges.containsKey(p)) edges.put(p, new TreeSet<>());
        edges.get(p).add(v);
        adj.get(a).put(v, b);
    }

    public Set<String> getEdge(String a, String b) {
        return getEdge(new Pair<>(a, b));
    }

    public Set<String> getEdge(Pair<String, String> p) {
        return edges.getOrDefault(p, null);
    }

    public String getNode(String node, String v) {
        if (containTransition(node, v)) return adj.get(node).get(v);
        else return null;
    }

    public boolean containNode(String node) {
        return nodes.contains(node);
    }

    public boolean containTransition(String node, String v) {
        return containNode(node) && adj.get(node).containsKey(v);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String a : adj.keySet()) {
            s.append(a).append(": ");
            for (String v : adj.get(a).keySet()) {
                s.append("{").append(adj.get(a).get(v)).append(", ").append(v).append("}");
            }
            s.append('\n');
        }
        return "Grafo {N=" + size + '\n' + s + '}';
    }

    public int getSize() {
        return size;
    }

    public Set<String> getNodes() {
        return new TreeSet<>(nodes);
    }

    public Map<Pair<String, String>, Set<String>> getEdges() {
        return new TreeMap<>(edges);
    }
}
