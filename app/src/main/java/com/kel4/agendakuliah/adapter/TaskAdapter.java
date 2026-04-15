package com.kel4.agendakuliah.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kel4.agendakuliah.databinding.ItemTaskBinding;
import com.kel4.agendakuliah.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskActionListener {
        void onEditTask(Task task);
        void onDeleteTask(Task task);
        void onMarkDone(Task task);
    }

    private List<Task> tasks = new ArrayList<>();
    private final boolean isDoneTab;
    private final OnTaskActionListener listener;

    public TaskAdapter(OnTaskActionListener listener, boolean isDoneTab) {
        this.listener = listener;
        this.isDoneTab = isDoneTab;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding b = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        final ItemTaskBinding b;

        TaskViewHolder(ItemTaskBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(Task task) {
            b.tvTaskTitle.setText(task.getTitle());
            b.tvTaskCourse.setText(task.getCourse());
            b.tvTaskDeadline.setText("Deadline: " + task.getDeadline());

            switch (task.getPriority()) {
                case 3:
                    b.viewPriority.setBackgroundColor(Color.parseColor("#EA4335"));
                    break;
                case 2:
                    b.viewPriority.setBackgroundColor(Color.parseColor("#FBBC05"));
                    break;
                default:
                    b.viewPriority.setBackgroundColor(Color.parseColor("#34A853"));
                    break;
            }

            if (isDoneTab) {
                b.btnDone.setVisibility(View.GONE);
                b.btnEdit.setVisibility(View.GONE);
            } else {
                b.btnDone.setOnClickListener(v -> listener.onMarkDone(task));
                b.btnEdit.setOnClickListener(v -> listener.onEditTask(task));
            }
            b.btnDelete.setOnClickListener(v -> listener.onDeleteTask(task));
        }
    }
}