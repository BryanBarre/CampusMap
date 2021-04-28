package com.example.campusmap;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
        View constraintMap = findViewById(R.id.constraintMap);
        setContentView(R.layout.activity_map);

        Spinner batiment = (Spinner) findViewById(R.id.batiment);
        Spinner etage = (Spinner) findViewById(R.id.etage);
        Spinner salle = (Spinner) findViewById(R.id.salle);


        final TextView resultat = (TextView) findViewById(R.id.resultat);
        Button astar = (Button) findViewById(R.id.astar);

        map = (MapView) findViewById(R.id.map);

        findViewById(R.id.map).setOnTouchListener((View.OnTouchListener) new MultiTouchListener());//permet de zoomer et deplacer la carte

        astar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (QrCode) {
                    case "Rdc_Code1":
                        map.setBackgroundResource(R.drawable.rdc);
                        initializePoint("RDC");
                        map.setStart("depart_0.1");
                        constraintMap.setRotation(90);//suposé faire une rotation de la carte
                        break;
                    case "Rdc_Code2":
                        map.setBackgroundResource(R.drawable.rdc);
                        initializePoint("RDC");
                        map.setStart("depart_0.2");
                        break;
                    case "Rdc_Code3":
                        map.setBackgroundResource(R.drawable.rdc);
                        initializePoint("RDC");
                        map.setStart("depart_0.3");
                        break;
                    case "Etage1_Code1":
                        map.setBackgroundResource(R.drawable.etage1);
                        initializePoint("ET1");
                        map.setStart("depart_1.1");
                        break;
                    case "Etage1_Code2":
                        map.setBackgroundResource(R.drawable.etage1code2);
                        initializePoint("ET1");
                        map.setStart("depart_1.2");
                        break;
                    case "Etage1_Code3":
                        map.setBackgroundResource(R.drawable.etage1);
                        initializePoint("ET1");
                        map.setStart("depart_1.3");
                        break;
                    case "Etage2_Code1":
                        map.setBackgroundResource(R.drawable.etage2);
                        initializePoint("ET2");
                        map.setStart("depart_2.1");
                        break;
                    case "Etage2_Code2":
                        map.setBackgroundResource(R.drawable.etage2);
                        initializePoint("ET2");
                        map.setStart("depart_2.2");
                        break;
                    case "Etage2_Code3":
                        map.setBackgroundResource(R.drawable.etage2);
                        initializePoint("ET2");
                        map.setStart("depart_2.3");
                        break;
                }
                String numSalle = (batiment.getSelectedItem().toString() + etage.getSelectedItem().toString() + salle.getSelectedItem().toString());
                resultat.setText("salle recherché " + numSalle);
                map.setStop(numSalle);
                map.Astar();

                //String text = map.Astar();
                //resultat.setText(text);
            }
        });
    }

    @Override
    protected void onStop() {
        if (map.animationthread.getStatus() == AsyncTask.Status.RUNNING)
            map.animationthread.cancel(true);
        super.onStop();
    }

    public void initializePoint(String etage){
        Resources res = getResources();
        map.graph.clear();
        String[] pointX;
        String[] pointY;
        String[] pointName;
        String[] link;
        if (etage.equals("RDC")) {
            pointX = res.getStringArray(R.array.RDCpointsX);
            pointY = res.getStringArray(R.array.RDCpointsY);
            pointName = res.getStringArray(R.array.RDCname);
            link=res.getStringArray(R.array.RDClink);
        }
        else if (etage.equals("ET1")){
            pointX = res.getStringArray(R.array.ET1pointsX);
            pointY = res.getStringArray(R.array.ET1pointsY);
            pointName = res.getStringArray(R.array.ET1name);
            link=res.getStringArray(R.array.ET1link);

        }
        else{
            pointX = res.getStringArray(R.array.ET2pointsX);
            pointY = res.getStringArray(R.array.ET2pointsY);
            pointName = res.getStringArray(R.array.ET2name);
            link=res.getStringArray(R.array.ET2link);
        }

        float x;
        float y;
        String name;
        int cpt=0;
        height=map.getHeight();
        width=map.getWidth();
        try {
            while (true){
                x = Float.parseFloat(pointX[cpt]) * (width / 864);
                y = Float.parseFloat(pointY[cpt]) * (height / 864);
                name = pointName[cpt];
                map.graph.addVertex(name, x, y);
                cpt++;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("fin de fichier: "+e);
        }

        try {
            cpt=0;
            while (true){
                map.graph.addEdge(link[cpt],link[cpt+1]);
                cpt=cpt+2;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("fin de fichier: "+e);
        }
    }
}

