package com.example.thewitcherapplication;

import android.net.Uri;
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
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ResetPasswordFragment extends Fragment {

    private EditText emailField;
    private FirebaseAuth mAuth;
    private ExoPlayer exoPlayer;

    private long playbackPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        PlayerView playerView = view.findViewById(R.id.playerView_Reset);

        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("asset:///reset_background_video.mp4"));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();

        emailField = view.findViewById(R.id.emailField_Reset);
        Button resetBtn = view.findViewById(R.id.resetButton);
        TextView toLogin = view.findViewById(R.id.toLoginFromReset);

        mAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(v -> resetPassword());
        toLogin.setOnClickListener(v -> navigate(new LoginFragment()));

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
                .addOnFailureListener(e -> {
                    String errorMessage = "Не удалось отправить письмо для сброса пароля";

                    if (e instanceof FirebaseAuthInvalidUserException) {
                        errorMessage = "Пользователь с таким email не найден";
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        errorMessage = "Некорректный формат email";
                    } else if (e instanceof FirebaseNetworkException) {
                        errorMessage = "Проблема с интернет-соединением";
                    }

                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });
    }

    private void navigate(Fragment fragment) {
        ((AuthActivity) requireActivity()).navigateTo(fragment);
    }
}

