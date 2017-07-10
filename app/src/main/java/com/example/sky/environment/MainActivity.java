package com.example.sky.environment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import model.DiaDiem;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    FloatingActionButton btnXuLy;
    private GoogleMap mMap;
    Spinner spMap1, spMap2;
    List<String> dsMapType;
    ArrayAdapter<String> mapTypeAdapter;
    ProgressDialog progressDialog;
    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener
            = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(mMap != null){
                mMap.clear();
               mMap.addMarker(new MarkerOptions()
                    .position(loc)
                        .title("Marker in Sydney")
                      .snippet("Vi Tri Hien Tai"));


                mMap.animateCamera(CameraUpdateFactory
                       .newLatLngZoom(loc,16.0f));
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
    }

    private void addControls() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1"); // id
        tab1.setIndicator("Ket Xe");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2"); // id
        tab2.setIndicator("Môi Trường");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        btnXuLy = (FloatingActionButton) findViewById(R.id.btnXuLy);

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
        btnXuLy.setOnClickListener(e -> xuLyGui());

        spMap1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                xuLyTypeHienThi(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        Intent intent = new Intent(MainActivity.this, ThongTin.class);
        startActivity(intent);
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
//        mMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
//                Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
Intent intent= getIntent();
        DiaDiem dd= (DiaDiem) intent.getSerializableExtra("DIADIEM");
        Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();
        LatLng sydney = new LatLng(dd.getLat(), dd.getLon());
       Marker marker= mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Dia Diem")
                .snippet(dd.getTenDuong()));
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(sydney,16));
        Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();
        mMap.setInfoWindowAdapter(new MapAdapter(MainActivity.this,dd));
        marker.showInfoWindow();


    }

    private void hienThiViTriHienTai() {

    }
}
