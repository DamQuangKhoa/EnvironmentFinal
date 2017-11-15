package com.example.sky.environment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import adapter.DiaDiemAdapter;
import controller.RecyclerViewClickListener;
import model.Config;
import model.DiaDiem;

import static com.example.sky.environment.R.id.lvKetXe;
import static com.example.sky.environment.R.id.lvONhiem;

public class TinTuc extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView.LayoutManager mLayoutManager1,mLayoutManager2;
    RecyclerView recyclerViewKetXe,recyclerViewONhiem;
    List<DiaDiem> dsDiaDiem,dsKetXe,dsONhiem;
    DiaDiemAdapter ketXeAdapter,oNhiemAdapter;
    Calendar calendar;
    SimpleDateFormat sdf1;
    Map<Integer,List<DiaDiem>> mapDiaDiem;
    RecyclerViewClickListener listener;
    String dateCurrent="";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> { // item == menuItem
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(TinTuc.this,MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_dashboard:
                        intent = new Intent(TinTuc.this,TinTuc.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu:
                        intent = new Intent(TinTuc.this,Future.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tintuc_main2);
        addEvents();
        addControls();
        updateData();
    }
    private void addControls() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost2);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab2_1");
        tab1.setIndicator("", ContextCompat.getDrawable(this,R.drawable.lua));
        tab1.setContent(R.id.tab2_1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2_2");
        tab2.setIndicator("",ContextCompat.getDrawable(this,R.drawable.cay));
        tab2.setContent(R.id.tab2_2);
        tabHost.addTab(tab2);

        recyclerViewKetXe = (RecyclerView) findViewById(lvKetXe);
        recyclerViewONhiem = (RecyclerView) findViewById(lvONhiem);

       mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager2 = new LinearLayoutManager(this);
        recyclerViewKetXe.setLayoutManager(mLayoutManager1);
        recyclerViewONhiem.setLayoutManager(mLayoutManager2);
        recyclerViewKetXe.setItemAnimator(new DefaultItemAnimator());
        recyclerViewONhiem.setItemAnimator(new DefaultItemAnimator());

        mapDiaDiem = new LinkedHashMap<>();
        dsDiaDiem = new ArrayList<>();
        dsKetXe = new ArrayList<>();
        dsONhiem = new ArrayList<>();


        ketXeAdapter = new DiaDiemAdapter(TinTuc.this,listener);
        oNhiemAdapter= new DiaDiemAdapter(TinTuc.this,listener);

        recyclerViewKetXe.setAdapter(ketXeAdapter);
        recyclerViewONhiem.setAdapter(oNhiemAdapter);
dsKetXe.add(new DiaDiem("Ngã Tư Thủ Đức","B","1/1/2010",50,50,3));
        ketXeAdapter.updateData(dsKetXe);
        oNhiemAdapter.updateData(dsONhiem);


        calendar = Calendar.getInstance();
        sdf1 = new SimpleDateFormat("yyyy/MM/dd");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void taoDsDiaDiemTest() {
        for (int i=0;i<10;i++){
            DiaDiem dd = new DiaDiem("A"+i,"B","1/1/2010",50,50,3);
            dsKetXe.add(dd);
        }
    }

    private void addEvents() {
        listener = (view, position) -> {
//            Toast.makeText(this, "Position " + position, Toast.LENGTH_SHORT).show();

        DiaDiem dd = dsKetXe.get(position);
        Intent intent = new Intent(TinTuc.this,MainActivity.class);
        intent.putExtra("DIADIEM",dd);
        startActivity(intent);

    };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem search = menu.findItem(R.id.mnSearch);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) search.getActionView();
searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
    ketXeAdapter.getFilter().filter(newText);
        return false;
    }
});

        return super.onCreateOptionsMenu(menu);
    }
private void updateData(){
//        dateCurrent = sdf1.format(calendar.getTime());
    getData(Config.currentTime);
}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mncontact:
                Toast.makeText(TinTuc.this,"Contact",Toast.LENGTH_LONG).show();
//                String date = sdf1.format(calendar.getTime());
                break;
            case R.id.mnUpdate:
                    updateData();
                break;
            case R.id.mnSearch:
                break;
            case R.id.mnSetting:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(String date) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("Tin Tuc",response);
                xuLySRD(response);
                changeMapToList();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("loi get Data",error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Config.ACTION,Config.CRD);
                params.put(Config.TIME,date);
                return params;
            }
        }
        ;
        requestQueue.add(stringRequest);
    }
public void changeMapToList(){
    dsDiaDiem.clear();
    Log.e("danh sach map dia diem",mapDiaDiem.toString());
    for (int i =5;i>0;i--){
        if(mapDiaDiem.get(i) == null){
            continue;
        }
        else{
            dsDiaDiem.addAll(mapDiaDiem.get(i));
        }
    }

    for (DiaDiem d:
         dsDiaDiem) {
        if(d.getLoai().equalsIgnoreCase("Kẹt Xe")){
            dsKetXe.add(d);
        }
        else{
            dsONhiem.add(d);
        }
    }
//    Log.e("danh sach KX",dsKetXe.toString());

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        dsKetXe=dsDiaDiem.stream()
//                .filter(e -> e.getLoai().equals("Kẹt Xe"))
//                .collect(Collectors.toList());
//
//    dsONhiem = dsDiaDiem.stream()
//            .filter(e -> e.getLoai().equals("Ô Nhiễm"))
//            .collect(Collectors.toList());
//    }
    ketXeAdapter.updateData(dsKetXe);
    oNhiemAdapter.updateData(dsONhiem);
//    Log.e("FFF","Danh Sach Dia Diem Final: "+ dsDiaDiem);
}
    private  Map<Integer,List<DiaDiem>> xuLySRD(String response) {
        JSONObject objectTmp= null;
        JSONArray arrayTmp=null;
        mapDiaDiem.clear();
        try {
//            Log.e("DDD",response);
            JSONObject jsonObject =  new JSONObject(response);
//            Log.e("Tin tuc","Json: "+ jsonObject.toString());
            for (int i = 1; i<=5 ; i++) {
                if(!jsonObject.has(i+"")){
//                    Log.e("AAA",i+"");
                    continue;
                }
                else{
//                    Log.e("Tin tuc","Vao Day: ");
                    List<DiaDiem> list =  new ArrayList<>();
                    arrayTmp = jsonObject.getJSONArray(i+"");
                    for (int j = 0; j<arrayTmp.length(); j++){
                        objectTmp = arrayTmp.getJSONObject(j);
                        DiaDiem dd = new DiaDiem(
                                objectTmp.getString("tenDuong"),
                                objectTmp.getString("thoiGianBatDau"),
                                objectTmp.getString("ketXe"),
                                objectTmp.getInt("mucDo"),
                                objectTmp.getInt("khuVuc"),
                                objectTmp.getString("hinhAnh"),
                                objectTmp.getDouble("lat"),
                                objectTmp.getDouble("lon")
                        );
//                       Log.e("Tintuc","Dia Diem: "+dd.toString());
                        list.add(dd);
                    }
//                    Log.e("tin tuc","Array So "+i +":"+list+"");
                    mapDiaDiem.put(i,list);
                }
            }

        } catch (JSONException e) {
           Log.e("AAA","Loi Xu Ly SRD: "+ e.getMessage());
        }
        return mapDiaDiem;
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FirebaseAuth auth =  FirebaseAuth.getInstance();

        if (id == R.id.nav_user_setting) {
            changeUserSetting();
            // Handle the camera action
        }  else if (id == R.id.nav_change_language) {
            changeLanguageSetting();

        } else if (id == R.id.nav_grade) {

        } else if (id == R.id.nav_share) {

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
}
