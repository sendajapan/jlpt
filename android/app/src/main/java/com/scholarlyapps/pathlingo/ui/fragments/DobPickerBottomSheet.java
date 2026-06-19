package com.scholarlyapps.pathlingo.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.scholarlyapps.pathlingo.databinding.BottomSheetDobPickerBinding;

import java.util.Calendar;

public class DobPickerBottomSheet extends BottomSheetDialogFragment {

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }

    private static final String ARG_YEAR  = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY   = "day";

    private BottomSheetDobPickerBinding binding;
    private OnDateSelectedListener listener;

    public static DobPickerBottomSheet newInstance(int year, int month, int day) {
        DobPickerBottomSheet sheet = new DobPickerBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        sheet.setArguments(args);
        return sheet;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDobPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = requireArguments();
        int initYear  = args.getInt(ARG_YEAR, 2000);
        int initMonth = args.getInt(ARG_MONTH, 0);
        int initDay   = args.getInt(ARG_DAY, 1);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};

        setupPicker(binding.pickerMonth, 0, 11, initMonth, months);
        setupPicker(binding.pickerDay,   1, 31, initDay,   null);
        setupPicker(binding.pickerYear,  currentYear - 100, currentYear - 6, initYear, null);

        binding.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDateSelected(
                    binding.pickerYear.getValue(),
                    binding.pickerMonth.getValue(),
                    binding.pickerDay.getValue()
                );
            }
            dismiss();
        });
    }

    private void setupPicker(NumberPicker picker, int min, int max, int value, String[] displayValues) {
        picker.setMinValue(min);
        picker.setMaxValue(max);
        if (displayValues != null) picker.setDisplayedValues(displayValues);
        picker.setValue(value);
        picker.setTextColor(Color.parseColor("#2D2D2D"));
        picker.setTextSize(42f);
        removeDividers(picker);
    }

    private void removeDividers(NumberPicker picker) {
        try {
            java.lang.reflect.Field field = NumberPicker.class.getDeclaredField("mSelectionDivider");
            field.setAccessible(true);
            field.set(picker, null);
        } catch (Exception ignored) {}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
