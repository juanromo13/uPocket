package com.aplimovil.upocket.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aplimovil.upocket.MainActivity;
import com.aplimovil.upocket.R;
import com.aplimovil.upocket.RegisterMovementActivity;
import com.aplimovil.upocket.Reminder;
import com.aplimovil.upocket.ReminderAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Button newMovement;
    private ImageView hideButton;
    private TextView balance;
    private boolean esVisible = true;

    public HomeFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("Energy Bill", 150000));

        ReminderAdapter remindersAdapter = new ReminderAdapter(getActivity(), reminders);
        ListView listReminderView = root.findViewById(R.id.reminders_list);
        listReminderView.setAdapter(remindersAdapter);

        // Aqui funcionalidad de ocultar o mostrar balance
        balance = root.findViewById(R.id.balance_TextView);
        hideButton = root.findViewById(R.id.hide_ImageView);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!esVisible) {
                    balance.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    esVisible = true;
                    hideButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    ///aqui puedes cambiar el texto del boton, o textview, o cambiar la imagen de un imageView.
                }
                else {
                    balance.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    esVisible = false;
                    hideButton.setImageResource(R.drawable.ic_baseline_visibility_24);
                    ///aqui puedes cambiar el texto del boton, o textview, o cambiar la imagen de un imageView.
                }
            }
        });

        // Intent para ir a formuario para crear movimiento.
        newMovement = root.findViewById(R.id.new_movement_button);
        newMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getContext(), RegisterMovementActivity.class);
                startActivity(registerIntent);
            }
        });

        return root;
    }

}