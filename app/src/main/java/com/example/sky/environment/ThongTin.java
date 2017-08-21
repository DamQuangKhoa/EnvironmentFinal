package com.example.sky.environment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.Config;
import model.DiaDiemChinh;

public class ThongTin extends AppCompatActivity {

    Button btnGui;
    EditText txtTime,txtDate;
    ImageButton btnTime,btnDate;
    RadioButton radONhiem,radKetXe;
    AutoCompleteTextView autoDuong;
    Spinner spKhuVuc,spMucDo;
    ArrayAdapter<String> duongAdapter,khuVucAdapter,mucDoAdapter;
    String[] dsDuong;
    String[] dsKhuVuc;
    String[] dsMucDo;
    Calendar calendar;
    SimpleDateFormat sdf1,sdf2;
    int lastItemKhuVuc =0,lastItemMucDo=0;
    DiaDiemChinh diaDiem;
    double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);

        addControls();
        addEvents();

    }

    private void addEvents() {
btnTime.setOnClickListener(v -> xuLyTime());
        btnDate.setOnClickListener(v -> xuLyDate());
        btnGui.setOnClickListener(v -> xulyGui());
    }

    private void xulyGui() {
        String tenDuong,khuVuc,mucDo,thoiGian,ngayThang;
        String ketXe;
        if(radKetXe.isChecked()){
        ketXe = "Kẹt Xe";
        }
        else {
            ketXe ="Ô Nhiễm";
        }
        tenDuong =  autoDuong.getText().toString();
        spKhuVuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastItemKhuVuc = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMucDo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastItemMucDo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
            khuVuc = dsKhuVuc[lastItemKhuVuc];
            mucDo = dsMucDo[lastItemMucDo];
        ngayThang = txtDate.getText().toString();
        thoiGian =ngayThang+" " +txtTime.getText().toString();
        diaDiem = new DiaDiemChinh(tenDuong,khuVuc,mucDo,ketXe,thoiGian,lat,lon);

//        Log.e("AAA",diaDiem.toString());
        guiDuLieuWeb(diaDiem);

    }

    private void guiDuLieuWeb(DiaDiemChinh diaDiem) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("AAA",response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Loi",error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  params = new HashMap<>();

                params.put(Config.ACTION,Config.CSD);
                params.put(Config.TENDUONG,diaDiem.getDuong());
                params.put(Config.KHUVUC,diaDiem.getKhuvuc());
                params.put(Config.LOAI,diaDiem.getLoai());
                params.put(Config.MUCDO,diaDiem.getMucdo());
                params.put(Config.TIME,diaDiem.getThoiGianBD());
                params.put(Config.LAT,diaDiem.getLatitude()+"");
                params.put(Config.LONG,diaDiem.getLongtitude()+"");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void xuLyDate() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(calendar.YEAR,year);
                    calendar.set(calendar.MONTH,month);
                    calendar.set(calendar.DAY_OF_MONTH,day);
                txtDate.setText(sdf1.format(calendar.getTime()));
            }
        };

        DatePickerDialog dpdl = new DatePickerDialog(
                ThongTin.this,
                callback,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH)
        );
        dpdl.show();
    }

    private void xuLyTime() {
        TimePickerDialog.OnTimeSetListener callBack = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR,hour-1);
                calendar.set(Calendar.MINUTE,minute);

                txtTime.setText(sdf2.format(calendar.getTime()));
            }
        };
        TimePickerDialog tpdl = new TimePickerDialog(
                ThongTin.this,
                callBack,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true // true la 24h, false la 12h
        );
        tpdl.show();
    }

    private void addControls() {
        calendar = Calendar.getInstance();
txtTime = (EditText) findViewById(R.id.txtTime);
        txtDate = (EditText) findViewById(R.id.txtDate);
        btnTime = (ImageButton) findViewById(R.id.btnTime);
        btnDate = (ImageButton) findViewById(R.id.btnDate);
        btnGui = (Button) findViewById(R.id.btnGui);
        radKetXe = (RadioButton) findViewById(R.id.radKetXe);
        radONhiem = (RadioButton) findViewById(R.id.radONhiem);
        autoDuong = (AutoCompleteTextView) findViewById(R.id.autoDuong);
        spKhuVuc = (Spinner) findViewById(R.id.spKhuVuc);
        spMucDo = (Spinner) findViewById(R.id.spMucDo);

        dsDuong = getResources().getStringArray(R.array.duong);
        dsKhuVuc = getResources().getStringArray(R.array.quan);
        dsMucDo = getResources().getStringArray(R.array.mucdo);
        khuVucAdapter = new ArrayAdapter<String>(
                ThongTin.this,
                android.R.layout.simple_list_item_1,
                dsKhuVuc
        );
        duongAdapter = new ArrayAdapter<String>(
                ThongTin.this,
                android.R.layout.simple_list_item_1,
                dsDuong
        );
      mucDoAdapter = new ArrayAdapter<String>(
                ThongTin.this,
                android.R.layout.simple_list_item_1,
                dsMucDo
        );
        khuVucAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mucDoAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autoDuong.setAdapter(duongAdapter);
        spKhuVuc.setAdapter(khuVucAdapter);
        spMucDo.setAdapter(mucDoAdapter);

        sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        sdf2 = new SimpleDateFormat("HH:mm:ss");

        txtDate.setText(sdf1.format(calendar.getTime()));
        txtTime.setText(sdf2.format(calendar.getTime()));


        Intent intent = getIntent();
        lat=intent.getDoubleExtra("Lat",-1);
        lon =intent.getDoubleExtra("Long",-1);
//        Log.e("AAA",lat+" "+lon);


    }
}
