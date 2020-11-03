package com.aplimovil.upocket.ui.home;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.R;
import com.aplimovil.upocket.RegisterMovementActivity;
import com.aplimovil.upocket.Reminder;
import com.aplimovil.upocket.ReminderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.ArrayList;

import BD.ConexionSQLiteOpenHelper;

public class HomeFragment extends Fragment {

    private Button newMovement;
    private ImageView hideButton;
    private TextView balance;
    private TextView incomes;
    private TextView outcomes;
    private boolean esVisible = true;

    ConexionSQLiteOpenHelper conn;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Conexion SQlite
        conn = new ConexionSQLiteOpenHelper(getContext());

        balance = root.findViewById(R.id.balance_TextView);
        incomes = root.findViewById(R.id.incomes_value);
        outcomes = root.findViewById(R.id.outcomes_value);
        // Llenado de Current Balance
        balance.setText(NumberFormat.getCurrencyInstance().format(24000000));
        // Llenado de incomes y outcomes
        incomes.setText(NumberFormat.getCurrencyInstance().format(50000));
        outcomes.setText(NumberFormat.getCurrencyInstance().format(10000));
        // Llenado de Remainders
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("Energy Bill", NumberFormat.getCurrencyInstance().format(150000)));
        reminders.add(new Reminder("Water Bill", NumberFormat.getCurrencyInstance().format(150000)));
        reminders.add(new Reminder("Internet Bill", NumberFormat.getCurrencyInstance().format(150000)));
        // ArrayAdapter of remainders
        ReminderAdapter remindersAdapter = new ReminderAdapter(getActivity(), reminders);
        ListView listReminderView = root.findViewById(R.id.reminders_list);
        listReminderView.setAdapter(remindersAdapter);

        // Aqui funcionalidad de ocultar o mostrar balance
        hideButton = root.findViewById(R.id.hide_ImageView);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!esVisible) {
                    balance.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    incomes.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    outcomes.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    esVisible = true;
                    hideButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    ///aqui puedes cambiar el texto del boton, o textview, o cambiar la imagen de un imageView.
                } else {
                    balance.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    incomes.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    outcomes.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        compruebaLogin(currentUser);
    }

    private void compruebaLogin(FirebaseUser user) {
        if (user != null) {

            //Toast.makeText(getContext(), R.string.msg_autenticado, Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(getContext(), R.string.msg_noautenticado, Toast.LENGTH_LONG).show();
        }
    }
}