package com.aplimovil.upocket;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import BD.ConexionSQLiteOpenHelper;
import entities.Goal;
import utilities.UtilityGoal;

public class RegisterGoalActivity extends AppCompatActivity {

    EditText meta, precio;
    Button crear;

    private FirebaseFirestore dbFirestore;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_goal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        meta = findViewById(R.id.meta);
        precio = findViewById(R.id.precioGoal);
        crear = findViewById(R.id.new_goal_button);

        // Initialize Firebase Firestore
        dbFirestore = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGoal();
                limpiarEditText();
            }
        });
    }

    private void registrarGoal() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String miUid = currentUser.getUid();

            String miMeta = meta.getText().toString();
            int miPrecio = Integer.parseInt(precio.getText().toString());

            Goal miGoal = new Goal(miMeta, miPrecio, miUid);

            dbFirestore.collection("goals").add(miGoal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), R.string.msg_goodnewgoal, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.msg_badnewgoal, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(UtilityGoal.META, meta.getText().toString());
            values.put(UtilityGoal.PRECIO, Integer.parseInt(precio.getText().toString()));

            Long idResultante = db.insert(UtilityGoal.TABLA_GOALS, UtilityGoal.ID, values);
            Toast.makeText(this, "Id Registro" + idResultante, Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    private void limpiarEditText() {
        meta.setText("");
        precio.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }
        return true;
    }
}