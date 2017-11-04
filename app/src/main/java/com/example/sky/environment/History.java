package com.example.sky.environment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import controller.TinyDB;
import model.Config;

public class History extends AppCompatActivity {
    ListView lvHistory;
    Context mContext = History.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistory = (ListView) findViewById(R.id.lvHistory);
        TinyDB tinyDB = new TinyDB(mContext);
        List<String> list = tinyDB.getListString(Config.listHistory);
        Log.e("Array", Arrays.toString(list.toArray()));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,list);
        lvHistory.setAdapter(adapter);
        lvHistory.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            String address=list.get(position);
            intent.putExtra(Config.TENDUONG, address);
            startActivity(intent);
        });
    }
}
