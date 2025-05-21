package com.example.thewitcherapplication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText usernameField, emailField, passwordField;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ExoPlayer exoPlayer;

    private long playbackPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        PlayerView playerView = view.findViewById(R.id.playerView_Register);

        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("asset:///register_background_video.mp4"));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();

        usernameField = view.findViewById(R.id.usernameField_Register);
        emailField = view.findViewById(R.id.emailField_Register);
        passwordField = view.findViewById(R.id.passwordField_Register);
        Button registerBtn = view.findViewById(R.id.registerButton);
        TextView toLogin = view.findViewById(R.id.toLogin);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> registerUser());
        toLogin.setOnClickListener(v -> navigate(new LoginFragment()));

        db = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            exoPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer != null) {
            exoPlayer.seekTo(playbackPosition);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void registerUser() {
        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Введите имя, email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUserToFirestore(user.getUid(), username, email);
                    user.sendEmailVerification().addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Письмо с подтверждением отправлено", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        navigate(new LoginFragment());
                    }).addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Не удалось отправить письмо: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            } else {
                Toast.makeText(getContext(), "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserToFirestore(String uid, String username, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("email", email);

        db.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(unused -> Log.d("Register", "Пользователь добавлен в Firestore"))
                .addOnFailureListener(e -> Log.e("Register", "Ошибка Firestore: " + e.getMessage()));
    }

    private void navigate(Fragment fragment) {
        ((AuthActivity) requireActivity()).navigateTo(fragment);
    }
}

