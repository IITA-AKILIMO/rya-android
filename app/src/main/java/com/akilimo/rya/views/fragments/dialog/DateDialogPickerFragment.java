package com.akilimo.rya.views.fragments.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.akilimo.rya.interfaces.IDatePickerDismissListener;
import com.akilimo.rya.utils.DateHelper;

import java.util.Calendar;

public class DateDialogPickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final int PLANTING_REQUEST_CODE = 11; // Used to identify the result
    public static final int HARVEST_REQUEST_CODE = 12; // Used to identify the result
    public static final String TAG = "DatePickerFragment";

    private final Calendar myCalendar = Calendar.getInstance();
    private boolean pickPlantingDate;
    private boolean pickHarvestDate;
    private String selectedPlantingDate;
    private String selectedDate;

    private IDatePickerDismissListener onDismissListener;
    

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Set the current date as the default date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Return a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), DateDialogPickerFragment.this, year, month, day);

        DatePicker datePicker = datePickerDialog.getDatePicker();

        if (pickPlantingDate) {
            Calendar minDate = DateHelper.getMinDate(-16);
            Calendar maxDate = DateHelper.getMinDate(12);
            datePicker.setMinDate(minDate.getTimeInMillis());
            datePicker.setMaxDate(maxDate.getTimeInMillis());
        } else if (pickHarvestDate && !selectedPlantingDate.isEmpty()) {
            Calendar minDate = DateHelper.getFutureOrPastMonth(selectedPlantingDate, 8);
            Calendar maxDate = DateHelper.getFutureOrPastMonth(selectedPlantingDate, 16);
            datePicker.setMinDate(minDate.getTimeInMillis());
            datePicker.setMaxDate(maxDate.getTimeInMillis());
        }
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = DateHelper.getSimpleDateFormatter().format(myCalendar.getTime());

        // send date back to the target fragment
        Fragment target = getTargetFragment();
        if (target != null) {
            Intent intent = new Intent();
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("selectedDateObject", myCalendar.getTime());
            target.onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    intent
            );
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(myCalendar, selectedDate, pickPlantingDate, pickHarvestDate);
        }
    }

    public void setOnDismissListener(IDatePickerDismissListener dismissListener) {
        this.onDismissListener = dismissListener;
    }
}
