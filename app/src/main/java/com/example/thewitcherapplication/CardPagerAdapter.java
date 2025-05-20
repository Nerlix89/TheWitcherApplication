package com.example.thewitcherapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CardPagerAdapter extends RecyclerView.Adapter<CardPagerAdapter.ViewHolder> {

    private final List<CardItem> items;
    private final FragmentActivity activity;

    public CardPagerAdapter(List<CardItem> items, FragmentActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        CardItem item = items.get(pos);
        holder.title.setText(item.title);
        Glide.with(holder.image.getContext()).load(item.imageUrl).into(holder.image);

        //holder.itemView.setOnClickListener(v -> {
            //Fragment detail = new LocationDetailFragment();
            //Bundle args = new Bundle();
            //args.putString("location_id", item.id);
            //detail.setArguments(args);

            //activity.getSupportFragmentManager()
                    //.beginTransaction()
                    //.replace(R.id.fragment_container, detail)
                    //.addToBackStack(null)
                    //.commit();
        //});
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cardImage);
            title = itemView.findViewById(R.id.cardTitle);
        }
    }
}


