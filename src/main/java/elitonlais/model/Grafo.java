package elitonlais.model;

import elitonlais.controller.StringSizeFirstComparator;

import java.util.*;

public class Grafo {

    private int size = 0;
    private final Set<String> nodes;
    private final Map<Pair<String, String>, Set<Character>> edges;
    private final Map<String, Map<Character, String>> adj;

    public Grafo() {
        nodes = new TreeSet<>(new StringSizeFirstComparator());
        adj = new TreeMap<>(new StringSizeFirstComparator());
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

    public void removeNode(String n) {
        for (String node : nodes) {
            edges.remove(new Pair<>(n, node));
            edges.remove(new Pair<>(node, n));
        }
        nodes.remove(n);
        adj.remove(n);
    }

    public void addEdge(String a, String b, Character v) {
        addDirEdge(a, b, v);
        addDirEdge(b, a, v);
    }

    public void addDirEdge(String a, String b, Character v) {
        if (!containNode(a)) addNode(a);
        if (!containNode(b)) addNode(b);

        Pair<String, String> p = new Pair<>(a, b);
        if (!edges.containsKey(p)) edges.put(p, new TreeSet<>());
        edges.get(p).add(v);
        adj.get(a).put(v, b);
    }

    public Set<Character> getEdge(String a, String b) {
        return getEdge(new Pair<>(a, b));
    }

    public Set<Character> getEdge(Pair<String, String> p) {
        return edges.getOrDefault(p, null);
    }

    public String getNode(String node, Character v) {
        if (containTransition(node, v)) return adj.get(node).get(v);
        else return null;
    }

    public boolean containNode(String node) {
        return nodes.contains(node);
    }

    public boolean containTransition(String node, Character v) {
        return containNode(node) && adj.get(node).containsKey(v);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String a : adj.keySet()) {
            s.append(a).append(": ");
            for (Character v : adj.get(a).keySet()) {
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
        return nodes;
    }

    public Map<Pair<String, String>, Set<Character>> getEdges() {
        return edges;
    }

    public Map<String, Map<Character, String>> getAdj() {
        return adj;
    }
}
