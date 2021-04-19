package com.example.campusmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import com.example.campusmap.Graph.Edge;
import com.example.campusmap.Graph.Graph;
import com.example.campusmap.Graph.Vertex;


/**
 * Created by Bryan BARRE on 06/02/2021.
 */
public class MapView extends View{

    Graph graph=new Graph();
    Bitmap depart = BitmapFactory.decodeResource(getResources(), R.drawable.ufrmap);
    int buttonClicked=0; //1-start 2-stop 3-vertex 4-edge 5-edgeStart 6-edgeStop
    String counter = "sans nom";
    float start_x,start_y;
    float stop_x,stop_y;
    float testSize=20;
    Vertex edgeStart,edgeStop;//emplacement ou l'on stock depart et arrivé pour tracer un trait
    int edgeReady=0;//passe a 1 quand pret a calculer le trait (poids/taille)
    final int lesRayons=15;//taille du rayon des points
    final Paint paint = new Paint();
    MapView.async animationthread = new MapView.async();

    public MapView(Context context){
        super(context);
        init(null,0);
    }
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public MapView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    public void init(AttributeSet attrs,int defStyle){
        paint.setColor(Color.BLACK);
        //paint.setAntiAlias(true);
    }

    //onDraw est appelé au moment de dessiner les points (taille des noeuds , couleurs,etc)
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<graph.vertex.size();i++){
            paint.setStyle(Paint.Style.FILL);
            Vertex v = graph.vertex.get(i);
            if(v.x==start_x&&v.y==start_y){
                canvas.drawBitmap(depart, (float) v.x, (float) v.y, null); // 24 is the height of image
                paint.setColor(Color.RED);
            }
            else if(v.x==stop_x&&v.y==stop_y)
                paint.setColor(Color.GREEN);
            else
                paint.setColor(Color.TRANSPARENT);//si le point n'est ni celui de depart ni celui d'arriver alors on ne l'affiche pas
            //canvas.drawCircle((float) v.x, (float) v.y, lesRayons, paint);
            /*
            paint.setColor(Color.BLACK);
            paint.setTextSize(testSize);
            canvas.drawText("" + v.id, (float) v.x, (float) v.y, paint);
*/
            paint.setColor(Color.BLACK);
            for(int j=0;j<graph.vertex.get(i).edges.size();j++){
                Vertex v1=graph.vertex.get(i);
                Vertex v2=v1.edges.get(j).destination;
                if(v1.edges.get(j).isPath==1)
                    paint.setColor(Color.GREEN);//couleur du chemin a prendre
                else {
                    //paint.setColor(Color.BLACK);//couleur des autres chemins
                    paint.setColor(Color.TRANSPARENT);//couleur des autres chemins
                }
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8);
                canvas.drawLine((float)v1.x,(float) v1.y,(float) v2.x,(float) v2.y, paint);
                paint.setColor(Color.BLACK);
                float xmes = (float) (v1.x+v2.x)/2;
                float ymes = (float) (v1.y+v2.y)/2;
                paint.setTextSize(testSize);
                paint.setStrokeWidth(1);
                //canvas.drawText("" + String.format( "%.2f", v1.edges.get(j).weight ), xmes, ymes+40/*qe ms shkruajme mbi vize*/, paint);

            }
        }
    }

    public void setStart(String name){
        Vertex v = graph.getVname(name);
        start_x = (float)v.x;
        start_y = (float)v.y;
        //buttonClicked = 0;
        invalidate();
    }

    public void setStop(String name){
        try {
            Vertex v = graph.getVname(name);
            stop_x = (float) v.x;
            stop_y = (float) v.y;
            //buttonClicked = 0;
            invalidate();
        }catch (NullPointerException e){
            System.out.println("Salle introuvable");
        }
    }

    void setNodes(float x, float y){
        if(kontrollPike(x, y)) {
            graph.addVertex(counter, x, y);
            buttonClicked = 0;
            invalidate();
        }
    }

    void setEdgeStart(Vertex v){
        if(v!=null) {
            edgeStart = v;
        }
    }
    void setEdgeStop(Vertex v){
        if(v!=null) {
            edgeStop = v;
            edgeReady = 1;

            graph.addEdge(edgeStart, edgeStop, 1);
            edgeReady = 0;
            buttonClicked = 0;
            invalidate();
        }
    }


    //vérifier si le point x, y coupe un sommet
    public boolean kontrollPike(float x,float y){
        for(int i=0;i<graph.vertex.size();i++){
            Vertex v= graph.vertex.get(i);
            double d = Math.sqrt((x - v.x) * (x - v.x)+(y-v.y)*(y-v.y));//distance de deux centres
            if(d<2*lesRayons)
                return false;//les cercles se croisent
        }
        return true;

    }
    public Vertex getVertex(float x,float y){
        for(int i=0;i<graph.vertex.size();i++){
            Vertex v= graph.vertex.get(i);
            double d = Math.sqrt((x - v.x) * (x - v.x)+(y-v.y)*(y-v.y));//distance entre le centre du sommet et le point
            if(d<=lesRayons)
                return v;//c'est-à-dire que le point est dans le cercle
        }
        return null;

    }
    public Graph freeGraph(){
        //graphique sans notes précédentes alg
        for(int i=0;i<graph.vertex.size();i++){
            Vertex current = graph.vertex.get(i);
            current.parent=null;
            current.d_value= Double.POSITIVE_INFINITY;
            current.discovered=false;
            current.h_value=0;
            current.f_value=Double.POSITIVE_INFINITY;
            for(int j=0;j<current.edges.size();j++){
                current.edges.get(j).isPath=0;
            }
        }
        return graph;
    }

    public void Astar(){
        System.gc();
        if(graph.vertex.size()==0)
            return;
        graph=freeGraph();
        if(graph.getV(stop_x, stop_y)==null)
            return;
        String text=graph.Astar(graph.getV(start_x, start_y), graph.getV(stop_x, stop_y));
        animationthread = new MapView.async();
        animationthread.execute();
        invalidate();
    }

    public class async extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //set edges me isPath=1
            Vertex current = graph.getV(stop_x,stop_y);
            if(current.parent==null){
                //si aucun chemin
                return null;
            }
            while(current!=graph.getV(start_x,start_y)){
                if (isCancelled()) break;
                Vertex parent = current.parent;
                Edge e;
                for(int i=0;i<current.edges.size();i++){
                    Edge current_edge=current.edges.get(i);
                    if(current_edge.destination==parent) {
                        current_edge.isPath = 1;
                        //duhet bere dhe edge i anasjellte 1
                        for(int j=0;j<parent.edges.size();j++) {
                            if (parent.edges.get(j).destination == current) {
                                parent.edges.get(j).isPath = 1;
                            }
                        }
                    }
                }
                current=parent;
            }
            return null;
        }
    }
}
