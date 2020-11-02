package com.aplimovil.upocket.ui.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplimovil.upocket.Activity;
import com.aplimovil.upocket.ActivityAdapter;
import com.aplimovil.upocket.Goal;
import com.aplimovil.upocket.GoalAdapter;
import com.aplimovil.upocket.R;

import java.text.NumberFormat;
import java.util.ArrayList;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;
import utilities.UtilityMovement;

public class ActivityFragment extends Fragment {

    ArrayList<Activity> listaMovimientos = new ArrayList<>();
    RecyclerView recyclerMovements;
    ConexionSQLiteOpenHelper conn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerMovements = root.findViewById(R.id.recycler_view_activity_item);
        recyclerMovements.setLayoutManager(new LinearLayoutManager(getContext()));

        conn = new ConexionSQLiteOpenHelper(getContext());

        consultarMovimientos();

        ActivityAdapter adapter = new ActivityAdapter(listaMovimientos);
        recyclerMovements.setAdapter(adapter);

        return root;
    }

    private void consultarMovimientos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] campos = {UtilityMovement.NAME, UtilityMovement.PRECIO, UtilityMovement.CREATED_AT, UtilityMovement.TYPE};
        try {
            Cursor cursor = db.query(UtilityMovement.TABLA_MOVEMENTS, campos, null, null, null, null, "id desc");
            while (cursor.moveToNext()) {
                listaMovimientos.add(new Activity(cursor.getString(0), NumberFormat.getCurrencyInstance().format(Integer.parseInt(cursor.getString(1))), cursor.getString(2), Integer.parseInt(cursor.getString(3))));
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "No hay metas.", Toast.LENGTH_SHORT).show();
        }
    }
}