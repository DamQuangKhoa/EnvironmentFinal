package com.example.sky.environment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import controller.MyFunction;
import map.GPSTracker;
import model.Config;

public class Start extends AppCompatActivity implements View.OnClickListener  {
Button btnGift,btnMap,btnSendData,btnNew,btnExit;
    View.OnTouchListener touchListener;
    GPSTracker gps;  Location loc;
    private LocationManager locationManager;

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
    private boolean checkMap() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        if (locationManager != null) {
            Log.e("checkMap","Vao 1");
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (gpsIsEnabled&&networkIsEnabled) {
                return true;
            }
            else if(!gpsIsEnabled)
            {
                //Show an error dialog that GPS is disabled.
                MyFunction.showDialog(this,"Xin Bật GPS");
                return false;
            }
            else if(!networkIsEnabled)
            {
                //Show an error dialog that GPS is disabled.
                MyFunction.showDialog(this,"Xin Bật Internet");
                return false;
            }
        }
        return true;
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
                    if(checkMap()) {
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                break;
            case R.id.btnGift:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
