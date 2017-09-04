package controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sky on 24/06/2017.
 */

public class MyFunction {
    public static void makeToastErro(Activity manhinh,String alert,String erro){
        Toast.makeText(manhinh,"LOI"+alert,Toast.LENGTH_LONG).show();
        Log.e("LOI",erro);
    }
    public static void makeToastTest(Activity manhinh,String alert,String content){
        Toast.makeText(manhinh,"Kiem tra"+alert,Toast.LENGTH_LONG).show();
        Log.e("AAA",content);
    }

}
