package com.qubicoo.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.bignerdranch.criminalintent.R;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME =
            "com.bignerdranch.android.criminalintent.time_hours";
    private static final String ARG_TIME = "time";
    private static TimePicker mTimePicker;
    private Calendar calendar;
    private  int hours;
    private int minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date date; date = (Date) getArguments().getSerializable(ARG_TIME);

        int hour = date.getHours();
        int minutes = date.getMinutes();

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker, null);

        calendar = Calendar.getInstance();

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minutes);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle("Set time :")
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                hours = mTimePicker.getHour();
                                minute = mTimePicker.getMinute();
                            }else{
                                hours = mTimePicker.getCurrentHour();
                                minute = mTimePicker.getCurrentMinute();
                            }
                            calendar.setTime(date);
                            calendar.set(Calendar.HOUR_OF_DAY, hours);
                            calendar.set(Calendar.MINUTE, minute);
                            Date date = calendar.getTime();
                            sendResult(Activity.RESULT_OK,date);
                        }
                    }
                })
                .create();
    }

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void sendResult(int resultCode, Date calendar) {
        if (getTargetFragment() == null) {
        }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TIME, calendar);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }