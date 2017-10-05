package com.example.sky.environment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageButton;

import map.GPSTracker;

public class Future extends AppCompatActivity {
    GPSTracker gps;
    Intent intent;
    ImageButton btnComment,btnInviteFriend,btnGrade,btnGift,btnPerson,btnStar,btnSupport;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(Future.this,TinTuc.class);
//                    Log.e("AAA",loca+"");
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(Future.this,ThongTin.class);
//                    Log.e("AAA",loca+"");
//                    if(location != null) {
//                    }
//                    else{
//                        gps = new GPSTracker(Future.this);
//                        gps.showSettingsAlert();
//                    }
//                    return true;
                case R.id.menu:
                    intent = new Intent(Future.this,TinTuc.class);
//                    Log.e("AAA",loca+"");
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
