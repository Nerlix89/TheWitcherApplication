package com.example.thewitcherapplication;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MonsterPagerAdapter extends RecyclerView.Adapter<MonsterPagerAdapter.ViewHolder> {

    private final List<MonsterEntry> entries;

    public MonsterPagerAdapter(List<MonsterEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monster_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        MonsterEntry entry = entries.get(pos);
        holder.title.setText(entry.title);
        holder.quote.setText(entry.quote);
        holder.quoteAuthor.setText(entry.quoteAuthor);
        holder.description.setText(entry.description);
        Glide.with(holder.image.getContext()).load(entry.imageUrl).into(holder.image);

        setImage(holder.image01, entry.image01);
        setImage(holder.image02, entry.image02);
        setImage(holder.image03, entry.image03);
        setImage(holder.image04, entry.image04);

        if (!TextUtils.isEmpty(entry.image01) || !TextUtils.isEmpty(entry.image02) ||
                !TextUtils.isEmpty(entry.image03) || !TextUtils.isEmpty(entry.image04)) {
            holder.extraImagesContainer.setVisibility(View.VISIBLE);
        } else {
            holder.extraImagesContainer.setVisibility(View.GONE);
        }

    }

    private void setImage(ImageView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            view.setVisibility(View.VISIBLE);
            Glide.with(view.getContext()).load(url).into(view);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, quote, description, quoteAuthor;
        ImageView image, image01, image02, image03, image04;
        View extraImagesContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.monsterTitle);
            quoteAuthor = itemView.findViewById(R.id.quoteAuthor);
            quote = itemView.findViewById(R.id.monsterQuote);
            description = itemView.findViewById(R.id.monsterDesc);
            image = itemView.findViewById(R.id.monsterImage);
            image01 = itemView.findViewById(R.id.image01);
            image02 = itemView.findViewById(R.id.image02);
            image03 = itemView.findViewById(R.id.image03);
            image04 = itemView.findViewById(R.id.image04);
            extraImagesContainer = itemView.findViewById(R.id.extraImagesContainer);
        }
    }
}
