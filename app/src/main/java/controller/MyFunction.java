package controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.sky.environment.R;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Config;

/**
 * Created by Sky on 24/06/2017.
 */

public class MyFunction {
    public static void makeToastErro(Activity manhinh, String alert, String erro) {
        Toast.makeText(manhinh, "LOI" + alert, Toast.LENGTH_LONG).show();
        Log.e("LOI", erro);
    }

    public static void makeToastTest(Activity manhinh, String alert, String content) {
        Toast.makeText(manhinh, "Kiem tra" + alert, Toast.LENGTH_LONG).show();
        Log.e("AAA", content);
    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    public static boolean isMatch(String pa,String text){
        Pattern pattern = Pattern.compile(pa);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
    public static int testString(String s) {
        String text = s.trim();

        if (text.length() < 10) return Config.HAVE_NO_WORD;
        if (isMatch(Config.PATTERN_HAVE_NUMBER,text)) return Config.HAVE_NUMBER;
        if (isMatch(Config.PATTERN_HAVE_SPECIAL_CHAR,text)) return Config.HAVE_SPECIAL_CHAR;

        return Config.SUCCESS;
    }

    public static void showDialog(Context mContext, String content) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        // Setting Dialog Title
        // Setting Dialog Message
        alertDialog.setTitle("Thông Báo ");
        alertDialog.setMessage(content);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_maybay);
        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.queston_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public static String testUnicode(String query) {

        if(Arrays.asList(R.array.duong).contains(query) ||
                Arrays.asList(R.array.quan).contains(query)){
            return query;
        }
        else{
        }
        return "";

    }
}
