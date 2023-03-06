package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.utd.residentes.EnvioDatosResidentesActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ResidentesActivity extends AppCompatActivity {


    private TextView  tvtitulo, tvcarrera, tvdescripcion, tvrequisitos, tvContador;
    private Button benviar;


    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residentes);



        inicializarFirebase();

        tvtitulo = findViewById(R.id.tvtituloG);
        tvcarrera = findViewById(R.id.tvRCarrera);
        tvdescripcion = findViewById(R.id.tvRDescrpcion);
        tvrequisitos = findViewById(R.id.tvRRequisitos);
        tvContador =  findViewById(R.id.tvContador);

        benviar = findViewById(R.id.binteresa);

        String email = getIntent().getStringExtra("email");
        String tituloo = getIntent().getStringExtra("titulo");
        String carrera= getIntent().getStringExtra("carrera");
        String descripcion= getIntent().getStringExtra("descripcion");
        String requisitos = getIntent().getStringExtra("requisitos");
        String idAS= getIntent().getStringExtra("idAS");


        int pos = email.indexOf(".");
        String emailF = email.substring(0,pos);



        tvtitulo.setText(tituloo);
        tvcarrera.setText("Careras solicitas: "+carrera);
        tvrequisitos.setText("Requisitos: "+requisitos);
        tvdescripcion.setText("Descripcion: "+descripcion);






       numeroInteresados();



        benviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), EnvioDatosResidentesActivity.class);
                intent.putExtra("email", emailF);
                intent.putExtra("titulo",tituloo);
                intent.putExtra("idAS",idAS);
                startActivity(intent);
            }
        });


    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }



public void numeroInteresados(){
    String idAS= getIntent().getStringExtra("idAS");
    String nodo = getIntent().getStringExtra("email");


    int pos = nodo.indexOf(".");
    String nodoF = nodo.substring(0,pos);

    String nodoMensaje =  "Mensajes_"+nodoF;




    databaseReference.child(nodoMensaje).child(idAS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                long numHijos = snapshot.getChildrenCount();
                String contador = ""+numHijos;

                tvContador.setText("Numero solicitantes: "+contador);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
}




}