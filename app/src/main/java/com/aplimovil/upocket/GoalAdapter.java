package com.aplimovil.upocket;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import BD.ConexionSQLiteOpenHelper;
import utilities.UtilityGoal;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Arrays;

import static androidx.core.content.ContextCompat.startActivity;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolderMeta> {

    ArrayList<Goal> listaMetas;

    //Nuevas variables para el delete y update de goals;
    ConexionSQLiteOpenHelper conn;

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
    public void onBindViewHolder(@NonNull final ViewHolderMeta holder, final int position) {
        holder.meta.setText(listaMetas.get(position).getMeta());
        holder.restante.setText(listaMetas.get(position).getRestante());
        holder.precio.setText(listaMetas.get(position).getPrecio());


//        New methods

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                //Toast.makeText(view.getContext(), "OnLongClick called on position "+position, Toast.LENGTH_SHORT).show();
                conn = new ConexionSQLiteOpenHelper(view.getContext());
                //AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Que desea hacer con la meta seleccionada?").setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String meta_eliminar = listaMetas.get(position).getMeta();

                        removeItem(meta_eliminar, view);
                        Toast.makeText(view.getContext(), "Meta eliminada", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String meta_actualizar = listaMetas.get(position).getMeta();
                        editarItem(view, meta_actualizar);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }

            private void editarItem(View view, String meta_actualizar) {
                Intent registerIntent = new Intent(view.getContext(), UpdateGoalActivity.class);
                registerIntent.putExtra("NombreMeta", meta_actualizar);
                view.getContext().startActivity(registerIntent);
            }

            private void removeItem(String metaEliminar, View view) {
                SQLiteDatabase db = conn.getReadableDatabase();
                String [] args = new String[]{metaEliminar};
                Cursor eliminar = db.rawQuery("delete from goals where meta = ?", args);
                try{
                    eliminar.moveToFirst();
                }
                catch (Exception e)
                {
                    Toast.makeText(view.getContext(), "No existe el goal", Toast.LENGTH_SHORT).show();
                }
                Goal goalDrop = listaMetas.get(position);
                listaMetas.remove(goalDrop);
            }
        });
//        Fin new methods

    }

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