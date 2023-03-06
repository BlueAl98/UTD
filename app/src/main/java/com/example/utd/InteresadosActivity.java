package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.utd.adaptadores.AdaptadorMensajes;
import com.example.utd.adaptadores.AdaptadorPublicResidentes;
import com.example.utd.adaptadores.AdapterPublicaciones;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InteresadosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private ListView lvlistainteresado;
    SearchView searchView;
    //Lista
    //Para la lista

    private ArrayList<MensajesResidentes> listInteresados = new ArrayList<MensajesResidentes>();
   AdaptadorMensajes listviewmensajes;


    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

   MensajesResidentes vacmensajesResidentes;


   //Obtener el email
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesados);

        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();


        lvlistainteresado = findViewById(R.id.lvmensajes);
        searchView =  findViewById(R.id.txbusquedaIn);

        listardatos();

        searchView.setOnQueryTextListener(this);


        lvlistainteresado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                String nodo = usuario.getEmail();
                int pos = nodo.indexOf(".");
                String nodoF = nodo.substring(0,pos);

                String nodoMensaje =  "Mensajes_"+nodoF;
                String idd= getIntent().getStringExtra("id");


                Intent intent = new Intent(getApplicationContext(),VerificacionSolicitudActivity.class);
              vacmensajesResidentes = (MensajesResidentes) parent.getItemAtPosition(position);
                intent.putExtra("id", vacmensajesResidentes.getId());
                intent.putExtra("uid", idd);
                intent.putExtra("nodo",nodoMensaje);








                startActivity(intent);

            }
        });



    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void listardatos(){

        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);

        String nodoMensaje =  "Mensajes_"+nodoF;

        String idd= getIntent().getStringExtra("id");


        databaseReference.child(nodoMensaje).child(idd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listInteresados.clear();


                for (DataSnapshot objdataSnapshot : snapshot.getChildren()){
                    MensajesResidentes men = objdataSnapshot.getValue(MensajesResidentes.class);
                    listInteresados.add(men);

                    listviewmensajes =  new AdaptadorMensajes(InteresadosActivity.this, listInteresados );
                    lvlistainteresado.setAdapter(listviewmensajes);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

      try {
          listviewmensajes.filtrado(newText);
      }catch (Exception e){
          Toast.makeText(this, "No hay elementos que mostrar", Toast.LENGTH_SHORT).show();
      }







        return false;
    }
}