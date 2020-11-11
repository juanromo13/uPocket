package com.aplimovil.upocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityMovement;

public class RegisterMovementActivity extends AppCompatActivity {

    CheckBox frequently;
    EditText tipo, nombre, precio, frecuencia, fecha;
    Button crear, boton_fecha;
    private int dia, mes, anio;

    private FirebaseFirestore dbFirestore;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movement);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nombre = findViewById(R.id.nombre);
        precio = findViewById(R.id.precioMovement);
        tipo = findViewById(R.id.tipo);
        fecha = findViewById(R.id.fecha);
        frecuencia = findViewById(R.id.frecuencia);
        frequently = findViewById(R.id.checkbox_obligation);
        crear = findViewById(R.id.new_movement_button);
        boton_fecha = findViewById(R.id.boton_fecha);

        // Visibilidad
        boton_fecha.setVisibility(View.INVISIBLE);
        fecha.setVisibility(View.INVISIBLE);
        frecuencia.setVisibility(View.INVISIBLE);
        frequently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frequently.isChecked()) {
                    boton_fecha.setVisibility(View.VISIBLE);
                    fecha.setVisibility(View.VISIBLE);
                    frecuencia.setVisibility(View.VISIBLE);
                } else {
                    boton_fecha.setVisibility(View.INVISIBLE);
                    fecha.setVisibility(View.INVISIBLE);
                    frecuencia.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Initialize Firebase Firestore
        dbFirestore = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        boton_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DATE);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterMovementActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, dia, mes, anio);
                datePickerDialog.show();
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarMovement();
                limpiarEditText();
            }
        });

    }

    private void registrarMovement() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String miUid = currentUser.getUid();

            // Se crea un obejeto de Movimiento para guardarlo en la dbFirestore
            Map<String, Object> miMovimiento = new HashMap<>();
            miMovimiento.put("mFechaCreacion", Timestamp.now());
            miMovimiento.put("mNombre", nombre.getText().toString());
            miMovimiento.put("mPrecio", Integer.parseInt(precio.getText().toString()));
            miMovimiento.put("mTipo", tipo.getText().toString());
            miMovimiento.put("uId", miUid);

            // Se verifica si es frecuente o no. Si no es frecuente, la mFechaMensual es tipo null.
            if (fecha.length() > 1) {
                DateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");
                Date miFecha = null;
                try {
                    miFecha = miFormato.parse(fecha.getText().toString());
                } catch (ParseException e) { e.printStackTrace(); }

                miMovimiento.put("mFechaMensual", miFecha);
                miMovimiento.put("mFrecuencia", frecuencia.getText().toString()); // 0 Quincenal, 1 Mensual, 2 Anual.
            }
            else {
                miMovimiento.put("mFechaMensual", null);
                miMovimiento.put("mFrecuencia", null);
            }

            dbFirestore.collection("movimientos")
                    .add(miMovimiento)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), R.string.msg_goodnewmovement, Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.msg_badnewmovement, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(UtilityMovement.NAME, nombre.getText().toString());
            values.put(UtilityMovement.PRECIO, Integer.parseInt(precio.getText().toString()));
            values.put(UtilityMovement.TYPE, Integer.parseInt(tipo.getText().toString()));
            values.put(UtilityMovement.DATE, fecha.getText().toString());
            values.put(UtilityMovement.FREQUENCY, frecuencia.getText().toString());
            Long idResultante = db.insert(UtilityMovement.TABLA_MOVEMENTS, UtilityMovement.ID, values);
            Toast.makeText(this, R.string.msg_goodnewmovement, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, R.string.msg_goodnewmovement + ". " + R.string.msg_asignedid + ": " + idResultante, Toast.LENGTH_LONG).show();
            db.close();
        }
    }

    private void limpiarEditText() {
        tipo.setText("");
        nombre.setText("");
        precio.setText("");
        fecha.setText("");
        frecuencia.setText("");
    }

}