package com.example.thewitcherapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class LocationDetailFragment extends Fragment {

    private TextView titleView, descriptionView;
    private ImageView imageView;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_detail, container, false);
        titleView = view.findViewById(R.id.locationTitle);
        descriptionView = view.findViewById(R.id.locationDescription);
        imageView = view.findViewById(R.id.locationImage);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String id = getArguments().getString("location_id");
        db.collection("locations").document(id).get().addOnSuccessListener(doc -> {
            titleView.setText(doc.getString("title"));
            descriptionView.setText(doc.getString("description"));
            Glide.with(requireContext()).load(doc.getString("imageUrl")).into(imageView);
        });
    }
}

