package com.aplimovil.upocket.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityMovement;

public class HomeFragment extends Fragment {

    long date = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = sdf.format(date);

    ArrayList<Reminder> reminders = new ArrayList<>();
    ConexionSQLiteOpenHelper conn;
    private int incom = 0;
    private int incomdia = 0;
    private int incommes = 0;
    private int outcom = 0;
    private int outcomdia = 0;
    private int outcommes = 0;
    private int balan = 0;
    private int periodo = 1; // 0 all, 1 today, 2 mensual.
    private Button newMovement;
    private ImageView hideButton;
    private TextView balance;
    private TextView incomes;
    private TextView outcomes;
    private TextView tvStranger;
    private boolean esVisible = true;

    //ConexionSQLiteOpenHelper conn;

    private FirebaseAuth mAuth;

    private FirebaseFirestore dbFirestore;
  
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Conexion SQlite
        conn = new ConexionSQLiteOpenHelper(getContext());

        balance = root.findViewById(R.id.balance_TextView);
        incomes = root.findViewById(R.id.incomes_value);
        outcomes = root.findViewById(R.id.outcomes_value);
        tvStranger = root.findViewById(R.id.user_text_home);

        // Llenado de incomes y outcomes
        consultarIncomes();
        consultarOutcomes();
        balan = incom - outcom;
        // Llenado de Current Balance
        balance.setText(NumberFormat.getCurrencyInstance().format(balan));

        // Llenado de Remainders
        consultarReminders();

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

        // Initialize Firebase Firestore
        dbFirestore = FirebaseFirestore.getInstance();

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
            final String miUid = mAuth.getCurrentUser().getUid();

            dbFirestore.collection("usuarios").document(miUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String miName = documentSnapshot.getString("uNombre");

                        tvStranger.setText(miName);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.msg_error_documentsnapshot, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void consultarIncomes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] campos = {UtilityMovement.PRECIO};
        try {
            Cursor cursor = db.query(UtilityMovement.TABLA_MOVEMENTS, campos, UtilityMovement.TYPE + "=" + "1", null, null, null, null);
            Cursor cursordia = db.rawQuery("select precio from movements where type = 1 and created_at = CURRENT_DATE", null);
            while (cursor.moveToNext()) {
                incom += Integer.parseInt(cursor.getString(0));
            }
            while (cursordia.moveToNext()) {
                incomdia += Integer.parseInt(cursordia.getString(0));
            }
            if (periodo == 0) {
                incomes.setText(NumberFormat.getCurrencyInstance().format(incom));
            } else if(periodo == 1) {
                incomes.setText(NumberFormat.getCurrencyInstance().format(incomdia));
            } else {
                incomes.setText(NumberFormat.getCurrencyInstance().format(incommes));
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "No hay incomes.", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarOutcomes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] campos = {UtilityMovement.PRECIO};
        try {
            Cursor cursor = db.query(UtilityMovement.TABLA_MOVEMENTS, campos, UtilityMovement.TYPE + "=" + "0", null, null, null, null);
            Cursor cursordia = db.rawQuery("select precio from movements where type = 0 and created_at = CURRENT_DATE", null);
            while (cursor.moveToNext()) {
                outcom += Integer.parseInt(cursor.getString(0));
            }
            while (cursordia.moveToNext()) {
                outcomdia += Integer.parseInt(cursordia.getString(0));
            }
            if (periodo == 0) {
                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcom));
            } else if(periodo == 1) {
                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcomdia));
            } else {
                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcommes));
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "No hay outcomes.", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarReminders() {
        SQLiteDatabase db = conn.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select name,precio from movements where frequency != \"\"", null);
            while (cursor.moveToNext()) {
                reminders.add(new Reminder(cursor.getString(0), NumberFormat.getCurrencyInstance().format(Integer.parseInt(cursor.getString(1)))));
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "No hay recordatorios.", Toast.LENGTH_SHORT).show();

        }
    }

}