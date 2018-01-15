package com.example.sky.environment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Config;
import model.DiaDiemChinh;

public class ThongTin extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_CROP = 400;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0 ;
    EditText editTime, editDate;
    TextView txtLocality,txtKhuVuc;
    ImageButton btnTime,btnDate,btnUpload,btnGui,btnTakePic;
    ImageView imgView;
    RadioButton radCurrent,radOther;
    RadioButton radONhiem,radKetXe;
    AutoCompleteTextView autoDuong,autoTinhThanh;
    Spinner spKhuVuc,spMucDo,spThanhPho,spPhuong,spDuong;
    ArrayAdapter<String> duongAdapter,khuVucAdapter,mucDoAdapter,thanhPhoAdapter,phuongAdapter;
    String[] dsDuong,dsKhuVuc,dsMucDo,dsThanhPho,dsPhuong;
    Calendar calendar;
    SimpleDateFormat sdf1,sdf2;
    String base64="";
    Geocoder geocoder;
    static int lastItemKhuVuc =0,lastItemMucDo=0,lastItemDuong=0,lastItemPhuong=0,lastItemTinhThanh=0;
    DiaDiemChinh diaDiem;
    boolean sendSuccess= false,isOrder=false;
    double lat,lon;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);

        xuLyNhanDuLieu();
        addControls();
        addEvents();


    }
    public  Address getLocationFromAddress(String strAddress){
        List<Address> address= null;
        LatLng p1 = null;
        Random rd = new Random();
        try {
            // May throw an IOException
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address == null && address.isEmpty()) {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return address.get(rd.nextInt((address.size())));
    }
    private void xuLyNhanDuLieu() {
        Intent intent = getIntent();
        lat=intent.getDoubleExtra(Config.LAT,-1);
        lon =intent.getDoubleExtra(Config.LONG,-1);

    }

    private void addEvents() {
        btnTime.setOnClickListener(v -> xuLyTime());
        btnDate.setOnClickListener(v -> xuLyDate());
        btnGui.setOnClickListener(v -> xulyGui());
        btnUpload.setOnClickListener(v -> xuLyAnh());
        radCurrent.setOnClickListener(v-> xuLyCurrentLocal());
        radOther.setOnClickListener(v-> xuLuOtherLocal());
        btnTakePic.setOnClickListener(v-> xuLyChupAnh());
    }

    private void xuLyChupAnh() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }


    private void xuLuOtherLocal() {
        isOrder = true;

        txtKhuVuc.setVisibility(View.GONE);
        txtLocality.setVisibility(View.GONE);
        spDuong.setVisibility(View.GONE);
        autoDuong.setVisibility(View.VISIBLE);
        autoTinhThanh.setVisibility(View.VISIBLE);
        spKhuVuc.setVisibility(View.GONE);
        spPhuong.setVisibility(View.GONE);
        spThanhPho.setVisibility(View.GONE);
       dsDuong = getResources().getStringArray(R.array.duong);
         dsThanhPho = getResources().getStringArray(R.array.tinh);

        duongAdapter=checkAdapter(duongAdapter,dsDuong);
        thanhPhoAdapter=checkAdapter(thanhPhoAdapter,dsThanhPho);
        autoDuong.setAdapter(duongAdapter);
        spMucDo.setAdapter(mucDoAdapter);
        autoTinhThanh.setAdapter(thanhPhoAdapter);
    }

    private void xuLyCurrentLocal() {
        isOrder =false;
        autoDuong.setVisibility(View.GONE);
        spDuong.setVisibility(View.VISIBLE);
        txtLocality.setVisibility(View.VISIBLE);
        spPhuong.setVisibility(View.VISIBLE);
        try {
            List<Address>  addresses = geocoder.getFromLocation(lat, lon, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            int size;
            if(null != addresses &&(size= addresses.size())>0){
                dsDuong = new String[size];
                dsKhuVuc = new String[size];
                dsPhuong = new String[size];
                dsThanhPho = new String[size];

                int count=0;
                for (Address a:
                        addresses) {
                    dsDuong[count] =a.getAddressLine(0)==null?" ":a.getAddressLine(0);
                    dsKhuVuc[count] =a.getSubAdminArea()==null?" ":a.getSubAdminArea();
                    dsPhuong[count] =a.getLocality()==null?" ":a.getLocality();
                    dsThanhPho[count] =a.getAdminArea()==null?" ":a.getAdminArea();
                    count++;
                    Log.e("address",count+"");
                }
                khuVucAdapter=checkAdapter(khuVucAdapter,dsKhuVuc);
                duongAdapter=checkAdapter(duongAdapter,dsDuong);
                thanhPhoAdapter=checkAdapter(thanhPhoAdapter,dsThanhPho);
                phuongAdapter=checkAdapter(phuongAdapter,dsPhuong);
                spPhuong.setAdapter(phuongAdapter);
                spThanhPho.setAdapter(thanhPhoAdapter);
                spKhuVuc.setAdapter(khuVucAdapter);
                spDuong.setAdapter(duongAdapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        ImageView imageView = (ImageView) findViewById(R.id.imgView);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                base64 = imgToBase64(bmp);
                performCrop(picturePath);
//                Log.e("base64",base64);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imgView.setImageBitmap(bmp);
        }
        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                Bitmap image = getResizedBitmap(selectedBitmap,100,100);
                imgView.setImageBitmap(image);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode == Activity.RESULT_OK) {
            //use imageUri here to access the image
            try {
                Bitmap bmp = getBitmapFromUri(imageUri);
                base64 = imgToBase64(bmp);
                imgView.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
    private void performCrop(String picUri) {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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
            String tenDuong="", khuVuc="", mucDo, thoiGian, ngayThang,phuong="",tinhThanh="";
            String ketXe;
            if (radKetXe.isChecked()) {
                ketXe = "Kẹt Xe";
            } else {
                ketXe = "Ô Nhiễm";
            }
            spDuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    lastItemDuong = i;
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            spPhuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    lastItemPhuong = i;
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
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
        spThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastItemTinhThanh = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        tenDuong =isOrder? autoDuong.getText().toString():dsDuong[lastItemDuong];
        tinhThanh =isOrder? autoTinhThanh.getText().toString():dsThanhPho[lastItemTinhThanh];
        khuVuc = dsKhuVuc[lastItemKhuVuc];
        phuong = dsPhuong[lastItemPhuong];
        mucDo = dsMucDo[lastItemMucDo];

        ngayThang = editDate.getText().toString();
        thoiGian = ngayThang + " " + editTime.getText().toString();
        if(isOrder && !"".equalsIgnoreCase(tenDuong.trim())&& !isContainNumber(tenDuong)){
            Address adddress=getLocationFromAddress(tenDuong+", "+tinhThanh);
            if(null != adddress) {
                tenDuong = adddress.getAddressLine(0);
                khuVuc = adddress.getSubAdminArea();
                phuong = adddress.getLocality();
                tinhThanh = adddress.getAdminArea();
                lat = adddress.getLatitude();
                lon = adddress.getLongitude();
            }
        }
        sendSuccess = kiemTra(tenDuong,khuVuc,tinhThanh,phuong);
        if (!sendSuccess) {
            autoDuong.setBackgroundColor(Color.RED);
            }
        else{
            diaDiem = new DiaDiemChinh(tenDuong, khuVuc, mucDo, ketXe, thoiGian, lat, lon, base64,tinhThanh,phuong);
                guiDuLieuWeb(diaDiem);
            Toast.makeText(this, "Gửi Thành Công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

            }
    }
    private boolean isContainNumber(String data){
       return data.matches(".*\\d+.*");
    }
private boolean kiemTra(String... data){
    Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
    int count =0;
    for (String t :
            data) {
        Matcher matcher = pattern.matcher(t);
        if(t == null || t.trim().equals("") ){
            return false;
        }
    }
        return true;
}
    private void guiDuLieuWeb(DiaDiemChinh diaDiem) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL, response -> makeLog(response),
                error -> makeLog(error.getMessage())){
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
                params.put(Config.TINHTHANH,diaDiem.getTinhThanh());
                params.put(Config.PHUONG,diaDiem.getPhuong());
                return params;
            }
        };
        makeLog(stringRequest+"");
        requestQueue.add(stringRequest);

    }

    private void xuLyDate() {
        DatePickerDialog.OnDateSetListener callback = (datePicker, year, month, day) -> {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
            editDate.setText(sdf1.format(calendar.getTime()));
        };

        DatePickerDialog dpdl = new DatePickerDialog(
                ThongTin.this,
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpdl.show();
    }

    private void xuLyTime() {
        TimePickerDialog.OnTimeSetListener callBack = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR,hour-1);
                calendar.set(Calendar.MINUTE,minute);

                editTime.setText(sdf2.format(calendar.getTime()));
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
        txtLocality = (TextView) findViewById(R.id.txtLocal);
        txtKhuVuc = (TextView) findViewById(R.id.txtKhuVuc);
        editTime = (EditText) findViewById(R.id.txtTime);
        editDate = (EditText) findViewById(R.id.txtDate);
        btnTime = (ImageButton) findViewById(R.id.btnTime);
        btnDate = (ImageButton) findViewById(R.id.btnDate);
        btnGui = (ImageButton) findViewById(R.id.btnGui);
        radKetXe = (RadioButton) findViewById(R.id.radKetXe);
        radONhiem = (RadioButton) findViewById(R.id.radONhiem);
        radCurrent = (RadioButton) findViewById(R.id.radCurrent);
        radOther = (RadioButton) findViewById(R.id.radOther);
        autoDuong = (AutoCompleteTextView) findViewById(R.id.autoDuong);
        autoTinhThanh = (AutoCompleteTextView) findViewById(R.id.autoTinhThanh);
        spKhuVuc = (Spinner) findViewById(R.id.spKhuVuc);
        spDuong = (Spinner) findViewById(R.id.spDuong);
        spMucDo = (Spinner) findViewById(R.id.spMucDo);
        spThanhPho = (Spinner) findViewById(R.id.spThanhPho);
        spPhuong= (Spinner) findViewById(R.id.spPhuong);
        dsMucDo = getResources().getStringArray(R.array.mucdo);
        btnUpload = (ImageButton) findViewById(R.id.btnUpload);
        btnTakePic = (ImageButton) findViewById(R.id.btnTakePic);
        imgView = (ImageView) findViewById(R.id.imgView);
        geocoder = new Geocoder(this);
        sdf1 = new SimpleDateFormat(Config.sdfDate);
        sdf2 = new SimpleDateFormat(Config.sdfTime);
        mucDoAdapter = new ArrayAdapter<>(
                ThongTin.this,
                android.R.layout.simple_list_item_1,
                dsMucDo
        );
        spMucDo.setAdapter(mucDoAdapter);
        editDate.setText(sdf1.format(calendar.getTime()));
        editTime.setText(sdf2.format(calendar.getTime()));


            autoDuong.setVisibility(View.GONE);
            autoTinhThanh.setVisibility(View.GONE);
            spDuong.setVisibility(View.VISIBLE);
            txtLocality.setVisibility(View.VISIBLE);
            spPhuong.setVisibility(View.VISIBLE);
            try {
                List<Address>  addresses = geocoder.getFromLocation(lat, lon, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                int size;
                if(null != addresses &&(size= addresses.size())>0){
                  dsDuong = new String[size];
                    dsKhuVuc = new String[size];
                    dsPhuong = new String[size];
                     dsThanhPho = new String[size];

                    int count=0;
                    for (Address a:
                            addresses) {
                        dsDuong[count] =a.getAddressLine(0)==null?" ":a.getAddressLine(0);
                        dsKhuVuc[count] =a.getSubAdminArea()==null?" ":a.getSubAdminArea();
                        dsPhuong[count] =a.getLocality()==null?" ":a.getLocality();
                        dsThanhPho[count] =a.getAdminArea()==null?" ":a.getAdminArea();
                        count++;
                    }
                    khuVucAdapter=checkAdapter(khuVucAdapter,dsKhuVuc);
                    duongAdapter=checkAdapter(duongAdapter,dsDuong);
                    thanhPhoAdapter=checkAdapter(thanhPhoAdapter,dsThanhPho);
                    phuongAdapter=checkAdapter(phuongAdapter,dsPhuong);


                    spPhuong.setAdapter(phuongAdapter);
                    spThanhPho.setAdapter(thanhPhoAdapter);
                    spKhuVuc.setAdapter(khuVucAdapter);
                    spDuong.setAdapter(duongAdapter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
    public void makeLog(String message){
        Log.e("THONGTIN",message);
    }
    public ArrayAdapter checkAdapter(ArrayAdapter adapt,String[] ds){
        if(adapt == null) {
            adapt = new ArrayAdapter<>(
                    ThongTin.this,
                    android.R.layout.simple_list_item_1
            );
        }
        else{
            adapt.clear();
        }
        adapt.addAll(Arrays.asList(ds));
        adapt.notifyDataSetChanged();
        adapt.setDropDownViewResource(android.R.layout.simple_list_item_1);
        return adapt;
    }
}
