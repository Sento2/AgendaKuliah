package com.kel4.agendakuliah.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.kel4.agendakuliah.databinding.DialogTaskBinding;
import com.kel4.agendakuliah.model.Task;
import com.kel4.agendakuliah.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.Locale;

public class TaskDialogFragment extends DialogFragment {
    private DialogTaskBinding binding;
    private TaskViewModel taskViewModel;
    private Task editTask;
    private int userId;

    public static TaskDialogFragment newInstance(@Nullable Task task, int userId) {
        TaskDialogFragment f = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        if (task != null) {
            args.putInt("task_id", task.getId());
            args.putString("task_title", task.getTitle());
            args.putString("task_course", task.getCourse());
            args.putInt("task_priority", task.getPriority());
            args.putString("task_dl", task.getDeadline());
            args.putString("task_desc", task.getDescription());
        }
        f.setArguments(args);
        return f;
    }

    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogTaskBinding.inflate(getLayoutInflater());
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        userId = getArguments() != null ? getArguments().getInt("user_id") : -1;

        boolean isEdit = getArguments() != null && getArguments().containsKey("task_id");
        if (isEdit) {
            Bundle a = getArguments();
            binding.edtTitle.setText(a.getString("task_title"));
            binding.edtCourse.setText(a.getString("task_course"));
            binding.edtDeadline.setText(a.getString("task_dl"));
            binding.edtDescription.setText(a.getString("task_desc"));

            int priority = a.getInt("task_priority", 2);
            if (priority == 1) binding.rbLow.setChecked(true);
            else if (priority == 3) binding.rbHigh.setChecked(true);
            else binding.rbMedium.setChecked(true);

            editTask = new Task();
            editTask.setId(a.getInt("task_id"));
            editTask.setUserId(userId);
        }

        binding.edtDeadline.setOnClickListener(v -> showDatePicker());

        String dialogTitle = isEdit ? "Edit Tugas" : "Tambah Tugas";
        String positiveBtn = isEdit ? "Simpan" : "Tambah";

        return new AlertDialog.Builder(requireContext())
                .setTitle(dialogTitle)
                .setView(binding.getRoot())
                .setPositiveButton(positiveBtn, (d, w) -> saveTask(isEdit))
                .setNegativeButton("Batal", null)
                .create();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year1);
                    binding.edtDeadline.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTask(boolean isEdit) {
        String title = binding.edtTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Judul tugas tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        String course = binding.edtCourse.getText().toString().trim();
        String dl = binding.edtDeadline.getText().toString().trim();
        String desc = binding.edtDescription.getText().toString().trim();
        int priority = binding.rbHigh.isChecked() ? 3 : binding.rbMedium.isChecked() ? 2 : 1;

        if (isEdit && editTask != null) {
            editTask.setTitle(title);
            editTask.setCourse(course);
            editTask.setDeadline(dl);
            editTask.setPriority(priority);
            editTask.setDescription(desc);
            taskViewModel.updateTask(editTask);
        } else {
            Task newTask = new Task(title, course, dl, priority, desc, userId);
            taskViewModel.addTask(newTask);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}