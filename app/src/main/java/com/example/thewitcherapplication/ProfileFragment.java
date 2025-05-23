package com.example.thewitcherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
        ImageView editIcon = view.findViewById(R.id.editIcon);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loadUserData(currentUser.getUid());
        }

        logoutButton.setOnClickListener(v -> showConfirmDialog("Выйти из аккаунта?", () -> {
            mAuth.signOut();
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }));

        deleteButton.setOnClickListener(v -> showConfirmDialog("Удалить аккаунт?", () -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                db.collection("users").document(user.getUid())
                        .delete()
                        .addOnSuccessListener(unused -> user.delete().addOnCompleteListener(task -> {
                            user.delete();
                            mAuth.signOut();
                            startActivity(new Intent(requireActivity(), AuthActivity.class));
                            requireActivity().finish();
                        }));
            }
        }));

        editIcon.setOnClickListener(v -> showEditNameDialog());

        return view;
    }

    private void showConfirmDialog(String title, Runnable onConfirm) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_custom, null);
        TextView titleView = dialogView.findViewById(R.id.dialogTitle);
        Button confirmBtn = dialogView.findViewById(R.id.confirmButton);
        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);

        titleView.setText(title);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        confirmBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onConfirm.run();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showEditNameDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_name, null);
        EditText editUsername = dialogView.findViewById(R.id.editUsername);
        Button confirmButton = dialogView.findViewById(R.id.applyButton);
        Button cancelButton = dialogView.findViewById(R.id.dismissButton);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        confirmButton.setOnClickListener(v -> {
            String newName = editUsername.getText().toString().trim();
            if (!newName.isEmpty() && currentUser != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(currentUser.getUid())
                        .update("username", newName)
                        .addOnSuccessListener(unused -> {
                            nameText.setText(newName);
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Ошибка обновления:", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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

