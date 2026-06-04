package com.mountreachsolution.kisaan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.kisaan.POPJO.AgentModel;
import com.mountreachsolution.kisaan.R;
import com.mountreachsolution.kisaan.common.Urls;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ViewHolder> {

    Context context;
    List<AgentModel> list;          // filtered list
    List<AgentModel> fullList;      // original list

    public AgentAdapter(Context context, List<AgentModel> list) {
        this.context = context;
        this.list = list;
        this.fullList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.agent_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AgentModel model = list.get(position);

        holder.tvName.setText(model.getName());
        holder.tvCity.setText("City: " + model.getCity());
        holder.tvPhone.setText("Phone: " + model.getPhone());
        holder.tvEmail.setText("Email: " + model.getEmail());

        // Load image using Glide
        Glide.with(context)
                .load(Urls.imageAddress + model.getImage())
                .placeholder(R.drawable.agent)
                .into(holder.imgAgent);

        // CALL
        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + model.getPhone()));
            context.startActivity(intent);
        });

        // WHATSAPP
        holder.btnWhatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/91" + model.getPhone();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        });
    }

    public void updateList(List<AgentModel> newList) {
        list.clear();
        list.addAll(newList);

        fullList.clear();
        fullList.addAll(newList);

        notifyDataSetChanged();
    }

    // 🔎 FILTER METHOD
    public void filter(String text) {

        list.clear();

        if (text.isEmpty()) {
            list.addAll(fullList);
        } else {

            text = text.toLowerCase();

            for (AgentModel item : fullList) {

                if (item.getName().toLowerCase().contains(text) ||
                        item.getCity().toLowerCase().contains(text)) {

                    list.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCity, tvPhone, tvEmail;
        CircleImageView imgAgent;
        LinearLayout btnCall, btnWhatsapp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imgAgent = itemView.findViewById(R.id.imgAgent);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnWhatsapp = itemView.findViewById(R.id.btnWhatsapp);
        }
    }
}
