package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.FragmentHomeBinding;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DataManager dm = DataManager.getInstance();
        User user = dm.getUser();

        binding.txtDate.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(new Date()));
        binding.txtUserName.setText(user != null ? user.name : "");

        binding.btnSeeAll.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_categories));

        binding.rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvCategories.setAdapter(new CategoryAdapter(dm.getCategories(), category -> {
            Bundle args = new Bundle();
            args.putString("categoryId", category.id);
            Navigation.findNavController(view).navigate(R.id.action_home_to_subcategory, args);
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
