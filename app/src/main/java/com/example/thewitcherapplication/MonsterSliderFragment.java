package com.example.thewitcherapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MonsterSliderFragment extends Fragment {

    private ViewPager2 viewPager;
    private List<MonsterEntry> monsterList = new ArrayList<>();
    private MonsterPagerAdapter adapter;
    private FirebaseFirestore db;
    private String bestiaryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monster_slider, container, false);

        ImageButton closeButton = view.findViewById(R.id.closeButton_moster_fragment);
        closeButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        viewPager = view.findViewById(R.id.monsterViewPager);
        adapter = new MonsterPagerAdapter(monsterList);
        viewPager.setAdapter(adapter);

        if (getArguments() != null) {
            bestiaryId = getArguments().getString("bestiary_id");
            loadMonsters(bestiaryId);
        }

        return view;
    }

    private void loadMonsters(String bestiaryId) {
        db = FirebaseFirestore.getInstance();
        db.collection("bestiary")
                .document(bestiaryId)
                .collection("monsters")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (QueryDocumentSnapshot doc : snapshot) {
                        MonsterEntry entry = new MonsterEntry();
                        entry.title = doc.getString("title");
                        entry.quoteAuthor = doc.getString("quoteAuthor");
                        entry.quote = doc.getString("quote");
                        entry.description = doc.getString("description");
                        entry.imageUrl = doc.getString("imageUrl");
                        entry.image01 = doc.getString("image01");
                        entry.image02 = doc.getString("image02");
                        entry.image03 = doc.getString("image03");
                        entry.image04 = doc.getString("image04");
                        monsterList.add(entry);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}

