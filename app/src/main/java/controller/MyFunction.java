package controller;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Sky on 24/06/2017.
 */

public class MyFunction {
    public static void makeToast(Activity manhinh,String text){
        Toast.makeText(manhinh,text,Toast.LENGTH_LONG).show();
    }

}
