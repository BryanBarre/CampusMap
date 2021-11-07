package com.example.campusmap;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thuytrinh.android.collageviews.MultiTouchListener;

public class MapActivity extends AppCompatActivity {
    MapView map;
    //taille de map
    float height;
    float width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String QrCode = MainActivity.etage;

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        setContentView(R.layout.activity_map);

        final TextView resultat = findViewById(R.id.resultat);
        Button astarBtn = findViewById(R.id.astar);

        map = findViewById(R.id.map);

        findViewById(R.id.map).setOnTouchListener(new MultiTouchListener());

        switch (QrCode) {
            case "Rdc_Code1":
            case "Rdc_Code2":
            case "Rdc_Code3":
                map.setBackgroundResource(R.drawable.rdc);
                break;

            case "Etage1_Code1":
            case "Etage1_Code2":
            case "Etage1_Code3":
                map.setBackgroundResource(R.drawable.etage1);
                break;

            case "Etage2_Code1":
            case "Etage2_Code2":
            case "Etage2_Code3":
                map.setBackgroundResource(R.drawable.etage2);
                break;
        }

        astarBtn.setOnClickListener(view -> drawMap());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(this::drawDepart, 500);
    }


    @Override
    protected void onStop() {
        if (map.animationthread.getStatus() == AsyncTask.Status.RUNNING)
            map.animationthread.cancel(true);
        super.onStop();
    }

    public void drawMap(){
        String QrCode = MainActivity.etage;
        Spinner batiment = findViewById(R.id.batiment);
        Spinner etage = findViewById(R.id.etage);
        Spinner salle = findViewById(R.id.salle);

        switch (QrCode) {
            case "Rdc_Code1":
                initializePoint("RDC");
                map.setStart("depart_0.1");
                break;
            case "Rdc_Code2":
                initializePoint("RDC");
                map.setStart("depart_0.2");
                break;
            case "Rdc_Code3":
                initializePoint("RDC");
                map.setStart("depart_0.3");
                break;
            case "Etage1_Code1":
                initializePoint("ET1");
                map.setStart("depart_1.1");
                break;
            case "Etage1_Code2":
                initializePoint("ET1");
                map.setStart("depart_1.2");
                break;
            case "Etage1_Code3":
                initializePoint("ET1");
                map.setStart("depart_1.3");
                break;
            case "Etage2_Code1":
                initializePoint("ET2");
                map.setStart("depart_2.1");
                break;
            case "Etage2_Code2":
                initializePoint("ET2");
                map.setStart("depart_2.2");
                break;
            case "Etage2_Code3":
                initializePoint("ET2");
                map.setStart("depart_2.3");
                break;
        }
        String numSalle = (batiment.getSelectedItem().toString() + etage.getSelectedItem().toString() + salle.getSelectedItem().toString());
        map.setStop(numSalle);
        map.Astar();//trace le trajet

    }
    public void drawDepart(){
        String QrCode = MainActivity.etage;

        switch (QrCode) {
            case "Rdc_Code1":
                initializePoint("RDC");
                map.setStart("depart_0.1");
                break;
            case "Rdc_Code2":
                initializePoint("RDC");
                map.setStart("depart_0.2");
                break;
            case "Rdc_Code3":
                initializePoint("RDC");
                map.setStart("depart_0.3");
                break;
            case "Etage1_Code1":
                initializePoint("ET1");
                map.setStart("depart_1.1");
                break;
            case "Etage1_Code2":
                initializePoint("ET1");
                map.setStart("depart_1.2");
                break;
            case "Etage1_Code3":
                initializePoint("ET1");
                map.setStart("depart_1.3");
                break;
            case "Etage2_Code1":
                initializePoint("ET2");
                map.setStart("depart_2.1");
                break;
            case "Etage2_Code2":
                initializePoint("ET2");
                map.setStart("depart_2.2");
                break;
            case "Etage2_Code3":
                initializePoint("ET2");
                map.setStart("depart_2.3");
                break;
        }
        map.Astar();//trace le trajet

        //String text = map.Astar();
        //resultat.setText(text);
    }

    public void initializePoint(String etage) {
        float x, y;
        String name;
        height = map.getHeight();
        width = map.getWidth();
        Resources res = getResources();
        map.graph.clear();
        String[] links=new String[]{};
        String[] points=new String[]{};

        //récuperer points du RDC
        if (etage.equals("RDC")) {
            Log.d("---------", "");
        }
        //récuperer points de l'etage 1
        else if (etage.equals("ET1")) {
            points = res.getStringArray(R.array.et1);
            links = res.getStringArray(R.array.ET1link);

        }
        //récuperer points de l'etage 2
        else {
            Log.d("---------", "");
        }
//scrute le fichier xml et ajoute les points
        for (String point:points) {
            Log.d("---------", point);
            String[] split = point.split(",");
            name=split[0];
            x=Integer.parseInt(split[1])*(width/864);
            y=Integer.parseInt(split[2])*(height / 864);
            map.graph.addVertex(name, x, y);
            Log.d("---------", name+"/"+x+"\\"+y);
        }

//scrute le fichier xml et ajoute les liaisons entre points
  /*      for (int cpt = 0; cpt < link.length; cpt += 2) {
            //map.graph.addEdge(link[cpt], link[cpt + 1]);
        }*/
        for (String link:links){
            String[] split = link.split(",");
            Log.d("---------", "edge"+split[0]+","+split[1]);
            map.graph.addEdge(split[0], split[1]);
        }

    }


}

