package com.example.sky.environment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.Config;
import model.DiaDiemChinh;

public class ThongTin extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    Button btnGui;
    EditText txtTime,txtDate;
    ImageButton btnTime,btnDate,btnUpload;
    ImageView imgView;
    RadioButton radONhiem,radKetXe;
    AutoCompleteTextView autoDuong;
    Spinner spKhuVuc,spMucDo;
    ArrayAdapter<String> duongAdapter,khuVucAdapter,mucDoAdapter;
    String[] dsDuong;
    String[] dsKhuVuc;
    String[] dsMucDo;
    Calendar calendar;
    SimpleDateFormat sdf1,sdf2;
    String base64="";
    int lastItemKhuVuc =0,lastItemMucDo=0;
    DiaDiemChinh diaDiem;
    boolean sendSuccess= false;
    double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);

        addControls();
        addEvents();
        xuLyNhanDuLieu();

    }

    private void xuLyNhanDuLieu() {
        Intent intent = getIntent();
        lat=intent.getDoubleExtra(Config.LAT,-1);
        lon =intent.getDoubleExtra(Config.LONG,-1);
        Toast.makeText(this, "Nhan Du Lieu: "+lat+"_"+lon, Toast.LENGTH_SHORT).show();
    }

    private void addEvents() {
        btnTime.setOnClickListener(v -> xuLyTime());
        btnDate.setOnClickListener(v -> xuLyDate());
        btnGui.setOnClickListener(v -> xulyGui());
        btnUpload.setOnClickListener(v -> xuLyAnh());
    }

    private void xuLyAnh() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                base64 = imgToBase64(bmp);
//                Log.e("base64",base64);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imgView.setImageBitmap(bmp);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public static String imgToBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            // compress image
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void xulyGui() {
        sendSuccess = false;
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
        thoiGian  = ngayThang+" " +txtTime.getText().toString();
//        Log.e("AAA",diaDiem.toString());
        sendSuccess = kiemTra(tenDuong,khuVuc,mucDo,base64);
        if(!sendSuccess) {
            autoDuong.setBackgroundColor(Color.RED);
            btnUpload.setBackgroundColor(Color.RED);
        }
        else {
            diaDiem = new DiaDiemChinh(tenDuong,khuVuc,mucDo,ketXe,thoiGian,lat,lon,base64);
            guiDuLieuWeb(diaDiem);
        }
    }
private boolean kiemTra(String... data){
    for (String t :
            data) {
        if(t == null || t.trim().equals("")){
            return false;
        }
    }
        return true;
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
                params.put(Config.HINHANH,diaDiem.getHinhAnh()+"");

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
        btnUpload = (ImageButton) findViewById(R.id.btnUpload);
        imgView = (ImageView) findViewById(R.id.imgView);
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

        sdf1 = new SimpleDateFormat(Config.sdfDate);
        sdf2 = new SimpleDateFormat(Config.sdfTime);

        txtDate.setText(sdf1.format(calendar.getTime()));
        txtTime.setText(sdf2.format(calendar.getTime()));



//        Log.e("AAA",lat+" "+lon);


    }
}
