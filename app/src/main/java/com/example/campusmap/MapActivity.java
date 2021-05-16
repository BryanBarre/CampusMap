package com.example.campusmap;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thuytrinh.android.collageviews.MultiTouchListener;

public class MapActivity extends AppCompatActivity {
    MapView map;
    //taille de map
    float height;
    float width;
    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String QrCode = MainActivity.etage;

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        View constraintMap = findViewById(R.id.constraintMap);
        setContentView(R.layout.activity_map);

        Spinner batiment = (Spinner) findViewById(R.id.batiment);
        Spinner etage = (Spinner) findViewById(R.id.etage);
        Spinner salle = (Spinner) findViewById(R.id.salle);

        LinearLayout textLayout = findViewById(R.id.textlayout);

        final TextView resultat = (TextView) findViewById(R.id.resultat);
        Button astarBtn = (Button) findViewById(R.id.astar);

        map = (MapView) findViewById(R.id.map);

        findViewById(R.id.map).setOnTouchListener((View.OnTouchListener)
                new MultiTouchListener());

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

        astarBtn.setOnClickListener(view -> {
            drawMap();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                drawDepart();
            }
        }, 500);
    }


    @Override
    protected void onStop() {
        if (map.animationthread.getStatus() == AsyncTask.Status.RUNNING)
            map.animationthread.cancel(true);
        super.onStop();
    }

    public void drawMap(){
        String QrCode = MainActivity.etage;
        Spinner batiment = (Spinner) findViewById(R.id.batiment);
        Spinner etage = (Spinner) findViewById(R.id.etage);
        Spinner salle = (Spinner) findViewById(R.id.salle);

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
        String[] pointX, pointY, pointName, link;
        //récuperer points du RDC
        if (etage.equals("RDC")) {
            pointX = res.getStringArray(R.array.RDCpointsX);
            pointY = res.getStringArray(R.array.RDCpointsY);
            pointName = res.getStringArray(R.array.RDCname);
            link = res.getStringArray(R.array.RDClink);
        }
        //récuperer points de l'etage 1
        else if (etage.equals("ET1")) {
            pointX = res.getStringArray(R.array.ET1pointsX);
            pointY = res.getStringArray(R.array.ET1pointsY);
            pointName = res.getStringArray(R.array.ET1name);
            link = res.getStringArray(R.array.ET1link);

        }
        //récuperer points de l'etage 2
        else {
            pointX = res.getStringArray(R.array.ET2pointsX);
            pointY = res.getStringArray(R.array.ET2pointsY);
            pointName = res.getStringArray(R.array.ET2name);
            link = res.getStringArray(R.array.ET2link);
        }
//scrute le fichier xml et ajoute les points
        for (int cpt = 0; cpt < pointX.length; cpt++) {
            x = Float.parseFloat(pointX[cpt]) * (width / 864);
            y = Float.parseFloat(pointY[cpt]) * (height / 864);
            name = pointName[cpt];
            map.graph.addVertex(name, x, y);
        }
//scrute le fichier xml et ajoute les liaisons entre points
        for (int cpt = 0; cpt < link.length; cpt += 2) {
            map.graph.addEdge(link[cpt], link[cpt + 1]);
        }

    }

}

