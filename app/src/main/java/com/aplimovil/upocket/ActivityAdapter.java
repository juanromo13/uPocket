package com.aplimovil.upocket;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolderActivity>{

    ArrayList<Activity> listaMovimientos;

    public ActivityAdapter(ArrayList<Activity> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }


    @NonNull
    @Override
    public ViewHolderActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_item, null, false);
        return new ViewHolderActivity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderActivity holder, int position) {
        holder.movimiento.setText(listaMovimientos.get(position).getMovimiento());
        holder.valor.setText(listaMovimientos.get(position).getValor());
        if(listaMovimientos.get(position).getTipo() == 1) {
            holder.valor.setTextColor(Color.parseColor("#16A335"));
        } else {
            holder.valor.setTextColor(Color.parseColor("#A71F1F"));
        }
        holder.fecha.setText(listaMovimientos.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return listaMovimientos.size();
    }

    public class ViewHolderActivity extends RecyclerView.ViewHolder {
        TextView movimiento;
        TextView valor;
        TextView fecha;

        public ViewHolderActivity(@NonNull View itemView) {
            super(itemView);
            movimiento = itemView.findViewById(R.id.movimiento_textView);
            valor = itemView.findViewById(R.id.valor_textView);
            fecha = itemView.findViewById(R.id.fecha_textView);
        }
    }
}
