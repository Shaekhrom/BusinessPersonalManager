package com.barbacil.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import objectClasses.Evento;

public class EventoAdapter extends ArrayAdapter<Evento> {

    public EventoAdapter(Context context, List<Evento> eventos) {
        super(context, 0, eventos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtenemos el evento en la posición actual
        Evento evento = getItem(position);

        // Inflamos el layout de la vista del elemento de lista si aún no ha sido inflado
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_evento, parent, false);
        }

        // Obtenemos las referencias a los TextView en la vista del elemento de lista
        TextView textViewNombreEvento = convertView.findViewById(R.id.textViewNombreEvento);
        TextView textViewFechaEvento = convertView.findViewById(R.id.textViewFechaEvento);
        TextView textViewDetallesEvento = convertView.findViewById(R.id.textViewDetallesEvento);

        textViewNombreEvento.setText(evento.getNombreEvento());
        textViewFechaEvento.setText(evento.getFecha());
        textViewDetallesEvento.setText(evento.getDetallesEvento());
        return convertView;
    }

}

