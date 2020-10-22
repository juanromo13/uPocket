package com.aplimovil.upocket.ui.goals;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplimovil.upocket.Goal;
import com.aplimovil.upocket.GoalAdapter;
import com.aplimovil.upocket.MainActivity;
import com.aplimovil.upocket.R;
import com.aplimovil.upocket.RegisterGoalActivity;

import java.text.NumberFormat;
import java.util.ArrayList;

import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;


public class GoalsFragment extends Fragment {

    ArrayList<Goal> listaMetas = new ArrayList<>();
    RecyclerView recyclerGoals;
    ConexionSQLiteOpenHelper conn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);



        recyclerGoals = root.findViewById(R.id.recycler_view_goal_item);
        recyclerGoals.setLayoutManager(new LinearLayoutManager(getContext()));


        conn = new ConexionSQLiteOpenHelper(getContext());

        Consultar();
        GoalAdapter adapter = new GoalAdapter(listaMetas);
        recyclerGoals.setAdapter(adapter);

        return root;
    }

    private void Consultar(){
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] campos = {UtilityGoal.META , UtilityGoal.PRECIO};
        try {
            Cursor cursor = db.query(UtilityGoal.TABLA_GOALS, campos, null, null, null, null, null);
            while(cursor.moveToNext()) {
                listaMetas.add(new Goal(cursor.getString(0), NumberFormat.getCurrencyInstance().format(Integer.parseInt(cursor.getString(1))), NumberFormat.getCurrencyInstance().format(10)));
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(), "No hay metas.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.goals_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addItem) {
            Intent registerIntent = new Intent(getContext(), RegisterGoalActivity.class);
            startActivity(registerIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}