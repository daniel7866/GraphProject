package Graph;

import java.io.Serializable;

public class Edge<T> implements Serializable {
    private T src,dst;
    private double weight;

    public Edge(T src, T dst, double weight) {
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Edge && ((Edge)obj).dst == dst && ((Edge)obj).src == src;
    }

    @Override
    public String toString() {
        return src.toString() + "-"+ weight +"->" + dst.toString();
    }

    public T getSrc() {
        return src;
    }

    public void setSrc(T src) {
        this.src = src;
    }

    public T getDst() {
        return dst;
    }

    public void setDst(T dst) {
        this.dst = dst;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
