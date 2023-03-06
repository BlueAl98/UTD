package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VerificacionSolicitudActivity extends AppCompatActivity {


    TextView tvtitulo , tvnombre, tvcarrera;
    Button bdescarga;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;

    //Obtener el email
    FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_solicitud);


     inicializarFirebase();


        tvtitulo = findViewById(R.id.tvvTituloFinal);
        tvcarrera = findViewById(R.id.tvvEcarrera);
        tvnombre = findViewById(R.id.tvvEnombre);
        bdescarga = findViewById(R.id.imagenDescargar);




        progressDialog = new ProgressDialog(this);


        String id = getIntent().getStringExtra("id");
        String nodo = getIntent().getStringExtra("nodo");
        String idd= getIntent().getStringExtra("uid");


        databaseReference.child(nodo).child(idd).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String titulo = snapshot.child("titulo").getValue().toString();
                String nombre = snapshot.child("nombre").getValue().toString();
                String carrera = snapshot.child("carrera").getValue().toString();

                tvtitulo.setText("Publicacion: "+titulo);
                tvnombre.setText("Nombre: "+nombre);
                tvcarrera.setText("Carrera: "+carrera);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(VerificacionSolicitudActivity.this, "Error verifique red", Toast.LENGTH_SHORT).show();
            }
        });





        bdescarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descarga();

            }
        });





    }




    public void descarga(){

        String id = getIntent().getStringExtra("id");
        String nodo = getIntent().getStringExtra("nodo");
        String idd= getIntent().getStringExtra("uid");

        databaseReference.child(nodo).child(idd).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){


                    String ruta = snapshot.child("ruta").getValue().toString();





                    //Metodo para descargar pdf

                    storageReference = firebaseStorage.getInstance().getReference();


                    ref = storageReference.child(ruta);

                    progressDialog.setMessage("Descargando....");
                    progressDialog.show();

                 ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         String url = uri.toString();




                         dowloadfiles(VerificacionSolicitudActivity.this,"CV","pdf",DIRECTORY_DOWNLOADS,url);

                         progressDialog.dismiss();

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull @NotNull Exception e) {
                         Toast.makeText(VerificacionSolicitudActivity.this, "Error", Toast.LENGTH_SHORT).show();
                         progressDialog.dismiss();
                     }
                 });


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(VerificacionSolicitudActivity.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            }
        });







    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
    }



    public void dowloadfiles(Context context, String filename, String fileextencion, String destinationdir, String url){



        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationdir,filename+fileextencion);

        downloadManager.enqueue(request);


    }


}