package com.example.thewitcherapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BestiaryFragment extends Fragment {
    private ViewPager2 viewPager;
    private FirebaseFirestore db;
    private final List<CardItem> cardItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bestiary, container, false);
        viewPager = view.findViewById(R.id.viewPager_bestiary);
        db = FirebaseFirestore.getInstance();

        ImageView arrowLeft = view.findViewById(R.id.arrowLeft_bestiary);
        ImageView arrowRight = view.findViewById(R.id.arrowRight_bestiary);

        arrowLeft.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        arrowRight.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < cardItems.size() - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        loadCards();
        return view;
    }

    private void loadCards() {
        cardItems.clear();

        db.collection("bestiary").get().addOnSuccessListener(snapshot -> {
            for (QueryDocumentSnapshot doc : snapshot) {
                String id = doc.getId();
                String title = doc.getString("title");
                String imageUrl = doc.getString("imageUrl");
                cardItems.add(new CardItem(id, title, imageUrl));
            }
            viewPager.setAdapter(new CardPagerAdapter(cardItems, requireActivity(), 2));
        });
    }
}
