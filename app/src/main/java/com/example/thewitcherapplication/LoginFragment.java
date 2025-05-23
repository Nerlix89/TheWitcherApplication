package com.example.thewitcherapplication;

import android.content.Intent;
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
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.Player;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText emailField, passwordField;
    private FirebaseAuth mAuth;
    private ExoPlayer exoPlayer;

    private long playbackPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        PlayerView playerView = view.findViewById(R.id.playerView_Login);

        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("asset:///enterance_background_video.mp4"));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();

        emailField = view.findViewById(R.id.emailField_Login);
        passwordField = view.findViewById(R.id.passwordField_Login);
        Button loginBtn = view.findViewById(R.id.loginButton);
        TextView toRegister = view.findViewById(R.id.toRegister);
        TextView toReset = view.findViewById(R.id.toReset);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> loginUser());
        toRegister.setOnClickListener(v -> navigate(new RegisterFragment()));
        toReset.setOnClickListener(v -> navigate(new ResetPasswordFragment()));
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
                    Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_LONG).show();
                } else if (user.isEmailVerified()) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                } else {
                    Toast.makeText(getContext(),
                            "Ваш email не подтверждён. Подтвердите почту.",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Exception exception = task.getException();
                String errorMessage = "Неизвестная ошибка. Попробуйте позже.";

                if (exception instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) exception).getErrorCode();

                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            errorMessage = "Некорректный формат email";
                            break;
                        case "ERROR_TOO_MANY_REQUESTS":
                            errorMessage = "Слишком много попыток входа. Подождите и попробуйте снова";
                            break;
                        default:
                            Log.e("AuthError", "Код ошибки: " + errorCode);
                            errorMessage = "Ошибка входа: " + errorCode;
                            break;
                    }
                }

                if (exception != null && exception.getMessage() != null &&
                        exception.getMessage().contains("INVALID_LOGIN_CREDENTIALS")) {
                    errorMessage = "Неверный email или пароль";
                } else if (exception instanceof FirebaseNetworkException) {
                    errorMessage = "Проблема с интернет-соединением";
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigate(Fragment fragment) {
        ((AuthActivity) requireActivity()).navigateTo(fragment);
    }


}

