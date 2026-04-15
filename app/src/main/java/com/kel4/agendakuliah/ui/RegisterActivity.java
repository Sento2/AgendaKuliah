package com.kel4.agendakuliah.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kel4.agendakuliah.databinding.ActivityRegisterBinding;
import com.kel4.agendakuliah.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getRegisterResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Username sudah digunakan!", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getErrorMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

        binding.btnRegister.setOnClickListener(v -> {
            String user = binding.edtUsername.getText().toString().trim();
            String pass = binding.edtPassword.getText().toString().trim();
            String confirm = binding.edtConfirmPassword.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            authViewModel.register(user, pass, confirm, email);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }
}