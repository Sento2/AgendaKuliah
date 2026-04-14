package com.f55124091.agendakuliah.ui.fragment;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.f55124091.agendakuliah.databinding.FragmentProfileBinding;
import com.f55124091.agendakuliah.viewmodel.TaskViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private TaskViewModel taskViewModel;

    public static ProfileFragment newInstance(int userId, String username) {
        ProfileFragment f = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
        binding = FragmentProfileBinding.inflate(i, c, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle s) {
        super.onViewCreated(view, s);

        String username = getArguments() != null ? getArguments().getString("username") : "-";
        binding.tvUsername.setText(username);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        taskViewModel.getActiveTasks().observe(getViewLifecycleOwner(), tasks ->
                binding.tvActiveCount.setText(String.valueOf(tasks.size())));

        taskViewModel.getDoneTasks().observe(getViewLifecycleOwner(), tasks ->
                binding.tvDoneCount.setText(String.valueOf(tasks.size())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}