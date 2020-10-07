package com.aplimovil.upocket;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolderMeta> {

    ArrayList<Goal> listametas;

    public GoalAdapter(ArrayList<Goal> listaMetas) {
        this.listametas = listaMetas;
    }

    @NonNull
    @Override
    public ViewHolderMeta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_goals, null, false);
        return new ViewHolderMeta(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMeta holder, int position) {
        holder.meta.setText("Pantalla");
        holder.restante.setText("10");
        holder.precio.setText("40");
    }

    @Override
    public int getItemCount() {
        return getListaUsuarios().size();
    }


    public class ViewHolderMeta extends RecyclerView.ViewHolder {
        TextView meta;
        TextView restante;
        TextView precio;


        public ViewHolderMeta(@NonNull View itemView) {
            super(itemView);
            meta = itemView.findViewById(R.id.meta_textView);
            restante = itemView.findViewById(R.id.restante_textView);
            precio = itemView.findViewById(R.id.precio_textView);
        }
    }

    public ArrayList<Goal> getListaUsuarios() {
        if (listametas == null)
            return new ArrayList<Goal>();
        return listametas;
    }
}