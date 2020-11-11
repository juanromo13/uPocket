package com.aplimovil.upocket.ui.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplimovil.upocket.Activity;
import com.aplimovil.upocket.ActivityAdapter;
import com.aplimovil.upocket.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityMovement;

import static android.content.ContentValues.TAG;

public class ActivityFragment extends Fragment {

    ArrayList<Activity> listaMovimientos = new ArrayList<>();
    RecyclerView recyclerMovements;
    ConexionSQLiteOpenHelper conn;
    ActivityAdapter adapter;

    private FirebaseAuth mAuth;

    private FirebaseFirestore dbFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerMovements = root.findViewById(R.id.recycler_view_activity_item);
        recyclerMovements.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Firestore
        dbFirestore = FirebaseFirestore.getInstance();

        conn = new ConexionSQLiteOpenHelper(getContext());

        adapter = new ActivityAdapter(listaMovimientos);

        consultarMovimientos();

        return root;
    }

    private void consultarMovimientos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String miUid = currentUser.getUid();

            dbFirestore.collection("movimientos")
                .whereEqualTo("uId", miUid)
                .orderBy("mFechaCreacion", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            //Toast.makeText(getContext(), R.string.msg_listenfailed, Toast.LENGTH_LONG).show();
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("mPrecio") != null && doc.get("mFechaMensual") == null) {
                                //listaIncomes.add(doc.getDouble("mPrecio"));
                                Timestamp miFecha;
                                Date miFecha2;
                                DateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");
                                String miFecha3 = null;

                                if (doc.getTimestamp("mFechaCreacion") != null) {
                                    miFecha = doc.getTimestamp("mFechaCreacion");
                                    miFecha2 = miFecha.toDate();
                                    miFecha3 = miFormato.format(miFecha2);
                                }

                                listaMovimientos.add(new Activity(doc.getString("mNombre"), NumberFormat.getCurrencyInstance().format(doc.getDouble("mPrecio")), miFecha3, Integer.parseInt(doc.getString("mTipo"))));
                            }
                        }
                        recyclerMovements.setAdapter(adapter);
                    }
                });
        }

        else {
            SQLiteDatabase db = conn.getReadableDatabase();
            String[] campos = {UtilityMovement.NAME, UtilityMovement.PRECIO, UtilityMovement.CREATED_AT, UtilityMovement.TYPE};
            try {
                Cursor cursor = db.query(UtilityMovement.TABLA_MOVEMENTS, campos, null, null, null, null, "id desc");
                while (cursor.moveToNext()) {
                    listaMovimientos.add(new Activity(cursor.getString(0), NumberFormat.getCurrencyInstance().format(Integer.parseInt(cursor.getString(1))), cursor.getString(2), Integer.parseInt(cursor.getString(3))));
                }
                recyclerMovements.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.msg_nomovements, Toast.LENGTH_SHORT).show();
            }
        }
    }
}