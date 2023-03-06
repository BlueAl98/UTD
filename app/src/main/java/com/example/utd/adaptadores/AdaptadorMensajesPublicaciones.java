package com.example.utd.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.utd.MensajesResidentes;
import com.example.utd.R;
import com.example.utd.TitulosPublicacion;

import java.util.ArrayList;

public class AdaptadorMensajesPublicaciones extends BaseAdapter {

    Context context;
    ArrayList<TitulosPublicacion> titulosPublicacions;
    LayoutInflater layoutInflater;
    TitulosPublicacion vactitulos;


    public AdaptadorMensajesPublicaciones(Context context, ArrayList<TitulosPublicacion> titulosPublicacions){
        this.context = context;
        this.titulosPublicacions = titulosPublicacions;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return titulosPublicacions.size();
    }

    @Override
    public Object getItem(int position) {
        return titulosPublicacions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.modelo_mensajes_publicaciones, null, true);

        }

        //Enlazar las vistas


        TextView nombre = rowView.findViewById(R.id.tituloMensaje);



        vactitulos = titulosPublicacions.get(position);





        nombre.setText("Titulo: "+ vactitulos.getIdTitulo());








        return rowView;


    }
}
