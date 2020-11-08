package com.aplimovil.upocket.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.R;
import com.aplimovil.upocket.RegisterMovementActivity;
import com.aplimovil.upocket.Reminder;
import com.aplimovil.upocket.ReminderAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ListView listReminderView;
    private boolean esVisible = true;

    //ConexionSQLiteOpenHelper conn;

    private FirebaseAuth mAuth;

    private FirebaseFirestore dbFirestore;

    private static final String TAG = "DocSnippets";
  
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Conexion SQlite
        conn = new ConexionSQLiteOpenHelper(getContext());

        balance = root.findViewById(R.id.balance_TextView);
        incomes = root.findViewById(R.id.incomes_value);
        outcomes = root.findViewById(R.id.outcomes_value);
        tvStranger = root.findViewById(R.id.user_text_home);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Firestore
        dbFirestore = FirebaseFirestore.getInstance();

        // Llenado de incomes y outcomes
        //consultarIncomes();
        //consultarOutcomes();
        consultarIncomesOutcomes();
        //balan = incom - outcom;
        // Llenado de Current Balance
        //balance.setText(NumberFormat.getCurrencyInstance().format(balan));

        // Llenado de Remainders
        listReminderView = root.findViewById(R.id.reminders_list);
        consultarReminders();

        // ArrayAdapter of remainders
        //ReminderAdapter remindersAdapter = new ReminderAdapter(getActivity(), reminders);
        //listReminderView.setAdapter(remindersAdapter);

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

        return root;
    }

    private void consultarIncomesOutcomes() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String miUid = currentUser.getUid();

            dbFirestore.collection("movimientos")
                .whereEqualTo("uId", miUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), R.string.msg_listenfailed, Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Listen failed on Incomes and Outcomes with Firebase.", error);
                    return;
                }

                // Consulta de todos los Incomes del usuario con ID = miUid
                //List<Double> listaIncomes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("mPrecio") != null && doc.getString("mTipo").equals("1") && doc.get("mFechaMensual") == null) {
                        //listaIncomes.add(doc.getDouble("mPrecio"));
                        incom += doc.getDouble("mPrecio");
                        //Log.d(TAG, "mTipo desde doc.get: " + doc.getString("mTipo") + ". Valor asociado: " + doc.get("mPrecio") + ".");

                        switch (periodo) {
                            case 0: // All
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Incom.", Toast.LENGTH_LONG).show();
                                incomes.setText(NumberFormat.getCurrencyInstance().format(incom));
                                break;
                            case 1: // Today
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Incom.", Toast.LENGTH_LONG).show();
                                Timestamp miFechaIn;
                                Date miFechaIn2;
                                DateFormat miFormatoIn = new SimpleDateFormat("dd/MM/yyyy");
                                String miFechaIn3 = null;

                                if (doc.getTimestamp("mFechaCreacion") != null) {
                                    miFechaIn = doc.getTimestamp("mFechaCreacion");
                                    miFechaIn2 = miFechaIn.toDate();
                                    miFechaIn3 = miFormatoIn.format(miFechaIn2);
                                }

                                //Toast.makeText(getContext(), "Caso: " + periodo + ", con fecha: " + miFechaIn3, Toast.LENGTH_LONG).show();
                                //Log.d(TAG, "Caso: " + periodo + ", con fecha: " + miFechaIn3 + " de " + doc.get("mNombre"));

                                String fechaHoyIn = miFormatoIn.format(new Date());
                                //Toast.makeText(getContext(), "Fecha de hoy: " + fechaHoyIn, Toast.LENGTH_LONG).show();

                                if (miFechaIn3.equals(fechaHoyIn)) {
                                    //Toast.makeText(getContext(), "Entró al IF entre fechas", Toast.LENGTH_LONG).show();
                                    incomdia += doc.getDouble("mPrecio");
                                }
                                incomes.setText(NumberFormat.getCurrencyInstance().format(incomdia));
                                break;
                            case 2: // Monthly
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Incom.", Toast.LENGTH_LONG).show();
                                incomes.setText(NumberFormat.getCurrencyInstance().format(incommes));
                                break;
                            default:
                                break;
                        }
                    }
                }
                //Log.d(TAG, "Impresion de listaIcomes: " + listaIncomes + ". Total incomes: " + incom);

                // Consulta de todos los Outcomes del usuario con ID = miUid
                //List<Double> listaOutcomes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("mPrecio") != null && doc.getString("mTipo").equals("0") && doc.get("mFechaMensual") == null) {
                        //listaOutcomes.add(doc.getDouble("mPrecio"));
                        outcom += doc.getDouble("mPrecio");
                        //Log.d(TAG, "mTipo desde doc.get: " + doc.getString("mTipo") + ". Valor asociado: " + doc.getDouble("mPrecio") + ".");

                        switch (periodo) {
                            case 0:
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Outcom.", Toast.LENGTH_LONG).show();
                                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcom));
                                break;
                            case 1:
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Outcom.", Toast.LENGTH_LONG).show();
                                Timestamp miFechaOut;
                                Date miFechaOut2;
                                DateFormat miFormatoOut = new SimpleDateFormat("dd/MM/yyyy");
                                String miFechaOut3 = null;

                                if (doc.getTimestamp("mFechaCreacion") != null) {
                                    miFechaOut = doc.getTimestamp("mFechaCreacion");
                                    miFechaOut2 = miFechaOut.toDate();
                                    miFechaOut3 = miFormatoOut.format(miFechaOut2);
                                }

                                //Toast.makeText(getContext(), "Caso: " + periodo + ", con fecha: " + miFechaOut3, Toast.LENGTH_LONG).show();
                                //Log.d(TAG, "Caso: " + periodo + ", con fecha: " + miFechaOut3 + " de " + doc.get("mNombre"));

                                String fechaHoyOut = miFormatoOut.format(new Date());
                                //Toast.makeText(getContext(), "Fecha de hoy: " + fechaHoyOut, Toast.LENGTH_LONG).show();

                                if (miFechaOut3.equals(fechaHoyOut)) {
                                    //Toast.makeText(getContext(), "Entró al IF entre fechas", Toast.LENGTH_LONG).show();
                                    outcomdia += doc.getDouble("mPrecio");
                                }
                                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcomdia));
                                break;
                            case 2:
                                //Toast.makeText(getContext(), "Caso: " + periodo + ". Outcom.", Toast.LENGTH_LONG).show();
                                outcomes.setText(NumberFormat.getCurrencyInstance().format(outcommes));
                                break;
                            default:
                                break;
                        }
                    }
                }
                //Log.d(TAG, "Impresion de listaOutcomes: " + listaOutcomes + ". Total outcomes: " + outcom);
                balan = incom - outcom;
                Log.d(TAG, "Balance total: " + balan + " Usuario: " + miUid + ". " + incom + ", " + outcom + ".");

                // Llenado de Current Balance
                balance.setText(NumberFormat.getCurrencyInstance().format(balan));
                incom = 0; outcom = 0; balan = 0;
                incomdia = 0; incommes = 0;
                outcomdia = 0; outcommes = 0;
                }
            });
        }
        else {
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
                } else if (periodo == 1) {
                    incomes.setText(NumberFormat.getCurrencyInstance().format(incomdia));
                } else {
                    incomes.setText(NumberFormat.getCurrencyInstance().format(incommes));
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "No hay incomes.", Toast.LENGTH_SHORT).show();
            }

            //SQLiteDatabase db = conn.getReadableDatabase();
            String[] campos2 = {UtilityMovement.PRECIO};
            try {
                Cursor cursor = db.query(UtilityMovement.TABLA_MOVEMENTS, campos2, UtilityMovement.TYPE + "=" + "0", null, null, null, null);
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

            balan = incom - outcom;
            // Llenado de Current Balance
            balance.setText(NumberFormat.getCurrencyInstance().format(balan));
            incom = 0; outcom = 0; balan = 0;
            incomdia = 0; incommes = 0;
            outcomdia = 0; outcommes = 0;
        }
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
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                   if (documentSnapshot.exists()) {
                       String miName = "No Name field.";
                       if (documentSnapshot.contains("uNombre")) {
                           miName = documentSnapshot.getString("uNombre");
                       }
                       tvStranger.setText(miName);
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
            } else if (periodo == 1) {
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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String miUid = currentUser.getUid();

            dbFirestore.collection("movimientos")
                    .whereEqualTo("uId", miUid)
                    .orderBy("mFechaMensual")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getContext(), R.string.msg_listenfailed, Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Listen failed on Reminders with Firebase.", error);
                        return;
                    }

                    reminders.clear();

                    for (QueryDocumentSnapshot doc : value) {
                        if (doc.get("mPrecio") != null && doc.getString("mNombre") != null && doc.getString("mFrecuencia") != null && doc.get("mFechaMensual") != null) {
                            //listaIncomes.add(doc.getDouble("mPrecio"));

                            reminders.add(new Reminder(doc.getString("mNombre"), NumberFormat.getCurrencyInstance().format(doc.getDouble("mPrecio"))));
                            //Log.d(TAG, "mFrecuencia desde doc.get: " + doc.getString("mFrecuencia") + ". Valor asociado: " + doc.get("mPrecio") + ".");
                        }
                    }
                    // ArrayAdapter of remainders
                    ReminderAdapter remindersAdapter = new ReminderAdapter(getActivity(), reminders);
                    listReminderView.setAdapter(remindersAdapter);
                }
            });
        }
        else {
            SQLiteDatabase db = conn.getReadableDatabase();
            try {
                Cursor cursor = db.rawQuery("select name,precio from movements where frequency != \"\"", null);
                while (cursor.moveToNext()) {
                    reminders.add(new Reminder(cursor.getString(0), NumberFormat.getCurrencyInstance().format(Integer.parseInt(cursor.getString(1)))));
                }
                //Log.d(TAG, "Reminders: " + reminders + ".");
            } catch (Exception e) {
                Toast.makeText(getContext(), "No hay recordatorios.", Toast.LENGTH_SHORT).show();
            }

            // ArrayAdapter of remainders
            ReminderAdapter remindersAdapter = new ReminderAdapter(getActivity(), reminders);
            listReminderView.setAdapter(remindersAdapter);
        }
    }

}