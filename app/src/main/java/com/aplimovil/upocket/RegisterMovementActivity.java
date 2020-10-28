package com.aplimovil.upocket;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;
import utilities.UtilityMovement;

public class RegisterMovementActivity extends AppCompatActivity {

    CheckBox frequently;
    EditText tipo, nombre, precio, fecha, frecuencia;
    Button crear;

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

        fecha.setVisibility(View.INVISIBLE);
        frecuencia.setVisibility(View.INVISIBLE);
        frequently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked == true) {
                    fecha.setVisibility(View.VISIBLE);
                    frecuencia.setVisibility(View.VISIBLE);
                } else {
                    fecha.setVisibility(View.INVISIBLE);
                    frecuencia.setVisibility(View.INVISIBLE);
                }
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarMovement();
            }
        });

    }

    private void registrarMovement() {
        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UtilityMovement.NAME, nombre.getText().toString());
        values.put(UtilityGoal.PRECIO, Integer.parseInt(precio.getText().toString()));
        values.put(UtilityMovement.TYPE, Integer.parseInt(tipo.getText().toString()));
        values.put(UtilityMovement.DATE, "");
        values.put(UtilityMovement.FREQUENCY, "");

        Long idResultante = db.insert(UtilityMovement.TABLA_MOVEMENTS, UtilityMovement.ID, values);
        Toast.makeText(this, "Id Registro" + idResultante, Toast.LENGTH_SHORT).show();
        db.close();
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