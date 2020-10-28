package com.aplimovil.upocket;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterMovementActivity extends AppCompatActivity {

    CheckBox frequently;
    EditText fecha, frecuencia;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movement);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fecha = findViewById(R.id.fecha);
        frecuencia = findViewById(R.id.frecuencia);
        fecha.setVisibility(View.INVISIBLE);
        frecuencia.setVisibility(View.INVISIBLE);
        frequently = findViewById(R.id.checkbox_obligation);

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