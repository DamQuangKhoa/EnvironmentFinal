package com.example.sky.environment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileDescriptor;
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
import controller.TinyDB;
import map.GPSTracker;
import model.Config;
import model.DiaDiem;
import model.User;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private static int RESULT_LOAD_IMAGE = 1;
    private GoogleMap mMap;
    Geocoder geocoder;
    List<Address> addresses;
    TabHost tabHost;
    String[] dsTenDuong;
    ArrayAdapter<String> tenDuongAdapter;
    ProgressDialog progressDialog;
    LatLng loca;
    Intent intent;
    GPSTracker gps;
    Location loc;
    String address = "";
    List<DiaDiem> dsDiaDiem;
    TextView txtEmail, txtUser, txtAddress, txtGrade;
    Uri selectedImage;
    LocationManager locationManager;
    User userLocal;
    ImageView imgUser, imgRank;
    ImageButton btnEditUser;
    boolean xemTinTuc = false;
    FirebaseUser user;
    TinyDB tinydb;


    Context mContext = MainActivity.this;
    Calendar calendar = Calendar.getInstance();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(MainActivity.this, TinTuc.class);
//                    Log.e("AAA",loca+"");
                    startActivity(intent);
                    return true;
                case R.id.menu:
                    intent = new Intent(MainActivity.this, Future.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addControls();
        addEvents();
        initLocation();
    }

    private void initLocation() {
//        makeLog(loc.getLatitude()+"__"+loc.getLongitude());
        address=getAddress(loc);
        if(address != null){
        sendAddressToWeb(address);}
    }

    private String getAddress(Location loc) {
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0);
            String demo = addresses.get(0).getCountryName();
            String city = addresses.get(0).getAdminArea(); // ho chi minh - thanh pho
            String state = addresses.get(0).getLocality(); // phuong
            String demo3 = addresses.get(0).getFeatureName(); // ten duong

            Log.e(getString(R.string.MainActivityError),address+"____"+demo+"____"+city+"____"+state+"____"+demo3);
//        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
        makeLog(e.getMessage());
        }
        return address;
    }

    ;
public void makeLog(String message){
    Log.e(getString(R.string.MainActivityError),message);
}
    private void addControls() {
        tinydb = new TinyDB(MainActivity.this);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1"); // id
        tab1.setIndicator("", ContextCompat.getDrawable(this, R.drawable.lua));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2"); // id
        tab2.setIndicator("", ContextCompat.getDrawable(this, R.drawable.cay));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        dsTenDuong = getResources().getStringArray(R.array.duong);
        tenDuongAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dsTenDuong);
        dsDiaDiem = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




	    progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Map...");
        progressDialog.setMessage("Vui Lòng Chờ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
//        gps = new GPSTracker(MainActivity.this);
        xemTinTuc = false;
        checkMap();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                makeLog("Click floatting button");
                intent = new Intent(MainActivity.this, ThongTin.class);
//                    Log.e("AAA",loca+"");
                if (loc != null) {
                    intent.putExtra(Config.LAT, loc.getLatitude());
                    intent.putExtra(Config.LONG, loc.getLongitude());
                    makeLog(loc.getLatitude()+"_"+loc.getLongitude());
                    startActivity(intent);
                }
            }
        });
       /* intent = getIntent();

        if ((address = intent.getStringExtra(Config.TENDUONG)) != null) {
            sendAddressToWeb(address);
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);


        btnEditUser = (ImageButton) header.findViewById(R.id.btnEditUser);
        txtUser = (TextView) header.findViewById(R.id.txtUser);
        txtEmail = (TextView) header.findViewById(R.id.txtEmail);
        txtAddress = (TextView) header.findViewById(R.id.txtAddress);
        txtGrade = (TextView) header.findViewById(R.id.txtGrade);
        imgUser = (ImageView) header.findViewById(R.id.imgUser);
        imgRank = (ImageView) header.findViewById(R.id.imgRank);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            userLocal = tinydb.getObject("user", User.class) == null ? new User(mContext) : tinydb.getObject("user", User.class);
            Log.e(getString(R.string.MainActivityError), userLocal.toString());
            userLocal.setEmail(user.getEmail());
            txtEmail.setText(userLocal.getEmail());
            txtAddress.setText(userLocal.getAddress());
            txtGrade.setText("Grade: " + userLocal.getGrade() + "");
            imgUser.setImageBitmap(userLocal.getImage());
            imgRank.setImageBitmap(userLocal.getRank());
        } else {
            // No user is signed in
        }
    }

    private void checkMap() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        if (locationManager != null) {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (gpsIsEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
MyFunction.showDialog(this,"Please enable your location");
                    return;
                }
                else{

	                loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


	                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                }

            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void addEvents() {
        btnEditUser.setOnClickListener(v ->{
            xuLyEditUser();

        });
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
    private void xuLyEditUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Title");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.dialog_input, null , false);
        // Set up the input
        final EditText txt_changeName = (EditText) viewInflated.findViewById(R.id.txt_changeName);
        final EditText txt_changeAddress = (EditText) viewInflated.findViewById(R.id.txt_changeAddress);
        final Button btnAvatar = (Button) viewInflated.findViewById(R.id.btnAvatar);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = txt_changeName.getText().toString();
                String address = txt_changeAddress.getText().toString();
                userLocal.setAddress(address);
                userLocal.setName(name);
                txtAddress.setText(address);
                txtUser.setText(name);
                if(selectedImage != null ){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(selectedImage)
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                        try {
                                            Bitmap bm =getBitmapFromUri(selectedImage);
                                            imgUser.setImageBitmap(bm);
                                            userLocal.setImage(bm);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void pickImageFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
             selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem search = menu.findItem(R.id.mnSearch);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new MyOnQueryTextListener());

        return super.onCreateOptionsMenu(menu);
    }

    private void saveAddressToSharePre(String query) {

        ArrayList<String> list =tinydb.getListString(Config.listHistory) == null? new ArrayList<>(): tinydb.getListString("location");
        if(!list.contains(query)) {
            list.add(query);
        }
        list.clear();
        tinydb.putListString(Config.listHistory,list);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mncontact:
                break;
            case R.id.mnUpdate:
                if(loc !=null)initLocation();
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
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                try {
                    progressDialog.dismiss();
                    Intent intent= getIntent();
                    DiaDiem dd = (DiaDiem) intent.getSerializableExtra("DIADIEM");
                    if(dd != null)hienThiDiaDiem(dd);
                    else if(loc !=null) initLocation();

                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Context: "+e.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    private void sendAddressToWeb(String address){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               makeLog("sendAddressToWeb"+": "+response);
                xuLySSA(response);
                khoanhVungDiaDiem(dsDiaDiem);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeLog("sendAddressToWeb: "+error.getMessage());
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
            MyFunction.makeToastErro(this,getString(R.string.SSA_Error),ex.getMessage());
            makeLog( "xuLySSA: "+ex.getMessage());
        }catch (Exception ex){
            MyFunction.makeToastErro(this,getString(R.string.SSA_Error),ex.getMessage());
            makeLog( "xuLySSA: "+ ex.getMessage());
        }
    }
private void makeCircle(LatLng lat){
    CircleOptions optionCircle=new CircleOptions();
    optionCircle.center(lat).radius(50);
    Circle cir=mMap.addCircle(optionCircle);
    cir.setStrokeColor(Color.GREEN);
}
    private void hienThiDiaDiem(DiaDiem dd){

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
                    .fromResource(R.mipmap.ic_heart));
            marker.showInfoWindow();
    }
}
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_setting) {
            changeUserSetting();
            // Handle the camera action
        }  else if (id == R.id.nav_change_language) {
            changeLanguageSetting();

        } else if (id == R.id.nav_grade) {

        } else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_history) {
            startActivity(new Intent(mContext,History.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private void changeUserSetting() {
        Intent intent = new Intent(getApplicationContext(),ChangeUserInformation.class);
        startActivity(intent);
    }

        private void changeLanguageSetting() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
        startActivity(intent);
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
    @Override
    public void onLocationChanged(Location location) {
makeLog("Vao Onlocation Changed");
        loc = location;
        loca= new LatLng(location.getLatitude(), location.getLongitude());
//            Log.e("AAA","Lating: "+ loca);
        makeLog(loca.latitude+"_"+loca.longitude);
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
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class MyOnQueryTextListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query)
        {
            int erroCode ;
            if((erroCode=MyFunction.testString(query))== Config.SUCCESS) {
	            Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
	            sendAddressToWeb(query);
                saveAddressToSharePre(query);
                return true;
            }
            else{
                switch (erroCode){
                    case Config.HAVE_NO_WORD:
                        MyFunction.showDialog(mContext,getString(R.string.input_address));
                        break;
                    case Config.HAVE_NUMBER:
                        MyFunction.showDialog(mContext,getString(R.string.input_not_number));
                        break;
                    case Config.HAVE_SPECIAL_CHAR:
                        MyFunction.showDialog(mContext,getString(R.string.input_not_speacialChar));
                        break;
                }

                // Showing Alert Message
        }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            tenDuongAdapter.getFilter().filter(newText);
            // hello world
            return false;
        }
    }
}
