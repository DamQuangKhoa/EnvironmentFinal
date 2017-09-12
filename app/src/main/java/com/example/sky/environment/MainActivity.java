package com.example.sky.environment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.MapAdapter;
import controller.MyFunction;
import map.GPSTracker;
import model.Config;
import model.DiaDiem;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton btnXuLy;
    private GoogleMap mMap;
    Geocoder geocoder;
    List<Address> addresses;
    TabHost tabHost;
    String[] dsTenDuong;
    ArrayAdapter<String> tenDuongAdapter;
    ArrayAdapter<String> mapTypeAdapter;
    ProgressDialog progressDialog;
    LatLng loca;
    Intent intent;
    GPSTracker gps;
    Location location;
    String address="";
    List<DiaDiem> dsDiaDiem;
    boolean xemTinTuc= false;
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
                    if(location != null) {
                        intent.putExtra(Config.LAT, location.getLatitude());
                        intent.putExtra(Config.LONG, location.getLongitude());
                        startActivity(intent);
                    }
                    else{
                        gps = new GPSTracker(MainActivity.this);
                        gps.showSettingsAlert();
                    }
                    return true;
            }
            return false;
        }
    };


    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener
            = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location loc) {
            location =loc;
            loca= new LatLng(loc.getLatitude(), loc.getLongitude());
//            Log.e("AAA","Lating: "+ loca);
            if(mMap != null && !xemTinTuc){
                mMap.clear();
               mMap.addMarker(new MarkerOptions()
                    .position(loca)
                        .title("Vị Trí Bản Đồ")
                      .snippet("Vi Tri Hiện Tại Của Bạn")
               ).setIcon(BitmapDescriptorFactory
                       .fromResource(R.mipmap.ic_location));;


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
        setContentView(R.layout.activity_main2);
        addControls();
        addEvents();
//        initLocation();
    }

    private void initLocation() {
//        address=getAddress(location);
        sendAddressToWeb(Config.currentAddress);
    }

    private String getAddress(Location loc){
    geocoder = new Geocoder(this, Locale.getDefault());
    String address="";
    try {
        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        address= addresses.get(0).getAddressLine(0);
//        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

    } catch (IOException e) {
        Toast.makeText(this, "Loi Get Address", Toast.LENGTH_SHORT).show();
        Log.e("Loi",e.getMessage());
    }
    return address;
};


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
        dsTenDuong = getResources().getStringArray(R.array.duong);
        tenDuongAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,dsTenDuong);
        dsDiaDiem = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Map...");
        progressDialog.setMessage("Vui Lòng Chờ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        xemTinTuc = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void addEvents() {

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equalsIgnoreCase("tab1")){
//                    location =getLoCation();

                }
                else if (s.equalsIgnoreCase("tab2")){
//                    location=getLoCation();
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem search = menu.findItem(R.id.mnSearch);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                sendAddressToWeb(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                tenDuongAdapter.getFilter().filter(newText);
                // hello world
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mncontact:
                break;
            case R.id.mnUpdate:
                break;
            case R.id.mnSearch:
                break;
            case R.id.mnSetting:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void xuLyGui() {
//        MyFunction.makeToast(MainActivity.this,"Demo");
       intent = new Intent(MainActivity.this, ThongTin.class);
        startActivity(intent);
    }
//    public Location getLoCation(){
//        Location res=null ;
//        gps = new GPSTracker(MainActivity.this);
//
//        // check if GPS enabled
//        if(gps.canGetLocation()){
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//            res = new Location("vi tri");
//            res.setLatitude(latitude);
//            res.setLongitude(longitude);
//        }else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//        return res;
//    }

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
                hienThiDiaDiem();
//               Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                if(loca != null) {
//                Toast.makeText(MainActivity.this, loca.latitude + "_" + loca.longitude, Toast.LENGTH_LONG).show();
            }
                progressDialog.dismiss();
            }
        });

////        Toast.makeText(MainActivity.this,"Vao 2",Toast.LENGTH_LONG).show();



    }
    private void sendAddressToWeb(String address){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                MyFunction.makeToastTest(MainActivity.this,"Kiem Tra",response);
                xuLySSA(response);
                khoanhVungDiaDiem(dsDiaDiem);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            MyFunction.makeToastErro(MainActivity.this,"Xu Ly SSA",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat(Config.sdfDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat(Config.sdfTime);
                Map<String,String>  params = new HashMap<>();
                params.put(Config.ACTION,Config.CSA);
                params.put(Config.TENDUONG,address);
//                params.put(Config.TIME,sdf1.format(calendar.getTime()));
                params.put(Config.TIME,Config.currentTime);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void khoanhVungDiaDiem(List<DiaDiem> dsDiaDiem) {
        PolygonOptions pgOption=new PolygonOptions();
        LatLng latingTmp=null;
        int countMucDo=0;
        if(dsDiaDiem.size()>0) {
            for (DiaDiem d :
                    dsDiaDiem) {
                if (d.getMucDoInt() > 2) {
                    countMucDo++;
                }
                latingTmp = new LatLng(d.getLat(), d.getLon());
                pgOption.add(latingTmp);

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latingTmp)
                        .title("Dia Diem")
                        .snippet(d.getTenDuong()));
                mMap.setInfoWindowAdapter(new MapAdapter(MainActivity.this, d));
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_greendot));
                marker.showInfoWindow();

            }
            Polygon polyGon = mMap.addPolygon(pgOption);
            if (countMucDo > 6) {
                polyGon.setStrokeColor(Color.RED);
            } else if (countMucDo > 4) {
                polyGon.setStrokeColor(Color.BLUE);
            } else {
                polyGon.setStrokeColor(Color.GREEN);
            }
        }
    }
    private void xuLySSA(String response) {
//        MyFunction.makeToastTest(this,"data SSA",response);
        JSONObject objectTmp= null;
        JSONArray arrayTmp=null;
        DiaDiem dd = null;
        dsDiaDiem.clear();
        try {
            arrayTmp = new JSONArray(response);
            for (int i=0;i<arrayTmp.length();i++){
                objectTmp = arrayTmp.getJSONObject(i);
                 dd = new DiaDiem(
                        objectTmp.getString("tenDuong"),
                        objectTmp.getString("thoiGianBatDau"),
                        objectTmp.getString("ketXe"),
                        objectTmp.getInt("mucDo"),
                        objectTmp.getInt("khuVuc"),
                        objectTmp.getString("hinhAnh"),
                        objectTmp.getDouble("lat"),
                        objectTmp.getDouble("lon")
                );
                dsDiaDiem.add(dd);
            }
        }catch (RuntimeException ex){
            MyFunction.makeToastErro(this,"Loi Xu Ly SSA",ex.getMessage());
        }catch (Exception ex){
            MyFunction.makeToastErro(this,"Loi Xu Ly SSA",ex.getMessage());
        }
    }
private void makeCircle(LatLng lat){
    CircleOptions optionCircle=new CircleOptions();
    optionCircle.center(lat).radius(100);
    Circle cir=mMap.addCircle(optionCircle);
    cir.setStrokeColor(Color.GREEN);
    cir.setFillColor(Color.RED);
}
    private void hienThiDiaDiem(){
        Intent intent= getIntent();
        DiaDiem dd = (DiaDiem) intent.getSerializableExtra("DIADIEM");
        if(dd != null){
            xemTinTuc = true;
            sendAddressToWeb(dd.getTenDuong());
            LatLng sydney = new LatLng(dd.getLat(), dd.getLon());
            if(mMap != null) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Dia Diem")
                    .snippet(dd.getTenDuong()));
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(sydney, 16));
            makeCircle(sydney);
//        Toast.makeText(MainActivity.this,dd.getTenDuong(),Toast.LENGTH_LONG).show();
            mMap.setInfoWindowAdapter(new MapAdapter(MainActivity.this, dd));
            marker.setIcon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.ic_launcher));
            marker.showInfoWindow();
        }
    }
}

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
