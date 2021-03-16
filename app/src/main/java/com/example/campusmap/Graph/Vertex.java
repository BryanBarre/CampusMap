package com.example.campusmap.Graph;

import java.util.LinkedList;

public class  Vertex implements Comparable<Vertex>{
    public final String id;
    public Vertex parent=null;
    public double d_value=Double.POSITIVE_INFINITY;
    public final LinkedList<Edge> edges= new LinkedList<>();
    public boolean discovered = false;//discovered true dmth qe vertex eshte ne SET (Dijkstra) dhe ne CLOSED (per a*)
    //koordinata per grid
    public double x,y;
    //Vlera heuristike
    public double h_value=0;
    //Total weight
    public double f_value=Double.POSITIVE_INFINITY;//(f_value=d_value+h_value)

    public Vertex(String id){
        this.id=id;
    }
    public Vertex(String id,double x,double y){
        this.id=id;
        this.x=x;
        this.y=y;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(this.d_value,o.d_value);
    }




}

