package com.example.thewitcherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView nameText, emailText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = view.findViewById(R.id.nameText);
        emailText = view.findViewById(R.id.emailText);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button deleteButton = view.findViewById(R.id.deleteProfileButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loadUserData(currentUser.getUid());
        }

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                db.collection("users").document(user.getUid())
                        .delete()
                        .addOnSuccessListener(unused -> user.delete().addOnCompleteListener(task -> {
                            mAuth.signOut();
                            startActivity(new Intent(requireActivity(), AuthActivity.class));
                            requireActivity().finish();
                        }));
            }
        });

        return view;
    }

    private void loadUserData(String uid) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String name = document.getString("username");
                        String email = document.getString("email");

                        nameText.setText(name != null ? name : "Неизвестно");
                        emailText.setText(email != null ? email : "Нет email");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
                    Log.e("Profile", "Ошибка Firestore: " + e.getMessage());
                });
    }
}

