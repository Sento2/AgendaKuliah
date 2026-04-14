package com.f55124091.agendakuliah.ui.fragment;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.f55124091.agendakuliah.adapter.TaskAdapter;
import com.f55124091.agendakuliah.databinding.FragmentDoneTaskBinding;
import com.f55124091.agendakuliah.model.Task;
import com.f55124091.agendakuliah.viewmodel.TaskViewModel;

public class DoneTaskFragment extends Fragment implements TaskAdapter.OnTaskActionListener {
    private FragmentDoneTaskBinding binding;
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    public static DoneTaskFragment newInstance(int userId) {
        DoneTaskFragment f = new DoneTaskFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        f.setArguments(args);
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
        binding = FragmentDoneTaskBinding.inflate(i, c, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle s) {
        super.onViewCreated(view, s);
        adapter = new TaskAdapter(this, true); // true = mode selesai
        binding.rvDoneTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Observe layout mode (List vs Grid)
        taskViewModel.getIsGridView().observe(getViewLifecycleOwner(), isGrid -> {
            if (isGrid) {
                binding.rvDoneTasks.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                binding.rvDoneTasks.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        taskViewModel.getDoneTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTasks(tasks);
            binding.tvEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override public void onEditTask(Task t) {}
    @Override public void onMarkDone(Task t) {}

    @Override
    public void onDeleteTask(Task task) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Hapus Tugas")
                .setMessage("Hapus tugas yang sudah selesai ini?")
                .setPositiveButton("Hapus", (d, w) -> taskViewModel.deleteTask(task.getId()))
                .setNegativeButton("Batal", null).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}