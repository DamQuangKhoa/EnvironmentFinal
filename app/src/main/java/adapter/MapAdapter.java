package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sky.environment.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import model.DiaDiem;

/**
 * Created by Sky on 10/07/2017.
 */

public class MapAdapter implements GoogleMap.InfoWindowAdapter{
    private Activity context;
    private DiaDiem diaDiem;
    public MapAdapter(Activity context, DiaDiem diaDiem) {
        this.context = context;
        this.diaDiem = diaDiem;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public DiaDiem getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(DiaDiem diaDiem) {
        this.diaDiem = diaDiem;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view= inflater.inflate(R.layout.item_map,null);
        ImageView imgHinhHienThi = (ImageView) view.findViewById(R.id.imgHinhHienThi);
        TextView txtDuongHienThi = (TextView) view.findViewById(R.id.txtDuongHienThi);
        String hinhanh;
        if((hinhanh=diaDiem.getHinhAnh())!= null){
        imgHinhHienThi.setImageBitmap(convertBase64ToBitmap(hinhanh));
        imgHinhHienThi.setMaxWidth(150);
        imgHinhHienThi.setMaxHeight(150);
        }
        else {
            imgHinhHienThi.setImageResource(R.drawable.ketxe);
        }
        txtDuongHienThi.setText(diaDiem.getTenDuong());


        return view;
    }
    private Bitmap convertBase64ToBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
