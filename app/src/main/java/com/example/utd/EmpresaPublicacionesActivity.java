package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class EmpresaPublicacionesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

 private TextView tvpcorreo;
 ListView listV;
 VacantesResi vacanteSelected;
 SearchView searchView;




   //Para la lista

    private ArrayList<VacantesResi> listVacantes = new ArrayList<VacantesResi>();
    ArrayAdapter<VacantesResi> arrayAdapterVacantes;
    AdapterPublicaciones listviewvacantesAdapter;

    FirebaseAuth firebaseAuth;
    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_publicaciones);

        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();

        FirebaseUser usuario = firebaseAuth.getCurrentUser();


        tvpcorreo = findViewById(R.id.tvepCorreo);

        listV = findViewById(R.id.listVacantes);
        searchView = findViewById(R.id.txbusquedaP);


        tvpcorreo.setText(usuario.getEmail());

        listarDatos();

        searchView.setOnQueryTextListener(this);


        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), updateVacantesActivity.class);
                vacanteSelected = (VacantesResi) parent.getItemAtPosition(position);
                intent.putExtra("titulo", vacanteSelected.getTitulo());
                intent.putExtra("carrera", vacanteSelected.getCarrera());
                intent.putExtra("id", vacanteSelected.getUid());


                startActivity(intent);

            }
        });




    }




    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public void listarDatos(){

        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);


        databaseReference.child(nodoF).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                listVacantes.clear();

                for (DataSnapshot objdataSnapshot : snapshot.getChildren()){
                    VacantesResi vac = objdataSnapshot.getValue(VacantesResi.class);
                    listVacantes.add(vac);

                    listviewvacantesAdapter =  new AdapterPublicaciones(EmpresaPublicacionesActivity.this, listVacantes );
                    listV.setAdapter(listviewvacantesAdapter);
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
          listviewvacantesAdapter.filtrado(newText);
      }catch (Exception a){
          Toast.makeText(this, "No hay elementos que mostrar", Toast.LENGTH_SHORT).show();
      }


        return false;
    }
}