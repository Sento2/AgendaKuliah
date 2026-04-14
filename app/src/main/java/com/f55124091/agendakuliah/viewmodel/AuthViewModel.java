package com.f55124091.agendakuliah.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.f55124091.agendakuliah.database.DatabaseHelper;
import com.f55124091.agendakuliah.model.User;
import java.util.concurrent.Executors;

public class AuthViewModel extends AndroidViewModel {
    private final DatabaseHelper db;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application app) {
        super(app);
        db = new DatabaseHelper(app);
    }

    public LiveData<User> getLoginResult() { return loginResult; }
    public LiveData<Boolean> getRegisterResult() { return registerResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Username dan password tidak boleh kosong!");
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.checkLogin(username, password);
            loginResult.postValue(user);
        });
    }

    public void register(String username, String password, String confirmPass, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            errorMessage.setValue("Semua kolom wajib diisi!");
            return;
        }
        if (!password.equals(confirmPass)) {
            errorMessage.setValue("Konfirmasi password tidak cocok!");
            return;
        }
        if (password.length() < 6) {
            errorMessage.setValue("Password minimal 6 karakter!");
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean ok = db.registerUser(username, password, email);
            registerResult.postValue(ok);
        });
    }
}