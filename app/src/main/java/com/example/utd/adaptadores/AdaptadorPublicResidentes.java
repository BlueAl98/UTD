package com.example.utd.adaptadores;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.utd.R;
import com.example.utd.VacantesResi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdaptadorPublicResidentes extends BaseAdapter {


    Context context;
    ArrayList<VacantesResi> vacantesResis;
    LayoutInflater layoutInflater;
    VacantesResi vacModel;
    ArrayList<VacantesResi> vacDataFilter;


    public AdaptadorPublicResidentes(Context context, ArrayList<VacantesResi> vacantesResis) {
        this.context = context;
        this.vacantesResis = vacantesResis;
        vacDataFilter = new ArrayList<>();
        vacDataFilter.addAll(vacantesResis);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return vacantesResis.size();
    }

    @Override
    public Object getItem(int position) {
        return vacantesResis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.modelo_public,null,true);


        }

        //Enlazar las vistas


        TextView titulo = rowView.findViewById(R.id.tvTituloMP);
        TextView carrera = rowView.findViewById(R.id.tvCarreraMP);


        vacModel = vacantesResis.get(position);




        titulo.setText("Titulo: "+ vacModel.getTitulo());
        carrera.setText("Carrera: "+vacModel.getCarrera());







        return rowView;

    }



    public void filtrado(String txtbuscar){

        int longitud = txtbuscar.length();

        if(longitud == 0){
            vacantesResis.clear();
          vacantesResis.addAll(vacDataFilter);

        }else{
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<VacantesResi> colleccion = vacantesResis.stream()
                        .filter(i -> i.getCarrera().toLowerCase()
                                .contains(txtbuscar.toLowerCase()))
                        .collect(Collectors.toList());
                vacantesResis.clear();
                vacantesResis.addAll(colleccion);

            }else{
                for (VacantesResi p : vacDataFilter){
                    if(p.getCarrera().toLowerCase().contains(txtbuscar.toLowerCase())){
                        vacantesResis.add(p);
                    }
                }
            }






        }
        notifyDataSetChanged();

    }





}
