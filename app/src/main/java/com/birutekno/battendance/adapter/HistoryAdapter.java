package com.birutekno.battendance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birutekno.battendance.R;
import com.birutekno.battendance.model.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by No Name on 7/31/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    public ArrayList<Data> data;
    Context context;

    public HistoryAdapter(ArrayList<Data> data, Context context) {
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

//        Toast.makeText(context, data.get(i).getNama(), Toast.LENGTH_SHORT).show();
        viewHolder.nama.setText(data.get(i).getNama());
        viewHolder.jam.setText(data.get(i).getJam());
        if (data.get(i).getAction().equals("masuk")){
            viewHolder.action.setText(viewHolder.text_masuk);
            viewHolder.tanggal.setVisibility(View.VISIBLE);
            viewHolder.tanggal.setText(checkDate(data.get(i).getTanggal()));
        }else {
            viewHolder.action.setText(viewHolder.text_pulang);
            viewHolder.tanggal.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String checkDate(String date){
        String dateResult;
        long dateNow = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateStringNow = dateFormat.format(dateNow);
        if (dateStringNow.equals(date)){
            dateResult = "Hari Ini";
        }else {
            dateResult = date;
        }

        return dateResult;
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
