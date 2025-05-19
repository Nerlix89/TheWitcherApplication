package com.example.thewitcherapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText emailField;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        emailField = view.findViewById(R.id.emailField_Reset);
        Button resetBtn = view.findViewById(R.id.resetButton);
        TextView toLogin = view.findViewById(R.id.toLoginFromReset);

        mAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(v -> resetPassword());
        toLogin.setOnClickListener(v -> navigate(new LoginFragment()));

        return view;
    }

    private void resetPassword() {
        String email = emailField.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Введите email для сброса пароля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> Toast.makeText(getContext(),
                        "Письмо для сброса пароля отправлено на " + email,
                        Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Не удалось отправить письмо: " + e.getMessage(),
                        Toast.LENGTH_LONG).show());
    }

    private void navigate(Fragment fragment) {
        ((AuthActivity) requireActivity()).navigateTo(fragment);
    }
}

