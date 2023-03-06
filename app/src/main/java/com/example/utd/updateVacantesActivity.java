package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utd.residentes.EnvioDatosResidentesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;


public class updateVacantesActivity extends AppCompatActivity {

    private TextView txtTitulo, txtcarera, txtdescripcion, txtrequisitos ;
    private Button bconcluir, beliminar;

    FirebaseStorage storage;
    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;



    StorageReference storageReference;
    StorageReference ref;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vacantes);

        firebaseAuth = FirebaseAuth.getInstance();
        inicializarFirebase();




   txtTitulo = findViewById(R.id.txpTituloE);
   txtcarera = findViewById(R.id.txpCarreraE);



   bconcluir =  findViewById(R.id.bcomplete);
 beliminar = findViewById(R.id.beliminarPu);




       String myid = getIntent().getStringExtra("id");
       String mytitulo = getIntent().getStringExtra("titulo");
        String mycarrera = getIntent().getStringExtra("carrera");






       txtTitulo.setText(mytitulo);
       txtcarera.setText(mycarrera);






       txtTitulo.setEnabled(false);
       txtcarera.setEnabled(false);



        progressDialog = new ProgressDialog(this);


       bconcluir.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               FirebaseUser usuario = firebaseAuth.getCurrentUser();
               String nodo = usuario.getEmail();
               int pos = nodo.indexOf(".");
               String nodoF = nodo.substring(0,pos);


               String nodoMensaje =  "Mensajes_"+nodoF;





               Intent intent = new Intent(getApplication(),MultipleChek.class);
               intent.putExtra("nodoMensaje",nodoMensaje);
               intent.putExtra("idAbsoluto", myid);
               startActivity(intent);










           }




       });





       beliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               AlertDialog.Builder builder = new AlertDialog.Builder(updateVacantesActivity.this);
               builder.setTitle("Concluir publicacion");
               builder.setMessage("¿Sequro que quieres dar por terminada la publicacion?" +
                       "\n\nUna vez terminada se eliminaran los datos relacionados a ella");

               builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {




                       eliminar2();


                       startActivity(new Intent(updateVacantesActivity.this,PerfilActivity.class)
                               .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                       finish();



                       Toast.makeText(updateVacantesActivity.this, "Publicacion Finalizada", Toast.LENGTH_SHORT).show();




                   }
               });
               builder.setNegativeButton("Cancelar", null);
               AlertDialog dialog = builder.create();
               dialog.show();


           }
       });

    }





    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();


    }









    public void eliminar2(){

        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);


        String nodoMensaje =  "Mensajes_"+nodoF;

        String mytitulo = getIntent().getStringExtra("titulo");
        String myid = getIntent().getStringExtra("id");


        databaseReference.child(nodoMensaje).child(myid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){


                    for (DataSnapshot snap: snapshot.getChildren()){

                        String ruta = snap.child("ruta").getValue().toString();


                        //Metodo para elminar los archivos de firebase


                        //Hacemos una referencia hacia storage de firebase

                        StorageReference storageRef = storage.getReference();

                        //String nombre de la carpeta

                        String localizacion = ruta;






                        // Create a reference to the file to delete
                        StorageReference desertRef = storageRef.child(localizacion);


                        desertRef.delete().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                             //   databaseReference.child(nodoMensaje).child(myid).removeValue();
                            //    databaseReference.child("GeneralPubli").child(myid).removeValue();
                             //  databaseReference.child(nodoF).child(myid).removeValue();

                             //   Toast.makeText(updateVacantesActivity.this, "entre", Toast.LENGTH_SHORT).show();



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });


                    }

                    databaseReference.child(nodoMensaje).child(myid).removeValue();
                    databaseReference.child("GeneralPubli").child(myid).removeValue();
                    databaseReference.child(nodoF).child(myid).removeValue();


                }else{


                    databaseReference.child("GeneralPubli").child(myid).removeValue();
                    databaseReference.child(nodoF).child(myid).removeValue();
                    databaseReference.child(nodoMensaje).child(myid).removeValue();


                }










            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }






}