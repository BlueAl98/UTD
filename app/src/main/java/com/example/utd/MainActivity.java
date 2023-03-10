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
                "Cualquier persona que desee hacer uso de los servicios que ofrece esta aplicaci??n m??vil, podr?? hacerlo sujet??ndose a los presentes ???T??rminos y Condiciones de Uso??? as?? como a las pol??ticas y principios que se describen en el presente documento.\n" +
                "\n" +
               "I. ACEPTACI??N DE LOS T??RMINOS \n" +
                "\n" +
                "El ingreso y utilizaci??n de la aplicaci??n \"Estad??as UTD\" implica que usted ha le??do, entendido y aceptado los presentes ???T??rminos y Condiciones???. \n" +
                "Y esta aplicaci??n requerir?? la autorizaci??n expresa de los usuarios para acceder a las funciones como Almacenamiento local, Internet y sesiones para brindar atenci??n y seguimiento a los tr??mites y servicios solicitados en alguno de los m??dulos de la aplicaci??n.\n" +
                "\n" +
                "II. RESPONSABILIDAD SOBRE EL USO DE LA APLICACI??N \"Estad??as UTD\" \n" +
                "\n" +
                "Usted asume la responsabilidad de todas las acciones que ejecute, en la utilizaci??n y consulta de la aplicaci??n \"Estad??as UTD\", mismas que se considera que usted realiza voluntariamente. Usted no utilizar??, ni permitir?? que otros usuarios utilicen esta aplicaci??n m??vil o cualquier servicio prestado a trav??s de ella, de forma contraria a lo establecido en estos ???T??rminos y Condiciones de Uso???, as?? como a las disposiciones legales aplicables a los servicios que ofrece la aplicaci??n \"Estad??as UTD\". Es responsabilidad de usted todo acto que se realice en esta aplicaci??n m??vil, por medio de su ???Nombre de Usuario??? y ???Contrase??a???; as?? mismo, usted se responsabiliza del cuidado y custodia de los mismos, para que esta aplicaci??n m??vil ??nicamente sea utilizada para los fines para los cuales ha sido dise??ada, por lo que cualquier mal uso que se realice en su nombre, se presumir?? realizada por usted.\n" +
                "\n" +
                "El ingreso y utilizaci??n de \"Estad??as UTD\" implica que ha le??do y aceptado los presentes ???T??RMINOS Y CONDICIONES DE USO???\n";


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun",true);

        if(isFirstRun){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("T??RMINOS Y CONDICIONES")
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