package com.barbacil.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import objectClasses.Correo;

public class CorreoAdapter extends RecyclerView.Adapter<CorreoAdapter.CorreoViewHolder> {

    private static ArrayList<Correo> listaCorreos;
    private OnItemClickListener mListener;

    // Constructor
    public CorreoAdapter(ArrayList<Correo> listaCorreos) {
        this.listaCorreos = listaCorreos;
    }

    // Interfaz para manejar clics en elementos de la lista
    public interface OnItemClickListener {
        void onItemClick(Correo correo);
    }

    // Método para establecer el listener de clics
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Clase ViewHolder para representar cada elemento de la lista
    public static class CorreoViewHolder extends RecyclerView.ViewHolder {
        public TextView remitenteTextView;
        public TextView asuntoTextView;
        public TextView mensajeTextView;

        public CorreoViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            remitenteTextView = itemView.findViewById(R.id.textViewEmisor);
            asuntoTextView = itemView.findViewById(R.id.textViewTitulo);
            mensajeTextView = itemView.findViewById(R.id.textViewMensaje);

            // Configurar el listener de clic en el elemento de la lista
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listaCorreos.get(position));
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public CorreoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_correo, parent, false);
        return new CorreoViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CorreoViewHolder holder, int position) {
        Correo correoActual = listaCorreos.get(position);
        holder.remitenteTextView.setText(correoActual.getCorreoemisor());
        holder.asuntoTextView.setText(correoActual.getTitulomensaje());
    }

    @Override
    public int getItemCount() {
        return listaCorreos.size();
    }
}
