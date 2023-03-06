package com.example.utd.adaptadores;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.utd.MensajesResidentes;
import com.example.utd.R;
import com.example.utd.VacantesResi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdaptadorMensajes extends BaseAdapter {
    Context context;
    ArrayList<MensajesResidentes> mensajesResidentes;
    LayoutInflater layoutInflater;
    MensajesResidentes vacmensajes;
    ArrayList<MensajesResidentes> menDataFilter;


    public AdaptadorMensajes(Context context, ArrayList<MensajesResidentes> mensajesResidentes){
        this.context = context;
        this.mensajesResidentes = mensajesResidentes;

        menDataFilter = new ArrayList<>();
        menDataFilter.addAll(mensajesResidentes);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public int getCount() {
       return mensajesResidentes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajesResidentes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.modelo_interesados_lista, null, true);

        }

        //Enlazar las vistas


        TextView nombre = rowView.findViewById(R.id.tvInombre);
        TextView carrera = rowView.findViewById(R.id.tvIcarrera);


        vacmensajes = mensajesResidentes.get(position);


       nombre.setText("Nombre: "+ vacmensajes.getNombre());
        carrera.setText("Carrera: "+vacmensajes.getCarrera());







        return rowView;





    }


    public void filtrado(String txtbuscar){



        int longitud = txtbuscar.length();

        if(longitud == 0){
            mensajesResidentes.clear();
            mensajesResidentes.addAll(menDataFilter);

        }else{
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<MensajesResidentes> colleccion = mensajesResidentes.stream()
                        .filter(i -> i.getNombre().toLowerCase()
                                .contains(txtbuscar.toLowerCase()))
                        .collect(Collectors.toList());
                mensajesResidentes.clear();
                mensajesResidentes.addAll(colleccion);

            }else{
                for (MensajesResidentes p : mensajesResidentes){
                    if(p.getNombre().toLowerCase().contains(txtbuscar.toLowerCase())){
                        mensajesResidentes.add(p);
                    }
                }
            }






        }
        notifyDataSetChanged();

    }
}
