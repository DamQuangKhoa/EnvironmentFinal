package com.example.sky.environment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import fragment.FragmentFirst;
import fragment.FragmentSecond;
import fragment.FragmentThird;
import model.Config;

public class GioiThieu extends AppCompatActivity {
    FragmentManager fragmentManager;
    ImageButton btnNext,btnPrevious,btnFinal;


    int current=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu);

        fragmentManager = getFragmentManager();

        addControl();
        addEvent();
        addTransaction(current);
    }

    private void addEvent() {
       btnNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(current < Config.COUNT){
                   current++;
                addTransaction(current);
               }
               else{
              Intent intent = new Intent(GioiThieu.this,Start.class);
                   startActivity(intent);
               }
           }
       });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentManager.getBackStackEntryCount()>0) {
                    fragmentManager.popBackStack();
                    current--;
                }
                else {
                    current++;
                    addTransaction(current);
                }
            }
        });
        btnFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GioiThieu.this,Start.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() >0){
            fragmentManager.popBackStack();

        }
        else{
            super.onBackPressed();}
    }

    private void addControl() {
        btnFinal = (ImageButton) findViewById(R.id.btnFinal);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
    }


    public void addTransaction(int type){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment recent= null;
        String nameOfFragment = "";
        switch (type){
            case Config.FIRST:
                recent = new FragmentFirst();
                nameOfFragment = Config.FIRSTNAME;
                break;
            case Config.SECOND:
                recent = new FragmentSecond();
                nameOfFragment = Config.SECONDNAME;
                break;
            case Config.THIRD:
                recent = new FragmentThird();
                nameOfFragment = Config.THIRDNAME;
                break;
        }
        fragmentTransaction.add(R.id.content,recent,nameOfFragment);
        fragmentTransaction.addToBackStack(nameOfFragment);
        fragmentTransaction.commit();
    }

}
