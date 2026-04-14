package com.f55124091.agendakuliah.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.f55124091.agendakuliah.database.DatabaseHelper;
import com.f55124091.agendakuliah.model.Task;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private final DatabaseHelper db;
    private final MutableLiveData<List<Task>> activeTasks = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> doneTasks = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGridView = new MutableLiveData<>(false);
    private int currentUserId = -1;

    public TaskViewModel(@NonNull Application app) {
        super(app);
        db = new DatabaseHelper(app);
    }

    public void setUserId(int userId) {
        this.currentUserId = userId;
        refreshTasks();
    }

    public LiveData<List<Task>> getActiveTasks() { return activeTasks; }
    public LiveData<List<Task>> getDoneTasks() { return doneTasks; }
    public LiveData<Boolean> getIsGridView() { return isGridView; }

    public void setLayoutMode(boolean isGrid) {
        isGridView.setValue(isGrid);
    }

    public void refreshTasks() {
        if (currentUserId == -1) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            activeTasks.postValue(db.getActiveTasks(currentUserId));
            doneTasks.postValue(db.getDoneTasks(currentUserId));
        });
    }

    public void addTask(Task task) {
        task.setUserId(currentUserId);
        Executors.newSingleThreadExecutor().execute(() -> {
            db.addTask(task);
            refreshTasks();
        });
    }

    public void updateTask(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.updateTask(task);
            refreshTasks();
        });
    }

    public void deleteTask(int taskId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.deleteTask(taskId);
            refreshTasks();
        });
    }

    public void markAsDone(Task task) {
        task.setDone(true);
        updateTask(task);
    }
}