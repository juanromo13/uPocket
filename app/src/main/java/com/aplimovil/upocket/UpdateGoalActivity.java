package com.aplimovil.upocket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;

public class UpdateGoalActivity extends AppCompatActivity {

    EditText meta, precio;
    Button actualizar;
    String metaActualizar, precioInicial;
    Integer idActualizar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_goal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        meta = findViewById(R.id.meta);
        precio = findViewById(R.id.precioGoal);
        actualizar = findViewById(R.id.update_goal_button);

        Intent intent = getIntent();
        metaActualizar = intent.getStringExtra("NombreMeta");
        findIdGoal(metaActualizar);
        camposIniciales();
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarGoal();
                limpiarEditText();
            }
        });
    }

    private void camposIniciales() {
        meta.setText(metaActualizar);
        precio.setText(precioInicial);
    }

    private void findIdGoal(String meta_actualizar) {
        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getReadableDatabase();
        String [] args = new String[] {meta_actualizar};

        Cursor encontrar = db.rawQuery("select id, precio from goals where meta = ?;", args);
        try {
            encontrar.moveToFirst();
            idActualizar = Integer.parseInt(encontrar.getString(0));
            precioInicial = encontrar.getString(1);
        }
        catch (Exception e){
            Toast.makeText(UpdateGoalActivity.this, "No es posible actualizar la meta", Toast.LENGTH_SHORT).show();
        }

    }

    private void actualizarGoal() {
        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();
        //long rowId = 13;

        ContentValues values = new ContentValues();
        values.put(UtilityGoal.META, meta.getText().toString());
        values.put(UtilityGoal.PRECIO, Integer.parseInt(precio.getText().toString()));

        db.update(UtilityGoal.TABLA_GOALS, values, "id=" + idActualizar, null);
        Toast.makeText(this, "Actualizacion exitosa", Toast.LENGTH_SHORT).show();
        db.close();

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }
        return true;
    }

    private void limpiarEditText() {
        meta.setText("");
        precio.setText("");
    }
}
