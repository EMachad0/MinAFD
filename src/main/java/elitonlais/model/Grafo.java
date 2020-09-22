package elitonlais.model;

import elitonlais.controller.StringSizeFirstComparator;

import java.util.*;

public class Grafo {

    private int size = 0;
    private final Set<String> nodes;
    private final Map<String, Set<Aresta>> adj;

    public Grafo() {
        nodes = new TreeSet<>(new StringSizeFirstComparator());
        adj = new TreeMap<>();
    }

    public void addNode(String s) {
        size++;
        nodes.add(s);
        adj.put(s, new TreeSet<>());
    }

    public void addNode(List<String> list) {
        list.forEach(this::addNode);
    }

    public void removeNode(String n) {
        size--;
        nodes.remove(n);
        adj.remove(n);
    }

    public void addDirEdge(Aresta edge) {
        adj.get(edge.getA()).add(edge);
    }

    public List<Aresta> getEdge(String a, String b) {
        if (!containNode(a)) return null;
        List<Aresta> list = new ArrayList<>();
        for (Aresta ar : adj.get(a)) {
            if (b.equals(ar.getB())) list.add(ar);
        }
        return list;
    }

    public List<Aresta> getEdge(Pair<String, String> p) {
        return getEdge(p.getFi(), p.getSe());
    }

    public String getNode(String node, char fi, char se, String th) {
        if (!containNode(node)) return null;
        for (Aresta a : adj.get(node)) {
            if (a.compareValor(fi, se, th)) return a.getB();
        }
        return null;
    }

    public boolean containNode(String node) {
        return nodes.contains(node);
    }

    public boolean containTransition(String node, char fi, char se, String th) {
        if (!containNode(node)) return false;
        for (Aresta a : adj.get(node)) {
            if (a.compareValor(fi, se, th)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String a : adj.keySet()) {
            s.append(a).append(": ");
            for (Aresta ar : adj.get(a)) {
                s.append(ar);
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

    public List<Aresta> getEdges() {
        List<Aresta> list = new ArrayList<>();
        adj.forEach((k, v) -> list.addAll(v));
        return list;
    }

    public Map<String, Set<Aresta>> getAdj() {
        return adj;
    }
}
