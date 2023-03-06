package com.example.utd.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.utd.R;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class DialogModelos {






    public  interface FinalizoCuadroDialog{

        void ResultadoCuadroDialog(String nombre, String carrera, String cv);
    }

    private FinalizoCuadroDialog interfaz;



    public DialogModelos(Context context , FinalizoCuadroDialog actividad) {





        interfaz = actividad;

        final Dialog dialogo =  new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.modal_dialog);

        final EditText nombre = (EditText) dialogo.findViewById(R.id.tvmnombre);
        final EditText carrera = (EditText) dialogo.findViewById(R.id.tvmcarrera);
        final EditText cv = (EditText) dialogo.findViewById(R.id.tvmcv);

        Button aceptar = (Button) dialogo.findViewById(R.id.bmenviar);
        Button salir = (Button) dialogo.findViewById(R.id.bmsalir);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               interfaz.ResultadoCuadroDialog(nombre.getText().toString(), carrera.getText().toString(),
                       cv.getText().toString());

                dialogo.dismiss();



            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogo.dismiss();
            }
        });

        dialogo.show();


    }


}
