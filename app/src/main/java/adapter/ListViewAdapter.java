package adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sky.environment.R;

import java.util.List;

import model.DiaDiem;

/**
 * Created by Sky on 10/07/2017.
 */

public class ListViewAdapter extends ArrayAdapter {
    @NonNull
    Activity context;
    @LayoutRes int resource;
    @NonNull List objects;
    public ListViewAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = this.context.getLayoutInflater();

        View view = layoutInflater.inflate(resource,null);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linearStar);
        DiaDiem diadiem = (DiaDiem) objects.get(position);
        TextView txtDuong= (TextView) view.findViewById(R.id.txtDuong);
        TextView txtQuan= (TextView) view.findViewById(R.id.txtQuan);
        TextView txtThoiGian= (TextView) view.findViewById(R.id.txtThoiGian);
        ImageView imgHinh = (ImageView) view.findViewById(R.id.imgHinh);

//        imgHinh.setImageResource(R.drawable.duong);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        for(int i=0;i< diadiem.getMucDo();i++){
            ImageView btn = new ImageView(context);
            btn.setImageResource(R.mipmap.ic_star);
            btn.setLayoutParams(layoutParams); // hien len button
            linear.addView(btn);
        }
        txtDuong.setText(diadiem.getTenDuong());
       txtQuan.setText(diadiem.getKhuVuc());
        txtThoiGian.setText(diadiem.getThoiGianBatDau());
        return view;
    }
}
