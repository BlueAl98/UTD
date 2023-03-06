package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utd.residentes.EnvioDatosResidentesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class publicacionesActivity extends AppCompatActivity {

    private EditText txttitulo, txtcarrera, txtdescripcion, txtrequisitos;
    private TextView tvuser;
    FirebaseAuth firebaseAuth;
   ProgressDialog progress;
    private Button btnpublica;
    private Spinner spinner;


    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones);

        //inicialize
        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();
        FirebaseUser usuario = firebaseAuth.getCurrentUser();




      txttitulo = findViewById(R.id.txpTitulo);

        txtdescripcion = findViewById(R.id.txpDescripcion);
        txtrequisitos = findViewById(R.id.txpRequisitos);
       spinner = findViewById(R.id.spinnerC2);

        btnpublica = findViewById(R.id.bpublica);


        String [] opciones = {"Mecatrónica", "Energías Renovables", "Desarrollo y Gestión de Software","Logística Internacional",
                "Mantenimiento Industrial","Desarrollo e Innovacion Empresarial", "Diseño Digital"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_blue, opciones);
        spinner.setAdapter(adapter);





        // progressDialog = new ProgressDialog(this);



        btnpublica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPublicacion();
            }
        });



    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public void registrarPublicacion() {

        String titulo = txttitulo.getText().toString().trim();
        String carrera = spinner.getSelectedItem().toString();
        String descrip = txtdescripcion.getText().toString();
        String requi = txtrequisitos.getText().toString();








        //verificamos si esta vacia
        if (TextUtils.isEmpty(titulo)||TextUtils.isEmpty(carrera)||TextUtils.isEmpty(descrip)||TextUtils.isEmpty(requi)){
            Toast.makeText(this,"Ingrese todos los campos",Toast.LENGTH_LONG).show();
            return;
        }



        ProgressDialog dialog = ProgressDialog.show(publicacionesActivity.this, "", "Publicando...",
                true);
        dialog.show();

        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        String nodo = usuario.getEmail();
        int pos = nodo.indexOf(".");
        String nodoF = nodo.substring(0,pos);


        VacantesResi vac = new VacantesResi();





        vac.setUid(UUID.randomUUID().toString());
        vac.setTitulo(titulo);
        vac.setCarrera(carrera);
        vac.setDescripcion(descrip);
        vac.setRequisitos(requi);
        vac.setEmailAsociado(usuario.getEmail());





        databaseReference.child(nodoF).child(vac.getUid()).setValue(vac);
        databaseReference.child("GeneralPubli").child(vac.getUid()).setValue(vac);

        limpiarCajas();

        Toast.makeText(this, "Info agregada", Toast.LENGTH_LONG).show();




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 2000); // 3000 milliseconds delay






    }

    private void limpiarCajas() {

        txttitulo.setText("");

       txtdescripcion.setText("");
       txtrequisitos.setText("");

    }

}