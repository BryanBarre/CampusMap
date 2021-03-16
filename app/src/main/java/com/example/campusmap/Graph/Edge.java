package com.example.campusmap.Graph;

public class Edge {
    public final Vertex source;
    public final Vertex destination;
    public final double weight;
    public int isPath=0;//ky variable do perdoret tek sparseView, nqs eshte 1 ky edge do te jete i verdhe ndryshe i zi
    public Edge(Vertex source,Vertex destination,double weight){
        this.source=source;
        this.destination=destination;
        this.weight=weight;
    }
}

