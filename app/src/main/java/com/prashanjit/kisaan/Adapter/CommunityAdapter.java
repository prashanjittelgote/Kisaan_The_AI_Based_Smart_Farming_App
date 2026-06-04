package com.prashanjit.kisaan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.prashanjit.kisaan.R;
import com.prashanjit.kisaan.POPJO.CommunityModel;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {

    Context context;
    ArrayList<CommunityModel> list;

    public CommunityAdapter(Context context, ArrayList<CommunityModel> list) {
        this.context = context;
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCity, tvCompany, tvCharge;
        Button btnCall, btnWhatsapp;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvCharge = itemView.findViewById(R.id.tvCharge);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnWhatsapp = itemView.findViewById(R.id.btnWhatsapp);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_community, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CommunityModel model = list.get(position);

        holder.tvName.setText(model.getName());
        holder.tvCity.setText("City: " + model.getCity());
        holder.tvCompany.setText("Company: " + model.getConame());
        holder.tvCharge.setText("Charge: ₹" + model.getCharge());

        // Call Button
        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + model.getMobileno()));
            context.startActivity(intent);
        });

        // WhatsApp Button
        holder.btnWhatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/91" + model.getMobileno();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}