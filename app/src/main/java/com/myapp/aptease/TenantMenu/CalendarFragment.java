package com.myapp.aptease.TenantMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.fragment.app.Fragment;

import com.myapp.aptease.R;

public class CalendarFragment extends Fragment {  // Renamed class

    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);

        // Set the date change listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Format the date
                String date = formatDate(year, month, dayOfMonth);
                // Pass the selected date back to the TenantForm activity
                if (getActivity() instanceof TenantForm) {
                    ((TenantForm) getActivity()).setRegisterDate(date);
                }
                // Close the calendar fragment
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        // Adjust month (Calendar uses 0-based index)
        month = month + 1;
        String date = dayOfMonth + "/" + month + "/" + year;
        return date;
    }
}
