package com.birutekno.battendance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birutekno.battendance.R;
import com.birutekno.battendance.model.DataHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by No Name on 7/31/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    public ArrayList<DataHistory> data;
    Context context;

    public HistoryAdapter(ArrayList<DataHistory> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.nama.setText(data.get(i).getNama());
        viewHolder.jam.setText(data.get(i).getJam());
        try {
//            Picasso.get().load(data.get(i).getFoto()).fit().centerCrop().into(viewHolder.img_profile);
            Picasso.with(context).load("http://pronksiapartments.ee/wp-content/uploads/2015/10/placeholder-face-big.png").fit().centerCrop().into(viewHolder.img_profile);
        }catch (Exception ex){
            Log.d("ERROR_MSG", "onBindViewHolder: " + ex.getMessage());
        }
        if (data.get(i).getAction().equals("masuk")){
            viewHolder.action.setText(viewHolder.text_masuk);
            viewHolder.tanggal.setVisibility(View.VISIBLE);
            viewHolder.tanggal.setText(data.get(i).getTanggal());
        }else {
            viewHolder.action.setText(viewHolder.text_pulang);
            viewHolder.tanggal.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_profile;
        private TextView nama;
        private TextView action;
        private TextView jam;
        private TextView tanggal;

        String text_masuk = "Absen Masuk Kantor";
        String text_pulang = "Absen Pulang Kantor";

        public ViewHolder(final View view) {
            super(view);

            img_profile = view.findViewById(R.id.img_profile);
            nama = view.findViewById(R.id.nama);
            action = view.findViewById(R.id.action);
            jam = view.findViewById(R.id.jam);
            tanggal = view.findViewById(R.id.tanggal);

        }
    }
}
