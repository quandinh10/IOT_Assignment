package com.iot232.ssis.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iot232.ssis.databinding.FragmentAutomationsBinding;
import com.iot232.ssis.ui.automations.AutomationsViewModel;

public class AutomationsFragment extends Fragment {

    private FragmentAutomationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AutomationsViewModel automationsViewModel =
                new ViewModelProvider(this).get(AutomationsViewModel.class);

        binding = FragmentAutomationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        automationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}