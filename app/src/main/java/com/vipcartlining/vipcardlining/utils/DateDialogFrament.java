package com.vipcartlining.vipcardlining.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vipcartlining.vipcardlining.R;

/**
 * Created by Eugene on 09.07.2015.
 */
public class DateDialogFrament extends DialogFragment {
    DatePickerDialog.OnDateSetListener onDateSet;
    DialogInterface.OnDismissListener onDisMiss;

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSet) {
        this.onDateSet = onDateSet;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDisMiss){
        this.onDisMiss = onDisMiss;
    }

    private int year, month, day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(getActivity().getString(R.string.s_datedialog_header));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDisMiss.onDismiss(dialog);
        super.onDismiss(dialog);
    }
}
