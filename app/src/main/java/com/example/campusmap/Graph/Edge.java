package com.example.campusmap.Graph;

public class Edge {
    public final Vertex source;
    public final Vertex destination;
    public final double weight;
    public int isPath=0;//cette variable sera utilisée dans sparseView, si elle vaut 1 ce bord sera jaune sinon noir
    public Edge(Vertex source,Vertex destination,double weight){
        this.source=source;
        this.destination=destination;
        this.weight=weight;
    }
}

