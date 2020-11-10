package com.aplimovil.upocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import entities.User;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    TextView tvName, tvEmail, tvPassword, tvRepeatPassword;
    Button btnRegister;

    private String miNombre = "";
    private String miEmail = "";
    private String miPassword = "";
    private String miRepeatPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        tvName = (EditText) findViewById(R.id.input_name);
        tvEmail = (EditText) findViewById(R.id.input_email);
        tvPassword = (EditText) findViewById(R.id.input_password);
        tvRepeatPassword = (EditText) findViewById(R.id.input_repeat_password);
        btnRegister = findViewById(R.id.bottom_complete_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miNombre = tvName.getText().toString();
                miEmail = tvEmail.getText().toString();
                miPassword = tvPassword.getText().toString();
                miRepeatPassword = tvRepeatPassword.getText().toString();

                if (!miNombre.isEmpty() && !miEmail.isEmpty() && !miPassword.isEmpty() && !miRepeatPassword.isEmpty()) {
                    if (miPassword.equals(miRepeatPassword)) {
                        registrarUsuario();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, R.string.msg_nopasswordsregister, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, R.string.msg_fieldsincompletes, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Toast.makeText(RegisterActivity.this, R.string.msg_activesesion, Toast.LENGTH_LONG).show();

            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    private void registrarUsuario() {
        mAuth.createUserWithEmailAndPassword(miEmail, miPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = mAuth.getCurrentUser().getUid();

                    User miUser = new User(miNombre);

                    db.collection("usuarios").document(uid)
                            .set(miUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                            alert.setMessage(R.string.msg_completeregister);
                            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                            alert.show();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, R.string.msg_badregister, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}