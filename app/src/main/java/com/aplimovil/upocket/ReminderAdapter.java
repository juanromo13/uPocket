package com.aplimovil.upocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    public ReminderAdapter(@NonNull Context context,ArrayList<Reminder> resourse) {
        super(context, 0, resourse);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listReminderView = convertView;
        if (listReminderView == null) {
            listReminderView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_view_item, parent, false);
        }

        Reminder currentReminder = getItem(position);

        TextView recordatorio = listReminderView.findViewById(R.id.recordatorio_TextView);
        recordatorio.setText(currentReminder.getRecordatorio());

        TextView valor = listReminderView.findViewById(R.id.valor_recordatorio_TextView);
        valor.setText(Integer.toString(currentReminder.getValorRecordatorio()));

        return listReminderView;
    }
}
