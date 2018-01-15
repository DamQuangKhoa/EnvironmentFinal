package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sky.environment.R;

import java.util.ArrayList;
import java.util.List;

import controller.RecyclerViewClickListener;
import model.DiaDiem;

/**
 * Created by Sky on 28/08/2017.
 */

public class DiaDiemAdapter extends RecyclerView.Adapter<DiaDiemAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<DiaDiem> listDiaDiem;
    private RecyclerViewClickListener mListener;
    private Filter diaDiemFilter = new DiaDiemFilter();
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
public void makeLog(String message){
    Log.e("DIADIEMADAPTER",message);
}
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiaDiem dd=listDiaDiem.get(position);
        makeLog(dd+"");
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        for(int i = 0; i< dd.getMucDoInt(); i++){
            ImageView imageView = new ImageView(mContext);
//            imageView.setImageResource(R.mipmap.ic_star);
            imageView.setLayoutParams(layoutParams); // hien len button
            holder.relate.addView(imageView);
        }
        switch (dd.getMucDoInt()){
            case 1:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#08f204"));
                break;
            case 2:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#f7f307"));
                break;
            case 3:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#f2aa02"));
                break;
            case 4:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#b53203"));
                break;
            case 5:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#f24102"));
                break;
            default:
                holder.txtMucDo.setBackgroundColor(Color.parseColor("#f24102"));
                break;
        }
        holder.txtDuong.setText(dd.getTenDuong());
        holder.txtQuan.setText(dd.getKhuVuc());
        holder.txtThoiGian.setText(dd.getThoiGianBatDau());
        String hinhanh = dd.getHinhAnh();
        Bitmap bm;
       if("".equalsIgnoreCase(hinhanh)){
           holder.imgHinh.setImageResource(R.drawable.ketxe);
       }
       else {
           if ((bm=convertFrom64ToBit(hinhanh)) != null) {
               holder.imgHinh.setImageBitmap(bm);
           }
       }
    }
    public Bitmap convertFrom64ToBit(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    @Override
    public int getItemCount() {
        return listDiaDiem.size();
    }

    @Override
    public Filter getFilter() {
        return diaDiemFilter;
    }

    class DiaDiemFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                // No filter implemented we return all the list
                results.values = listDiaDiem;
                results.count = listDiaDiem.size();
            } else {
                // We perform filtering operation
                List<DiaDiem> diaDiemList = new ArrayList<>();
                for (DiaDiem p : listDiaDiem) {
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
            if (filterResults.count == 0){

            }
            else {
                listDiaDiem = (List<DiaDiem>) filterResults.values;
                notifyDataSetChanged();
            }
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
//        LinearLayout linear;
        RelativeLayout relate;
        DiaDiem diadiem;
        TextView txtDuong,txtQuan,txtThoiGian,txtMucDo;
        ImageView imgHinh;
        RecyclerViewClickListener mListener;

        public MyViewHolder(View view,RecyclerViewClickListener mListener) {
            super(view);
           relate = (RelativeLayout) view.findViewById(R.id.relative);
          txtDuong= (TextView) view.findViewById(R.id.txtDuong);
//           txtQuan= (TextView) view.findViewById(R.id.txtQuan);
//          txtThoiGian= (TextView) view.findViewById(R.id.txtThoiGian);
            txtMucDo = (TextView) view.findViewById(R.id.txtMucDo);
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
