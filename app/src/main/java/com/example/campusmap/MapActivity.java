package com.example.campusmap;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    MapView map;
    //taille de map
    float height;
    float width;

    //------------------------------------------------------------------------------
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    //------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String QrCode = MainActivity.etage;

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        setContentView(R.layout.activity_map);

        Spinner batiment = (Spinner) findViewById(R.id.batiment);
        Spinner etage = (Spinner) findViewById(R.id.etage);
        Spinner salle = (Spinner) findViewById(R.id.salle);

        map = (MapView) findViewById(R.id.map);

        //------------------------------------------------------------------------------
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        //------------------------------------------------------------------------------

        final TextView resultat = (TextView) findViewById(R.id.resultat);
        Button astar = (Button) findViewById(R.id.astar);

        map = (MapView) findViewById(R.id.map);

        switch (QrCode) {
            case "Rdc_Code1"://positioner les case comme cela equivaut a if(x || y)
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

        astar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

    public void initializePoint(String etage) {
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
            link = res.getStringArray(R.array.RDClink);
        } else if (etage.equals("ET1")) {
            pointX = res.getStringArray(R.array.ET1pointsX);
            pointY = res.getStringArray(R.array.ET1pointsY);
            pointName = res.getStringArray(R.array.ET1name);
            link = res.getStringArray(R.array.ET1link);

        } else {
            pointX = res.getStringArray(R.array.ET2pointsX);
            pointY = res.getStringArray(R.array.ET2pointsY);
            pointName = res.getStringArray(R.array.ET2name);
            link = res.getStringArray(R.array.ET2link);
        }

        float x;
        float y;
        String name;
        int cpt = 0;
        height = map.getHeight();
        width = map.getWidth();
        try {
            while (true) {
                x = Float.parseFloat(pointX[cpt]) * (width / 864);
                y = Float.parseFloat(pointY[cpt]) * (height / 864);
                name = pointName[cpt];
                map.graph.addVertex(name, x, y);
                cpt++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("fin de fichier: " + e);
        }

        try {
            cpt = 0;
            while (true) {
                map.graph.addEdge(link[cpt], link[cpt + 1]);
                cpt = cpt + 2;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("fin de fichier: " + e);
        }

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            map.setScaleX(mScaleFactor);
            map.setScaleY(mScaleFactor);
            return true;
        }
    }
    
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

}

