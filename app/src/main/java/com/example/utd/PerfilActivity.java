package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {


    public static final String user="names";
    private TextView usurio;
    private ImageButton bcerrar;
    private ImageView bagreagar, blistapublic, bperfil, binteresados;


    FirebaseAuth firebaseAuth;


    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                /* si esta activo el modo oscuro lo desactiva */
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }



     usurio =findViewById(R.id.tvuser);
     bcerrar = findViewById(R.id.bout);
     bagreagar = findViewById(R.id.btnPublicaciones);
     blistapublic = findViewById(R.id.btnlistaPublic);

     binteresados = findViewById(R.id.binteresados);

        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();








        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);







   databaseReference.child("users").child(nodoF+"_key").addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

           String userFF = snapshot.child("nombre").getValue().toString();

           usurio.setText(userFF);

       }

       @Override
       public void onCancelled(@NonNull @NotNull DatabaseError error) {

       }
   });





   bcerrar.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {


           firebaseAuth.signOut();
           startActivity(new Intent(PerfilActivity.this,LoginActivity.class)
                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
           finish();



       }
   });


   bagreagar.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {


           Intent intent = new Intent(getApplication(),publicacionesActivity.class);

           startActivity(intent);


       }
   });


   blistapublic.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           Intent intent = new Intent(getApplication(),EmpresaPublicacionesActivity.class);

           startActivity(intent);
       }
   });



   binteresados.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(getApplication(),EmpresaInteresadosPublicaciones.class);

           startActivity(intent);
       }
   });



    }



   private void inicializarFirebase() {
       FirebaseApp.initializeApp(this);
       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference();
   }



}