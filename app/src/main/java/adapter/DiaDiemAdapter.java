package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sky.environment.R;

import java.util.ArrayList;
import java.util.List;

import controller.RecyclerViewClickListener;
import model.DiaDiem;

/**
 * Created by Sky on 28/08/2017.
 */

public class DiaDiemAdapter extends RecyclerView.Adapter<DiaDiemAdapter.MyViewHolder> {
    private Context mContext;
    private List<DiaDiem> listDiaDiem;
    private RecyclerViewClickListener mListener;

    public DiaDiemAdapter(Context mContext, RecyclerViewClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        listDiaDiem = new ArrayList<>();
    }
public void updateData(List<DiaDiem>  list){
    listDiaDiem.clear();
    listDiaDiem.addAll(list);
    notifyDataSetChanged();
}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listview, parent, false);
        return new MyViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiaDiem dd=listDiaDiem.get(position);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        for(int i = 0; i< dd.getMucDoInt(); i++){
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.ic_star);
            imageView.setLayoutParams(layoutParams); // hien len button
            holder.linear.addView(imageView);
        }
        switch (dd.getMucDoInt()){
            case 1:
                holder.linear.setBackgroundColor(Color.parseColor("#08f204"));
                break;
            case 2:
                holder.linear.setBackgroundColor(Color.parseColor("#f7f307"));
                break;
            case 3:
                holder.linear.setBackgroundColor(Color.parseColor("#f2aa02"));

                break;
            case 4:
                holder.linear.setBackgroundColor(Color.parseColor("#b53203"));
                break;
            case 5:
                holder.linear.setBackgroundColor(Color.parseColor("#f24102"));
                break;
            default:
                holder.linear.setBackgroundColor(Color.parseColor("#f24102"));
                break;
        }
        holder.txtDuong.setText(dd.getTenDuong());
        holder.txtQuan.setText(dd.getKhuVuc());
        holder.txtThoiGian.setText(dd.getThoiGianBatDau());


    }

    @Override
    public int getItemCount() {
        return listDiaDiem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        LinearLayout linear;
        DiaDiem diadiem;
        TextView txtDuong,txtQuan,txtThoiGian;
        ImageView imgHinh;
        RecyclerViewClickListener mListener;
        public MyViewHolder(View view,RecyclerViewClickListener mListener) {
            super(view);
           linear = (LinearLayout) view.findViewById(R.id.linearStar);
          txtDuong= (TextView) view.findViewById(R.id.txtDuong);
           txtQuan= (TextView) view.findViewById(R.id.txtQuan);
          txtThoiGian= (TextView) view.findViewById(R.id.txtThoiGian);
            imgHinh = (ImageView) view.findViewById(R.id.imgHinh);
            this.mListener = mListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view,getAdapterPosition());
        }
    }

}