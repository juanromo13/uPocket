package com.aplimovil.upocket.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aplimovil.upocket.R;

public class GoalsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goals, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        textView.setText(R.string.title_goals);

        return root;
    }
}