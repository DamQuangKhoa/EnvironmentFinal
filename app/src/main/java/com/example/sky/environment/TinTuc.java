package com.example.sky.environment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.stream.Collectors;

import adapter.ListViewAdapter;
import model.Config;
import model.DiaDiem;

public class TinTuc extends AppCompatActivity {
ListView lvKetXe,lvONhiem;
List<DiaDiem> dsDiaDiem,dsKetXe,dsONhiem;
ListViewAdapter ketXeadapter,oNhiemAdapter;
    Calendar calendar;
    SimpleDateFormat sdf1;
    Map<Integer,List<DiaDiem>> mapDiaDiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);
        addControls();
        addEvents();
//        getData(Config.URL);
    }
    private void addControls() {
        ActionBar actionBar = getActionBar();
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost2);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab2_1");
        tab1.setIndicator("",getResources().getDrawable(R.drawable.lua));
        tab1.setContent(R.id.tab2_1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2_2");
        tab2.setIndicator("",getResources().getDrawable(R.drawable.cay));
        tab2.setContent(R.id.tab2_2);
        tabHost.addTab(tab2);

        lvKetXe = (ListView) findViewById(R.id.lvKetXe);
        lvONhiem = (ListView) findViewById(R.id.lvONhiem);

        mapDiaDiem = new LinkedHashMap<>();
        dsDiaDiem = new ArrayList<>();
        dsKetXe = new ArrayList<>();
        dsONhiem = new ArrayList<>();

            ketXeadapter= new ListViewAdapter(TinTuc.this,
                                            R.layout.item_listview,dsKetXe);
            oNhiemAdapter= new ListViewAdapter(TinTuc.this,
                                            R.layout.item_listview,dsONhiem);
        lvKetXe.setAdapter(ketXeadapter);
        lvONhiem.setAdapter(oNhiemAdapter);
        calendar = Calendar.getInstance();
         sdf1 = new SimpleDateFormat("yyyy/MM/dd");

    }
    private void addEvents() {
        lvKetXe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaDiem dd = dsDiaDiem.get(i);
                Intent intent = new Intent(TinTuc.this,MainActivity.class);
                intent.putExtra("DIADIEM",dd);
                startActivity(intent);
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
});

        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mncontact:
                Toast.makeText(TinTuc.this,"Contact",Toast.LENGTH_LONG).show();
//                String date = sdf1.format(calendar.getTime());
                getData("2017-08-12");
                changeMapToList();
                break;
            case R.id.mnInfo:
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
//                Log.e("AAA",response);
                xuLySRD(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("loi",error.getMessage());
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
@RequiresApi(api = Build.VERSION_CODES.N)
public void changeMapToList(){
    dsDiaDiem.clear();
    for (int i =5;i>0;i--){
        if(mapDiaDiem.get(i) == null){
            continue;
        }
        else{
            dsDiaDiem.addAll(mapDiaDiem.get(i));
        }
    }

    dsKetXe=dsDiaDiem.stream()
            .filter(e -> e.getLoai().equals("Kẹt Xe"))
            .collect(Collectors.toList());
    dsONhiem = dsDiaDiem.stream()
            .filter(e -> e.getLoai().equals("Ô Nhiễm"))
            .collect(Collectors.toList());
    ketXeadapter.notifyDataSetChanged();
    oNhiemAdapter.notifyDataSetChanged();
//    Log.e("FFF","Danh Sach Dia Diem Final: "+ dsDiaDiem);
}
    private void xuLySRD(String response) {
        JSONObject objectTmp= null;
        JSONArray arrayTmp=null;
        mapDiaDiem.clear();
        try {
//            Log.e("DDD",response);
            JSONObject jsonObject =  new JSONObject(response);
            for (int i = 1; i<5 ; i++) {
                if((arrayTmp = jsonObject.getJSONArray(i+"")).length() ==0){
//                    Log.e("AAA",i+"");
                    continue;
                }
                else{
//                    Log.e("CCC",arrayTmp+"");
                    List<DiaDiem> list =  new ArrayList<>();
                    for (int j = 0; j<arrayTmp.length(); j++){
                        objectTmp = arrayTmp.getJSONObject(j);
                        DiaDiem dd = new DiaDiem(
                                objectTmp.getString("tenDuong"),
                                objectTmp.getString("thoiGianBatDau"),
                                objectTmp.getString("ketXe"),
                                objectTmp.getInt("mucDo"),
                                objectTmp.getInt("khuVuc"),
                                objectTmp.getInt("hinhAnh"),
                                objectTmp.getDouble("lat"),
                                objectTmp.getDouble("lon")
                        );
//                       Log.e("AAA","Dia Diem: "+dd.toString());
                        list.add(dd);
                    }
                    Log.e("BBB","Array So "+i +":"+list+"");
                    mapDiaDiem.put(i,list);
                }
            }


        } catch (JSONException e) {
           Log.e("AAA","Loi Xu Ly SRD: "+ e.getMessage());
        }
    }
}
