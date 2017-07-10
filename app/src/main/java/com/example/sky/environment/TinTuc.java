package com.example.sky.environment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import adapter.ListViewAdapter;
import model.DiaDiem;

public class TinTuc extends AppCompatActivity {
ListView lvKetXe,lvONhiem;
List<DiaDiem> dsDiaDiem;
ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);

        addControls();
        addEvents();
    }
    private void addControls() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost2);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab2_1");
        tab1.setIndicator("Ket Xe");
        tab1.setContent(R.id.tab2_1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2_2");
        tab2.setIndicator("Ô Nhiễm");
        tab2.setContent(R.id.tab2_2);
        tabHost.addTab(tab2);

        lvKetXe = (ListView) findViewById(R.id.lvKetXe);
        lvONhiem = (ListView) findViewById(R.id.lvONhiem);

        dsDiaDiem = new ArrayList<>();

        for (int i =0;i<10;i++){
            DiaDiem dd = new DiaDiem("Nguyen Van "+i,"Quan: "+i,"12/7/2017",50,50,5);
            dd.setHinhAnh(R.drawable.maybay);
            dsDiaDiem.add(dd);
        }
            adapter = new ListViewAdapter(TinTuc.this,
                                            R.layout.item_listview,dsDiaDiem);
        lvKetXe.setAdapter(adapter);
        lvONhiem.setAdapter(adapter);


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
}
