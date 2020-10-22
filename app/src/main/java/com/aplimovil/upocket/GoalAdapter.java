package com.aplimovil.upocket;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolderMeta> {

    ArrayList<Goal> listaMetas;

    public GoalAdapter(ArrayList<Goal> listaMetas) {
        this.listaMetas = listaMetas;
    }

    @NonNull
    @Override
    public ViewHolderMeta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_view_item, null, false);
        return new ViewHolderMeta(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMeta holder, int position) {
        holder.meta.setText(listaMetas.get(position).getMeta());
        holder.restante.setText(listaMetas.get(position).getRestante());
        holder.precio.setText(listaMetas.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        return listaMetas.size();
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

}