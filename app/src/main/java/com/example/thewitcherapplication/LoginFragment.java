package com.example.thewitcherapplication;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText emailField, passwordField;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        Button loginBtn = view.findViewById(R.id.loginButton);
        TextView toRegister = view.findViewById(R.id.toRegister);
        TextView toReset = view.findViewById(R.id.toReset);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> loginUser());
        //toRegister.setOnClickListener(v -> navigate(new RegisterFragment()));
        //toReset.setOnClickListener(v -> navigate(new ResetPasswordFragment()));
        return view;
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(getContext(), "Такой пользователь не найден.", Toast.LENGTH_LONG).show();
                } else if (!user.isEmailVerified()) {
                    Toast.makeText(getContext(),
                            "Ваш email не подтверждён. Пожалуйста, проверьте почту и подтвердите аккаунт.",
                            Toast.LENGTH_LONG).show();
                    }
            }
            else {
                Toast.makeText(getContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigate(Fragment fragment) {
        ((AuthActivity) requireActivity()).navigateTo(fragment);
    }
}

