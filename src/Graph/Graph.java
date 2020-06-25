package Graph;
import Graph.Exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Graph<T> implements Serializable {
    private HashMap<T,ArrayList<Edge<T>>> adjList;

    public Graph(){
        adjList = new HashMap<>();
    }

    public ArrayList<Edge<T>> getEdgesOf(T node){
        return adjList.get(node);
    }

    public void addNode(T node){
        if(contains(node))
            return;
        adjList.put(node, new ArrayList<>());
    }

    public void addDirectedEdge(T src, T dst,double weight)throws VertexNotExistException {
        if(!contains(src) || !contains(dst))
            throw new VertexNotExistException();
        adjList.get(src).add(new Edge<>(src,dst, weight));
    }

    public void addUndirectedEdge(T src, T dst,double weight)throws VertexNotExistException {
        addDirectedEdge(src,dst,weight);
        addDirectedEdge(dst,src,weight);
    }

    public ArrayList<T> getAllNodes(){
        ArrayList<T> nodeList = new ArrayList<>();
        nodeList.addAll(adjList.keySet());
        return nodeList;
    }

    public ArrayList<Edge<T>> bfs(T src) throws VertexNotExistException{
        if(!contains(src))
            throw new VertexNotExistException();
        ArrayList<T> visited = new ArrayList<>();
        ArrayList<Edge<T>> bfsTree = new ArrayList<>();
        ArrayList<ArrayList<Edge<T>>> layers = new ArrayList<>();

        layers.add(adjList.get(src));
        while (!layers.isEmpty()){
            ArrayList<Edge<T>> currLayer = layers.remove(0);

            for (Edge edge: currLayer) {
                if(!visited.contains(edge.getDst())){
                    visited.add((T)edge.getSrc());
                    visited.add((T)edge.getDst());
                    bfsTree.add(edge);
                    layers.add(adjList.get(edge.getDst()));
                }
            }
        }
        return bfsTree;
    }

    public ArrayList<Edge<T>> dfs(T src)throws VertexNotExistException{
        if(!contains(src))
            throw new VertexNotExistException();
        ArrayList<T> visited = new ArrayList<>();
        ArrayList<Edge<T>> dfsTree = new ArrayList<>();
        Stack<Edge<T>> edgeStack = new Stack<>();
        for (Edge edge:adjList.get(src)) {
            edgeStack.push(edge);
        }

        while(!edgeStack.isEmpty()){
            Edge<T> e = edgeStack.pop();
            if(!visited.contains(e.getDst())){
                visited.add(e.getSrc());
                visited.add(e.getDst());
                dfsTree.add(e);
            }

            for (Edge edge:adjList.get(e.getDst())) {
                if(!visited.contains(edge.getDst()))
                    edgeStack.push(edge);
            }
        }
        return dfsTree;
    }

    public boolean contains(T node){
        return adjList.containsKey(node);
    }

}
