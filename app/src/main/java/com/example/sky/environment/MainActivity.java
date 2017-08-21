package com.example.sky.environment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.MapAdapter;
import map.GPSTracker;
import model.DiaDiem;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    FloatingActionButton btnXuLy;
    private GoogleMap mMap;
    TabHost tabHost;
    Spinner spMap1, spMap2;
    List<String> dsMapType;
    ArrayAdapter<String> mapTypeAdapter;
    ProgressDialog progressDialog;
    LatLng loca;
    Intent intent;
    GPSTracker gps;
    Location location;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(MainActivity.this,TinTuc.class);
//                    Log.e("AAA",loca+"");
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(MainActivity.this,ThongTin.class);
//                    Log.e("AAA",loca+"");
                    intent.putExtra("Lat",loca.latitude);
                    intent.putExtra("Long",loca.longitude);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };


    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener
            = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location loc) {
            loca= new LatLng(loc.getLatitude(), loc.getLongitude());
//            Log.e("AAA","Lating: "+ loca);
            if(mMap != null){
                mMap.clear();
               mMap.addMarker(new MarkerOptions()
                    .position(loca)
                        .title("Marker in Sydney")
                      .snippet("Vi Tri Hien Tai"));


                mMap.animateCamera(CameraUpdateFactory
                       .newLatLngZoom(loca,16.0f));
            }
//                mMap.moveCamera(CameraUpdateFactory
//                        .newLatLngZoom(sydney,13));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
      hienThiDiaDiem();

    }



    private void addControls() {
         tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1"); // id
        tab1.setIndicator("",getResources().getDrawable(R.drawable.lua));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2"); // id
        tab2.setIndicator("",getResources().getDrawable(R.drawable.cay));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        spMap1 = (Spinner) findViewById(R.id.spMap1);
        spMap2 = (Spinner) findViewById(R.id.spMap2);
        dsMapType = new ArrayList<>();
        dsMapType.addAll(Arrays.asList(getResources().getStringArray(R.array.mapTpe)));
        mapTypeAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item);
        mapTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMap1.setAdapter(mapTypeAdapter);
//       spMap2.setAdapter(mapTypeAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Thong Tin");
        progressDialog.setMessage("Vui Lòng Chờ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void addEvents() {

        spMap1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                xuLyTypeHienThi(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equalsIgnoreCase("tab1")){
                    Toast.makeText(getApplicationContext(), "Vao 1", Toast.LENGTH_SHORT).show();
                    location =getLoCation();

                }
                else if (s.equalsIgnoreCase("tab2")){
                    Toast.makeText(getApplicationContext(), "Vao 2", Toast.LENGTH_SHORT).show();
                    location=getLoCation();
                }

            }
        });

    }

    private void xuLyTypeHienThi(int position) {
        switch (position) {
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }

    }

    private void xuLyGui() {
//        MyFunction.makeToast(MainActivity.this,"Demo");
       intent = new Intent(MainActivity.this, ThongTin.class);
        startActivity(intent);
    }
    public Location getLoCation(){
        Location res=null ;
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            res = new Location("vi tri");
            res.setLatitude(latitude);
            res.setLongitude(longitude);
          Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            Toast.makeText(getApplicationContext(), "Vao alert", Toast.LENGTH_SHORT).show();
            gps.showSettingsAlert();
        }
        return res;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
//                Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();
//               Location location=mMap.getMyLocation();
//               Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            if(loca != null) {
                Toast.makeText(MainActivity.this, loca.latitude + "_" + loca.longitude, Toast.LENGTH_LONG).show();
            }
                progressDialog.dismiss();
            }
        });

////        Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();



    }
private void hienThiDiaDiem(){
    Intent intent= getIntent();
        DiaDiem dd = (DiaDiem) intent.getSerializableExtra("DIADIEM");
if(dd != null){
    LatLng sydney = new LatLng(dd.getLat(), dd.getLon());
    Marker marker= mMap.addMarker(new MarkerOptions()
            .position(sydney)
            .title("Dia Diem")
            .snippet(dd.getTenDuong()));
    mMap.moveCamera(CameraUpdateFactory
            .newLatLngZoom(sydney,16));
//        Toast.makeText(MainActivity.this,dd.getTenDuong(),Toast.LENGTH_LONG).show();
    mMap.setInfoWindowAdapter(new MapAdapter(MainActivity.this,dd));
    marker.showInfoWindow();
    }
}
    private void hienThiViTriHienTai() {

    }
}
