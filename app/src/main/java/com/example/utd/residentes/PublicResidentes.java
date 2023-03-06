package com.example.utd.residentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utd.MainActivity;
import com.example.utd.MultipleChek;
import com.example.utd.PerfilActivity;
import com.example.utd.R;
import com.example.utd.ResidentesActivity;
import com.example.utd.VacantesResi;
import com.example.utd.adaptadores.AdaptadorPublicResidentes;
import com.example.utd.updateVacantesActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PublicResidentes extends AppCompatActivity implements SearchView.OnQueryTextListener {





    private ListView lvlistageneral;
    private SearchView searchView;

    //Lista
    //Para la lista

    private ArrayList<VacantesResi> listVacantes = new ArrayList<VacantesResi>();
    AdaptadorPublicResidentes listviewvacantesAdapter;



    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    VacantesResi vacanteSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_residentes);

        inicializarFirebase();
        lvlistageneral = findViewById(R.id.lvListaGeneral);
        searchView = findViewById(R.id.txbusqueda);




       listarDatos();
        contar();

        searchView.setOnQueryTextListener(this);

        lvlistageneral.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ResidentesActivity.class);
                vacanteSelected= (VacantesResi) parent.getItemAtPosition(position);
                intent.putExtra("titulo", vacanteSelected.getTitulo());
                intent.putExtra("carrera", vacanteSelected.getCarrera());
                intent.putExtra("descripcion", vacanteSelected.getDescripcion());
                intent.putExtra("requisitos", vacanteSelected.getRequisitos());
                intent.putExtra("email", vacanteSelected.getEmailAsociado());
                intent.putExtra("idAS", vacanteSelected.getUid());

                startActivity(intent);
            }
        });







    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }



private void contar() {


    databaseReference.child("GeneralPubli").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            long numHijos = snapshot.getChildrenCount();


           if(numHijos == 0){


             try {
                 mensaje();
             }catch (Exception e){

             }



           }

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    });
}

    public void mensaje(){


        AlertDialog.Builder builder = new AlertDialog.Builder(PublicResidentes.this);
        builder.setTitle("Aviso ");
        builder.setMessage("No hay publicaciones en este momento");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               try {
                   listviewvacantesAdapter.notifyDataSetChanged();
               }catch (Exception e){

               }

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();






    }
    private void listarDatos(){





        databaseReference.child("GeneralPubli").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listVacantes.clear();
                for (DataSnapshot objsnapshot : snapshot.getChildren()){

                    VacantesResi vac = objsnapshot.getValue(VacantesResi.class);
                    listVacantes.add(vac);

                    listviewvacantesAdapter =  new AdaptadorPublicResidentes(PublicResidentes.this, listVacantes );
                    lvlistageneral.setAdapter(listviewvacantesAdapter);

                    int numero = lvlistageneral.getAdapter().getCount();


                    //    Toast.makeText(PublicResidentes.this, numero, Toast.LENGTH_SHORT).show();










                    listviewvacantesAdapter.notifyDataSetChanged();


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
}catch (Exception e){
    Toast.makeText(this, "No hay publicaciones", Toast.LENGTH_SHORT).show();
}



        return false;
    }




}