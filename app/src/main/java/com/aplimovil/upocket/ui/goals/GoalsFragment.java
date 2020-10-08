package com.aplimovil.upocket.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
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

    public GoalsFragment() {
        // Required empty public constructor
    }

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


}