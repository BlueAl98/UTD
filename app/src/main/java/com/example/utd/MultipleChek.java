package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class MultipleChek extends AppCompatActivity {



    ListView listV;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;

    private ArrayList<MensajesResidentesMultiple> listPerson = new ArrayList<MensajesResidentesMultiple>();
    ArrayAdapter<MensajesResidentesMultiple> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_chek);





        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();

        listV = findViewById(R.id.lv_aceptados);

        listarPersonas();




    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

       // SparseBooleanArray checked = listV.getCheckedItemPositions();



          switch (item.getItemId()){
              case R.id.barEliminar: {


                 if (listV.getCheckedItemCount() == 0){


                     Toast.makeText(this, "Seleccione al menos 1 alumno", Toast.LENGTH_SHORT).show();




                  }else{

                     AlertDialog.Builder builder = new AlertDialog.Builder(MultipleChek.this);
                     builder.setTitle("Concluir publicacion");
                     builder.setMessage("¿Sequro que quieres dar por terminada la publicacion?" +
                             "\n\nUna vez terminada se eliminaran los datos relacionados a ella");

                     builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {


                             registroFinal();

                            eliminarInteresados();


                             startActivity(new Intent(MultipleChek.this,PerfilActivity.class)
                                     .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                             finish();



                             Toast.makeText(MultipleChek.this, "Aspirantes obtenidos", Toast.LENGTH_SHORT).show();




                         }
                     });
                     builder.setNegativeButton("Cancelar", null);
                     AlertDialog dialog = builder.create();
                     dialog.show();








                 }







              }
              break;
          }




        return true;
    }



    public  void listarPersonas(){

        String myid = getIntent().getStringExtra("idAbsoluto");
        String mensajeNodo = getIntent().getStringExtra("nodoMensaje");

        databaseReference.child(mensajeNodo).child(myid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listPerson.clear();

                for(DataSnapshot objsnapshot: snapshot.getChildren()){
                    MensajesResidentesMultiple alumno = objsnapshot.getValue(MensajesResidentesMultiple.class);
                    listPerson.add(alumno);

                    arrayAdapter = new ArrayAdapter<MensajesResidentesMultiple>(
                            MultipleChek.this,android.R.layout.simple_list_item_multiple_choice,
                            listPerson
                    );


                    listV.setAdapter(arrayAdapter);



                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }


    public void registroFinal(){

        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);


        String nodoMensaje =  "Mensajes_"+nodoF;
        String empresa = nodoF+"_key";



        String myid = getIntent().getStringExtra("idAbsoluto");



        databaseReference.child("users").child(empresa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                String empresaName = snapshot.child("nombre").getValue().toString();
               String numInteresados =""+ listV.getCheckedItemCount();




              RegistroFinal reg =  new RegistroFinal();

                reg.setId(UUID.randomUUID().toString());
                reg.setTitulo(empresaName);
                reg.setNumI(numInteresados);

                databaseReference.child("RegistroFinal").child(reg.getId()).setValue(reg);






            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }







    public void eliminarInteresados(){



        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);


        String nodoMensaje =  "Mensajes_"+nodoF;




        String myid = getIntent().getStringExtra("idAbsoluto");







        databaseReference.child(nodoMensaje).child(myid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {




                //Obtenemos las rutas en un snap para eliminar cada child del nodo padre

                for (DataSnapshot snap: snapshot.getChildren()){
                    String ruta = snap.child("ruta").getValue().toString();


                    //Metodo para elminar los archivos de firebase


                    //Hacemos una referencia hacia storage de firebase

                    StorageReference storageRef = storage.getReference();

                    //String nombre de la carpeta

                    String localizacion = ruta;






                    // Create a reference to the file to delete
                    StorageReference desertRef = storageRef.child(localizacion);




                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {



                            databaseReference.child(nodoMensaje).child(myid).removeValue();
                            databaseReference.child("GeneralPubli").child(myid).removeValue();
                            databaseReference.child(nodoF).child(myid).removeValue();



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                        }
                    });




                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(MultipleChek.this, "Algo salio mal", Toast.LENGTH_SHORT).show();

            }




        });





    }





}