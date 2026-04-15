package com.kel4.agendakuliah.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kel4.agendakuliah.databinding.ActivityLoginBinding;
import com.kel4.agendakuliah.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        int savedUserId = prefs.getInt("user_id", -1);
        if (savedUserId != -1) {
            goToMain(savedUserId, prefs.getString("username", ""));
            return;
        }

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                prefs.edit()
                        .putInt("user_id", user.getId())
                        .putString("username", user.getUsername())
                        .apply();
                goToMain(user.getId(), user.getUsername());
            } else {
                Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getErrorMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

        binding.btnLogin.setOnClickListener(v -> {
            String user = binding.edtUsername.getText().toString().trim();
            String pass = binding.edtPassword.getText().toString().trim();
            authViewModel.login(user, pass);
        });

        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void goToMain(int userId, String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }
}