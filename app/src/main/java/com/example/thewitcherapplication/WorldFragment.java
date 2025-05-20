package com.example.thewitcherapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldFragment extends Fragment {

    private ViewPager2 viewPager;
    private List<CardItem> cardItems = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_world, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        db = FirebaseFirestore.getInstance();

        loadCards();

        return view;
    }

    private void loadCards() {
        cardItems.clear();

        db.collection("locations").get().addOnSuccessListener(snapshot -> {
            for (QueryDocumentSnapshot doc : snapshot) {
                String id = doc.getId();
                String title = doc.getString("title");
                String imageUrl = doc.getString("imageUrl");
                cardItems.add(new CardItem(id, title, imageUrl));
            }

            CardPagerAdapter adapter = new CardPagerAdapter(cardItems, requireActivity());
            viewPager.setAdapter(adapter);

            viewPager.setOffscreenPageLimit(3);
            viewPager.setClipChildren(false);
            viewPager.setClipToPadding(false);

            CompositePageTransformer transformer = new CompositePageTransformer();
            transformer.addTransformer((page, pos) -> {
                float scale = 0.85f + (1 - Math.abs(pos)) * 0.15f;
                page.setScaleY(scale);
            });
            viewPager.setPageTransformer(transformer);
        });
    }

}


