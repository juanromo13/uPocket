package com.aplimovil.upocket.ui.account;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AccountFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView tvName, tvEmail, tvDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        tvName = root.findViewById(R.id.input_name);
        tvEmail = root.findViewById(R.id.input_email);
        tvDate = root.findViewById(R.id.input_date);

        obtenerDatos();

        return root;
    }

    private void obtenerDatos() {

        db.document("usuarios/1")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String miName = documentSnapshot.getString("nombre");
                    String miEmail = documentSnapshot.getString("email");
                    String miDate = documentSnapshot.getTimestamp("fechanacimiento").toDate().toString();

                    tvName.setText(miName);
                    tvEmail.setText(miEmail);
                    tvDate.setText(miDate);

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Monitoreo BD");
                    alert.setMessage("DocumentSnapshot Existe!");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Monitoreo BD");
                    alert.setMessage("DocumentSnapshot NO Existe!");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
            }
        });
    }
}
