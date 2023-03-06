package com.example.utd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.utd.residentes.PublicResidentes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private Button btn_empresa ,  btnResidente;

    //Objeto de firebase
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                /* si esta activo el modo oscuro lo desactiva */
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }


        onfirst();

        //inicialize
        firebaseAuth = FirebaseAuth.getInstance();


        btn_empresa = findViewById(R.id.btn_empresa);
        btnResidente = findViewById(R.id.bresidentes);






        //Si presiona el boton alumno empresa
        btn_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),LoginActivity.class);
                startActivity(intent);
            }
        });




      btnResidente.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getApplication(), PublicResidentes.class);
              startActivity(intent);
          }
      });


    }


    @Override
    protected void onStart() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user!=null){
            Intent intent = new Intent(getApplication(),PerfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);


        }

        super.onStart();


    }



    public void onfirst(){


        String terminos = " \n" +
                "\n" +
                "Cualquier persona que desee hacer uso de los servicios que ofrece esta aplicación móvil, podrá hacerlo sujetándose a los presentes “Términos y Condiciones de Uso” así como a las políticas y principios que se describen en el presente documento.\n" +
                "\n" +
               "I. ACEPTACIÓN DE LOS TÉRMINOS \n" +
                "\n" +
                "El ingreso y utilización de la aplicación \"Estadías UTD\" implica que usted ha leído, entendido y aceptado los presentes “Términos y Condiciones”. \n" +
                "Y esta aplicación requerirá la autorización expresa de los usuarios para acceder a las funciones como Almacenamiento local, Internet y sesiones para brindar atención y seguimiento a los trámites y servicios solicitados en alguno de los módulos de la aplicación.\n" +
                "\n" +
                "II. RESPONSABILIDAD SOBRE EL USO DE LA APLICACIÓN \"Estadías UTD\" \n" +
                "\n" +
                "Usted asume la responsabilidad de todas las acciones que ejecute, en la utilización y consulta de la aplicación \"Estadías UTD\", mismas que se considera que usted realiza voluntariamente. Usted no utilizará, ni permitirá que otros usuarios utilicen esta aplicación móvil o cualquier servicio prestado a través de ella, de forma contraria a lo establecido en estos “Términos y Condiciones de Uso”, así como a las disposiciones legales aplicables a los servicios que ofrece la aplicación \"Estadías UTD\". Es responsabilidad de usted todo acto que se realice en esta aplicación móvil, por medio de su “Nombre de Usuario” y “Contraseña”; así mismo, usted se responsabiliza del cuidado y custodia de los mismos, para que esta aplicación móvil únicamente sea utilizada para los fines para los cuales ha sido diseñada, por lo que cualquier mal uso que se realice en su nombre, se presumirá realizada por usted.\n" +
                "\n" +
                "El ingreso y utilización de \"Estadías UTD\" implica que ha leído y aceptado los presentes “TÉRMINOS Y CONDICIONES DE USO”\n";


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun",true);

        if(isFirstRun){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("TÉRMINOS Y CONDICIONES")
                    .setMessage(terminos)
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                       finish();
                       System.exit(0);
                        }
                    })
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();


                        }
                    }).show();
        }



    }



}