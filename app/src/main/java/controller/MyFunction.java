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

    public static boolean testString(String s) {
        String text = s.trim();
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(s);
        if (text.length() < 5 || matcher.matches()) {
            return false;
        }
        return true;
    }

    public static void showDialog(Context mContext, String icon, String content) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);

        // Setting Dialog Title
        alertDialog.setTitle(icon);
        // Setting Dialog Message
        alertDialog.setMessage(content);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_maybay);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.queston_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                boolean res = true;
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.question_No, new DialogInterface.OnClickListener() {
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
