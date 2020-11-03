package com.aplimovil.upocket.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.LoginActivity;
import com.aplimovil.upocket.MainActivity;
import com.aplimovil.upocket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    TextView tvName, tvEmail, tvDate;
    Button btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        tvName = root.findViewById(R.id.input_name_account);
        tvEmail = root.findViewById(R.id.input_email_account);
        tvDate = root.findViewById(R.id.input_date_account);
        btnLogout = root.findViewById(R.id.button_logout_account);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                //Toast.makeText(getContext(), R.string.msg_logout, Toast.LENGTH_LONG).show();

                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

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
            Toast.makeText(getContext(), R.string.msg_autenticado, Toast.LENGTH_LONG).show();

            obtenerDatos();

            btnLogout.setEnabled(true);
        }
        else {
            Toast.makeText(getContext(), R.string.msg_noautenticado, Toast.LENGTH_LONG).show();

            btnLogout.setEnabled(false);

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(R.string.msg_login);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
            alert.setNegativeButton(R.string.btn_nologin, null);
            alert.show();
        }
    }

    private void obtenerDatos() {
        final String miId = mAuth.getCurrentUser().getUid();
        final String miEmail = mAuth.getCurrentUser().getEmail();

        db.collection("usuarios").document(miId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String miName = documentSnapshot.getString("uNombre");
                    //String miDate = documentSnapshot.getTimestamp("uFechaNacimiento").toDate().toString();

                    tvName.setText(miName);
                    tvEmail.setText(miEmail);
                    //tvDate.setText(miDate);

                    //Toast.makeText(getContext(), "DocumentSnapshot existe!", Toast.LENGTH_LONG).show();
                }
                else {
                    //Toast.makeText(getContext(), "DocumentSnapshot NO existe!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
