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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.LoginActivity;
import com.aplimovil.upocket.MainActivity;
import com.aplimovil.upocket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountFragment extends Fragment {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    TextView tvStranger, tvName, tvEmail, tvUid;
    Button btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        tvStranger = root.findViewById(R.id.user_text_view);
        tvName = root.findViewById(R.id.input_name_account);
        tvEmail = root.findViewById(R.id.input_email_account);
        tvUid = root.findViewById(R.id.input_uid_account);
        btnLogout = root.findViewById(R.id.button_logout_account);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

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
            obtenerDatos();

            btnLogout.setEnabled(true);
        }
        else {
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
        final String miUid = mAuth.getCurrentUser().getUid();
        final String miEmail = mAuth.getCurrentUser().getEmail();

        db.collection("usuarios").document(miUid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String miName = "No Name";
                    if (documentSnapshot.contains("uNombre")) {
                        miName = documentSnapshot.getString("uNombre");
                    }
                    tvStranger.setText(miName);
                }
            }
        });

        db.collection("usuarios").document(miUid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String miName = "No Name";
                    if (documentSnapshot.contains("uNombre")) {
                        miName = documentSnapshot.getString("uNombre");
                    }
                    tvName.setText(miName);
                    tvEmail.setText(miEmail);
                    tvUid.setText(miUid);
                }
                else {
                    Toast.makeText(getContext(), R.string.msg_error_documentsnapshot, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
