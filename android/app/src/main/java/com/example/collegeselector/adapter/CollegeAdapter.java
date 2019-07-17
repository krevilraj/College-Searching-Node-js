package com.example.collegeselector.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.collegeselector.CollegeDescriptionActivity;
import com.example.collegeselector.R;
import com.example.collegeselector.model.College;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ClothViewHolder> {
    Context mContext;
    List<College> collegeList;

    public CollegeAdapter(Context mContext, List<College> contactsList) {
        this.mContext = mContext;
        this.collegeList = contactsList;
    }

    @NonNull
    @Override
    public ClothViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemlist, viewGroup, false);
        return new ClothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothViewHolder clothViewHolder, int i) {
        final College contacts = collegeList.get(i);
        clothViewHolder.imgProfile.setImageResource(contacts.getImage());
        clothViewHolder.tvName.setText(contacts.getName());
        clothViewHolder.tvPrice.setText("Nrs. "+contacts.getPrice());

        clothViewHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CollegeDescriptionActivity.class);
                intent.putExtra("image",contacts.getImage());
                intent.putExtra("name",contacts.getName());
                intent.putExtra("price",contacts.getPrice());
                intent.putExtra("desc",contacts.getDesc());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return collegeList.size();
    }



    public class ClothViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfile;
        TextView tvName, tvPrice,tvDesc;
        public ClothViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
