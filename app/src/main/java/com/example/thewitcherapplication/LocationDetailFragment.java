package com.example.thewitcherapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        ImageButton closeButton = view.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String id = getArguments() != null ? getArguments().getString("location_id") : null;

        if (id != null) {
            db.collection("locations").document(id)
                    .get()
                    .addOnSuccessListener(doc -> {
                        Location location = doc.toObject(Location.class);
                        if (location != null) {
                            titleView.setText(location.getTitle());
                            descriptionView.setText(location.getDescription());
                            Glide.with(requireContext()).load(location.getImageUrl()).into(imageView);
                        }
                    });
        }
    }
}

