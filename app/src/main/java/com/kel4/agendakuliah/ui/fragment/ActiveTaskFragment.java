package com.kel4.agendakuliah.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kel4.agendakuliah.adapter.TaskAdapter;
import com.kel4.agendakuliah.databinding.FragmentActiveTaskBinding;
import com.kel4.agendakuliah.model.Task;
import com.kel4.agendakuliah.ui.TaskDialogFragment;
import com.kel4.agendakuliah.viewmodel.TaskViewModel;

public class ActiveTaskFragment extends Fragment implements TaskAdapter.OnTaskActionListener {
    private static final String ARG_USER_ID = "user_id";
    private FragmentActiveTaskBinding binding;
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private int userId;

    public static ActiveTaskFragment newInstance(int userId) {
        ActiveTaskFragment f = new ActiveTaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) userId = getArguments().getInt(ARG_USER_ID);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActiveTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TaskAdapter(this, false);
        binding.rvActiveTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        
        taskViewModel.getIsGridView().observe(getViewLifecycleOwner(), isGrid -> {
            if (isGrid) {
                binding.rvActiveTasks.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                binding.rvActiveTasks.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        taskViewModel.getActiveTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTasks(tasks);
            binding.layoutEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override public void onEditTask(Task task) {
        TaskDialogFragment dialog = TaskDialogFragment.newInstance(task, userId);
        dialog.show(getParentFragmentManager(), "edit_task");
    }

    @Override public void onDeleteTask(Task task) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Hapus Tugas")
                .setMessage("Yakin ingin menghapus tugas ini?")
                .setPositiveButton("Hapus", (d, w) -> taskViewModel.deleteTask(task.getId()))
                .setNegativeButton("Batal", null).show();
    }

    @Override public void onMarkDone(Task task) { taskViewModel.markAsDone(task); }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}