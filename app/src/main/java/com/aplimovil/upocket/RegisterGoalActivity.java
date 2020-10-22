package com.aplimovil.upocket;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;

public class RegisterGoalActivity extends AppCompatActivity {

    EditText meta, precio;
    Button crear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_goal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        meta = findViewById(R.id.meta);
        precio = findViewById(R.id.precio);
        crear = findViewById(R.id.new_goal_button);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGoal();
            }
        });
    }

    private void registrarGoal() {

        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UtilityGoal.META, meta.getText().toString());
        values.put(UtilityGoal.PRECIO, Integer.parseInt(precio.getText().toString()));

        Long idResultante = db.insert(UtilityGoal.TABLA_GOALS, UtilityGoal.ID, values);
        Toast.makeText(this, "Id Registro"+idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent prueba = new Intent(RegisterGoalActivity.this, MainActivity.class);
                startActivity(prueba);
                //finish()
            break;
        }
        return true;
    }
}