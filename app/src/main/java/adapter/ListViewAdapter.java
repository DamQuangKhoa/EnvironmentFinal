/*
package adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sky.environment.R;

import java.util.ArrayList;
import java.util.List;

import model.DiaDiem;

*/
/**
 * Created by Sky on 10/07/2017.
 *//*


public class ListViewAdapter extends ArrayAdapter implements Filterable {
    @NonNull
    Activity context;
    @LayoutRes
    int resource;
    @NonNull
    List<DiaDiem> objects;

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

        View view = layoutInflater.inflate(resource, null);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.relative);
        DiaDiem diadiem = (DiaDiem) objects.get(position);
        TextView txtDuong = (TextView) view.findViewById(R.id.txtDuong);
//        TextView txtQuan = (TextView) view.findViewById(R.id.txtQuan);
//        TextView txtThoiGian = (TextView) view.findViewById(R.id.txtThoiGian);
        ImageView imgHinh = (ImageView) view.findViewById(R.id.imgHinh);

//        imgHinh.setImageResource(R.drawable.duong);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        for (int i = 0; i < diadiem.getMucDoInt(); i++) {
            ImageView imageView = new ImageView(context);
//            imageView.setImageResource(R.mipmap.ic_star);
            imageView.setLayoutParams(layoutParams); // hien len button
            linear.addView(imageView);
        }
        Log.e("AAA", diadiem.getMucDoInt() + "");
        switch (diadiem.getMucDoInt()) {
            case 1:
                linear.setBackgroundColor(Color.parseColor("#08f204"));
                break;
            case 2:
                linear.setBackgroundColor(Color.parseColor("#f7f307"));
                break;
            case 3:
                linear.setBackgroundColor(Color.parseColor("#f2aa02"));

                break;
            case 4:
                linear.setBackgroundColor(Color.parseColor("#b53203"));
                break;
            case 5:
                linear.setBackgroundColor(Color.parseColor("#f24102"));
                break;
            default:
                linear.setBackgroundColor(Color.parseColor("#f24102"));
                break;
        }

        txtDuong.setText(diadiem.getTenDuong());
//        txtQuan.setText(diadiem.getKhuVuc());
//        txtThoiGian.setText(diadiem.getThoiGianBatDau());
        return view;
    }

    class DiaDiemFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                // No filter implemented we return all the list
                results.values = objects;
                results.count = objects.size();
            } else {
                // We perform filtering operation
                List<DiaDiem> diaDiemList = new ArrayList<DiaDiem>();
                for (DiaDiem p : objects) {
                    if (p.getTenDuong().toUpperCase()
                            .startsWith(charSequence.toString().toUpperCase())) {
                        diaDiemList.add(p);
                    }
                }
                results.values = diaDiemList;
                results.count = diaDiemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.count == 0)
                notifyDataSetInvalidated();
            else {
                objects = (List<DiaDiem>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}*/
