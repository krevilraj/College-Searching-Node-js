package com.example.collegeselector.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.collegeselector.CollegeDescriptionActivity;
import com.example.collegeselector.R;
import com.example.collegeselector.model.CollegeReview;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>  {
    private Context context;
    private List<CollegeReview> items;

    public ReviewAdapter(Context context, List<CollegeReview> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CollegeReview item = items.get(position);

        holder.tvName.setText(item.getUser());
        holder.tvDesc.setText(item.getReview());
        holder.rating.setRating(item.getRating());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDesc;
        RatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            rating = itemView.findViewById(R.id.ratingbar);
        }
    }
    public void clear_data() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
}
