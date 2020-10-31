package com.aplimovil.upocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityMovement;

public class RegisterMovementActivity extends AppCompatActivity {

    CheckBox frequently;
    EditText tipo, nombre, precio, frecuencia, fecha;
    Button crear, boton_fecha;
    private int dia, mes, anio;

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
                if (frequently.isChecked() == true) {
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
        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UtilityMovement.NAME, nombre.getText().toString());
        values.put(UtilityMovement.PRECIO, Integer.parseInt(precio.getText().toString()));
        values.put(UtilityMovement.TYPE, Integer.parseInt(tipo.getText().toString()));
        values.put(UtilityMovement.DATE, fecha.getText().toString());
        values.put(UtilityMovement.FREQUENCY, frecuencia.getText().toString());
        Long idResultante = db.insert(UtilityMovement.TABLA_MOVEMENTS, UtilityMovement.ID, values);
        Toast.makeText(this, "Id Registro" + idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }

    private void limpiarEditText() {
        tipo.setText("");
        nombre.setText("");
        precio.setText("");
        fecha.setText("");
        frecuencia.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}