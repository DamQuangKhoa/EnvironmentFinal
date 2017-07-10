package com.example.sky.environment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.DiaDiem;

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
    int lastItemKhuVuc =-1,lastItemMucDo=-1;
    DiaDiem diaDiem;
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
        String loai,tenDuong,khuVuc,mucDo,thoiGian,ngayThang;
        if(radKetXe.isChecked()){
        loai = (String)radKetXe.getText();
        }
        else {
            loai =(String) radONhiem.getText();
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
        thoiGian = txtTime.getText().toString();
        diaDiem = new DiaDiem();

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
                calendar.set(Calendar.HOUR,hour);
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

        sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        sdf2 = new SimpleDateFormat("HH:mm");

        txtDate.setText(sdf1.format(calendar.getTime()));
        txtTime.setText(sdf2.format(calendar.getTime()));

    }
}
