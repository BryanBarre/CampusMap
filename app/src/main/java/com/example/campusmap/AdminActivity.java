package com.example.campusmap;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by Bryan BARRE on 14/03/2021.
 */
public class AdminActivity extends FragmentActivity {
    MapView map;
    float height;
    float width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = findViewById(R.id.map);
        setContentView(R.layout.activity_admin);
        Button link = (Button)findViewById(R.id.buttonLink);
        Button addPoint = (Button)findViewById(R.id.buttonAddPoint);
        Button rdc = (Button)findViewById(R.id.rdc);
        Button etage1 = (Button)findViewById(R.id.etage1);
        Button etage2 = (Button)findViewById(R.id.etage2);

        final TextView p1 = (TextView) findViewById(R.id.editTextTextPointName1);
        final TextView p2 = (TextView) findViewById(R.id.editTextTextPointName2);
        final TextView pName = (TextView) findViewById(R.id.editTextTextPointName);
        final TextView coordX = (TextView) findViewById(R.id.editTextTextCoordX);
        final TextView coordY = (TextView) findViewById(R.id.editTextTextCoordY);

        coordX.setTransformationMethod (null);//pour avoir uniquement des chiffre sur le pavé numerique
        coordY.setTransformationMethod (null);

        map = (MapView) findViewById(R.id.map);

        rdc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                map.setBackgroundResource(R.drawable.rdc);
                initializePoint("RDC");
            }
        });

        etage1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                map.setBackgroundResource(R.drawable.etage1cadri);
                initializePoint("ET1");
            }
        });

        etage2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                map.setBackgroundResource(R.drawable.etage2);
                initializePoint("ET2");
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link1 = p1.getText().toString();
                String link2 = p2.getText().toString();
                System.out.println("ajout du lien entre: " + link1 + " et " + link2);

                map.graph.addEdge(link1,link2);
                //ecrire le link dans le XML
                //puis relancer initializePoint(String etage)

            }
        });

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x=Integer.parseInt(coordX.getText().toString());
                int y=Integer.parseInt(coordY.getText().toString());
                String name=pName.getText().toString();
                System.out.println("rajout du point: X=" + x + " Y= " + y + " name= " + name);

                map.graph.addVertex(name, x, y);

            }
        });
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
        }else if (etage.equals("ET1")){
            pointX = res.getStringArray(R.array.ET1pointsX);
            pointY = res.getStringArray(R.array.ET1pointsY);
            pointName = res.getStringArray(R.array.ET1name);
            link=res.getStringArray(R.array.ET1link);

        }else{
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
        System.out.println("-----------------------"+height+"---"+width);
        try {
            while (true){
                x = Float.parseFloat(pointX[cpt]) * (width / 864);
                y = Float.parseFloat(pointY[cpt]) * (height / 864);
                name = pointName[cpt];
                map.graph.addVertex(name, x, y);
                System.out.println("point: X=" + pointX[cpt] + " Y= " + pointY[cpt] + " name= " + pointName[cpt]);
                cpt++;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("fin de fic "+e);
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
