package com.aplimovil.upocket.ui.goals;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aplimovil.upocket.Goal;
import com.aplimovil.upocket.GoalAdapter;
import com.aplimovil.upocket.R;

import java.util.ArrayList;


public class GoalsFragment extends Fragment {

    ArrayList<Goal> listaMetas;
    RecyclerView recyclerGoals;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);


        listaMetas = new ArrayList<>();
        recyclerGoals = root.findViewById(R.id.recycler_view_goal_item);
        recyclerGoals.setLayoutManager(new LinearLayoutManager(getContext()));

        listaMetas.add(new Goal("Pantalla","40","10"));
        listaMetas.add(new Goal("Pantalla","40","10"));
        listaMetas.add(new Goal("Pantalla","40","10"));
        listaMetas.add(new Goal("Pantalla","40","10"));

        GoalAdapter adapter = new GoalAdapter(listaMetas);
        recyclerGoals.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.goals_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addItem){
            Toast.makeText(getActivity(), "hola", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}