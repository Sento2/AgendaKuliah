package com.f55124091.agendakuliah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.f55124091.agendakuliah.R;
import com.f55124091.agendakuliah.databinding.ActivityMainBinding;
import com.f55124091.agendakuliah.ui.fragment.ActiveTaskFragment;
import com.f55124091.agendakuliah.ui.fragment.DoneTaskFragment;
import com.f55124091.agendakuliah.ui.fragment.ProfileFragment;
import com.f55124091.agendakuliah.viewmodel.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TaskViewModel taskViewModel;
    private int userId;
    private String username;
    private boolean isGridView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("USER_ID", -1);
        username = getIntent().getStringExtra("USERNAME");

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Halo, " + username + "!");

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.setUserId(userId);

        setupViewPager();

        binding.fabAdd.setOnClickListener(v -> {
            TaskDialogFragment dialog = TaskDialogFragment.newInstance(null, userId);
            dialog.show(getSupportFragmentManager(), "add_task");
        });

        binding.fabSwitchLayout.setOnClickListener(v -> {
            isGridView = !isGridView;
            binding.fabSwitchLayout.setImageResource(isGridView ? android.R.drawable.ic_menu_sort_by_size : android.R.drawable.ic_dialog_dialer);
            taskViewModel.setLayoutMode(isGridView);
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Sembunyikan FAB di fragment Profil (index 2)
                if (position == 2) {
                    binding.fabAdd.hide();
                    binding.fabSwitchLayout.hide();
                } else {
                    binding.fabAdd.show();
                    binding.fabSwitchLayout.show();
                }
            }

            @Override public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ActiveTaskFragment.newInstance(userId), "Aktif");
        adapter.addFragment(DoneTaskFragment.newInstance(userId), "Selesai");
        adapter.addFragment(ProfileFragment.newInstance(userId, username), "Profil");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Hapus menu switch layout dari toolbar karena sudah pindah ke FAB
        menu.findItem(R.id.action_switch_layout).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}