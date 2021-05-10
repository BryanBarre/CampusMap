package com.example.campusmap.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {

    public final ArrayList<Vertex> vertex= new ArrayList<>();

    public void addVertex(String id,double x,double y){
        Vertex v1 = new Vertex(id,x,y);
        vertex.add(v1);
    }

    public void addEdge(String s, String s1) {
        Vertex source=getVname(s);
        Vertex destination=getVname(s1);
        int weight=1;
        source.edges.add(new Edge(source,destination,weight));
        destination.edges.add(new Edge(destination,source,weight));
    }

    public Vertex getV(double x,double y){
        for(int i=0;i<vertex.size();i++)
            if(vertex.get(i).x==x&&vertex.get(i).y==y)
                return vertex.get(i);
        //sinon retourne null
        return null;
    }

    public Vertex getVname(String name){
        for(int i=0;i<vertex.size();i++) {
            //System.out.println("vertex.get(i)= "+vertex.get(i).id+" name= "+name);
            if (vertex.get(i).id.equals(name)) {
                return vertex.get(i);
            }
        }
        return null;
    }


    public String Astar(Vertex start,Vertex destination){
        String text="Alg: A*(A star) ";
        //Initialization
        long startTime = System.nanoTime();
        LinkedList<Vertex> CLOSED = new LinkedList<>();
        astarcomparator comparator = new astarcomparator();
        PriorityQueue<Vertex> OPEN = new PriorityQueue<>(1, comparator);
        start.d_value=0;
        start.f_value=0;
        OPEN.add(start);


        //cycle until queue is empty or destination has been inserted into s
        while(!OPEN.isEmpty()){

            Vertex extracted = OPEN.poll();
            Objects.requireNonNull(extracted).discovered=true;//lorsqu'il est en CLOSED et que f_value est déjà spécifié
            CLOSED.add(extracted);
            if(extracted==destination){
                break;
            }

            for(int i=0;i<extracted.edges.size();i++){
                Edge edge = extracted.edges.get(i);
                Vertex neighbor = edge.destination;
                if(!neighbor.discovered){
                    heuristic(neighbor,destination);
                    if(neighbor.f_value>extracted.f_value+edge.weight){
                        neighbor.d_value=extracted.d_value+edge.weight;
                        heuristic(neighbor,destination);
                        neighbor.f_value=neighbor.d_value+neighbor.h_value;
                        neighbor.parent=extracted;
                        OPEN.remove(neighbor);
                        OPEN.add(neighbor);
                    }
                }

            }
        }
        long stopTime = System.nanoTime();

        if(destination.parent==null)
            text="This path does not exist";
        else{
            text+=" Vertex ne CLOSED: "+CLOSED.size();
            System.out.println();

            //Astar process finished, now we will take our path and print it
            Stack<Vertex> stack = new Stack<>();
            Vertex current = destination;
            while(current!=null){
                stack.push(current);
                current = current.parent;
            }
            int hops = stack.size() - 1;
            double path_length = destination.d_value;
            text+=" Nr.Hops:"+hops+" Path length: "+String.format( "%.2f", path_length )+" Time: "+(stopTime-startTime)+" ns";
        }
        return text;
    }
    public void heuristic(Vertex v,Vertex destination){
        //distance euclidienne
        v.h_value=Math.sqrt((v.x-destination.x)*(v.x-destination.x)+
                (v.y-destination.y)*(v.y-destination.y));
    }

    public void clear(){
        vertex.clear();
    }

}

