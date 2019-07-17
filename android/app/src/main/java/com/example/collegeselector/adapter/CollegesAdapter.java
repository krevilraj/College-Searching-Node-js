package com.example.collegeselector.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.collegeselector.R;
import com.example.collegeselector.CollegeDescriptionActivity;
import com.example.collegeselector.model.Data;

import java.util.List;


public class CollegesAdapter extends RecyclerView.Adapter<CollegesAdapter.ViewHolder>  {
    private Context context;
    private List<Data> items;

    public CollegesAdapter(Context context, List<Data> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Data item = items.get(position);

        holder.tvName.setText(item.getCollege_name());
        holder.tvPrice.setText("Nrs. "+item.getPrice());

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CollegeDescriptionActivity.class);
                intent.putExtra("college_description", item);
                context.startActivity(intent);
            }
        });

        /* Glide.with(context).load(item.getUploadVehiclePath()).into(holder.v_image);*/


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvName, tvPrice,tvDesc;
        Button btn_more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btn_more = itemView.findViewById(R.id.btnMore);

        }
    }

    public void clear_data() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
}
