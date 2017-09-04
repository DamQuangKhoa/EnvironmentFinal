package com.example.sky.environment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import map.GPSTracker;
import model.Config;

public class Start extends AppCompatActivity implements View.OnClickListener  {
Button btnGift,btnMap,btnSendData,btnNew,btnExit;
    View.OnTouchListener touchListener;
    GPSTracker gps;  Location loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        addControls();
        addEvent();

    }

    private void addEvent() {


    }

    private void addControls() {
        btnGift = (Button) findViewById(R.id.btnGift);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnSendData = (Button) findViewById(R.id.btnSendData);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnExit = (Button) findViewById(R.id.btnExit);



        btnExit.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnSendData.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnGift.setOnClickListener(this);

        gps = new GPSTracker(Start.this);
    }
    public Location getLocation(){

        return loc;
    }
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btnExit:
                System.exit(0);
                break;
            case R.id.btnSendData:
                if(loc != null) {
                    intent = new Intent(this,ThongTin.class);
                    intent.putExtra(Config.LAT, loc.getLatitude());
                    intent.putExtra(Config.LONG, loc.getLongitude());
                }
                else {
//                    gps.showSettingsAlert();
                }
                break;
            case R.id.btnNew:
                intent = new Intent(this,TinTuc.class);
                startActivity(intent);
                break;
            case R.id.btnMap:
//                loc=gps.getLocation();
//                if(loc != null) {
                    intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
//                }
//                else {
//                    gps.showSettingsAlert();
//                }
                break;
            case R.id.btnGift:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
