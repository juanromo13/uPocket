package com.aplimovil.upocket.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplimovil.upocket.Goal;
import com.aplimovil.upocket.GoalAdapter;
import com.aplimovil.upocket.MainActivity;
import com.aplimovil.upocket.R;
import com.aplimovil.upocket.databinding.ActivityMainBinding;
import com.aplimovil.upocket.databinding.GoalViewItemBinding;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    ArrayList<Goal> listametas;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        listametas = new ArrayList<>();
        listametas.add(new Goal("Pantalla","40","10"));
        listametas.add(new Goal("Pantalla","40","10"));
        listametas.add(new Goal("Pantalla","40","10"));
        listametas.add(new Goal("Pantalla","40","10"));

        GoalAdapter adapter = new GoalAdapter(listametas);

        recyclerView = root.findViewById(R.id.recycler_view_goal_item);

        recyclerView.setAdapter(adapter);

        return root;
    }


}