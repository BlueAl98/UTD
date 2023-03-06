package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistroActivity extends AppCompatActivity {


    private EditText email, pass, nombre, telefono, domicilio;
    private Button brRegistrar;
    private ProgressDialog  progressDialog;


    //Objeto de firebase
    private FirebaseAuth firebaseAuth;

    //objetos firebase realtimedatabase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //inicialize
        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();


        email = findViewById(R.id.txrEmail);
        pass = findViewById(R.id.txrPass);
        nombre = findViewById(R.id.txrNombre);
        telefono = findViewById(R.id.txrTel);
        domicilio = findViewById(R.id.txrDireccion);

        brRegistrar = findViewById(R.id.bpublica);

        progressDialog = new ProgressDialog(this);

        //Evento de registrar
        brRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            registrarUsuarios();





            }
        });

    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void limpiarCajas() {

        email.setText("");
        pass.setText("");
        nombre.setText("");
        telefono.setText("");
        domicilio.setText("");
    }



    private void registrarUsuarios() {

        String emailR = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String name = nombre.getText().toString();
        String tel = telefono.getText().toString();
        String dom = domicilio.getText().toString();



        if (TextUtils.isEmpty(emailR)||TextUtils.isEmpty(password)||TextUtils.isEmpty(name)||TextUtils.isEmpty(tel)||TextUtils.isEmpty(dom)){
            Toast.makeText(this,"Ingrese todos los campos",Toast.LENGTH_LONG).show();
            return;
        }
        else {


            progressDialog.setMessage("Realizado registro en linea....");
            progressDialog.show();

            //Creando usuario firebase
            firebaseAuth.createUserWithEmailAndPassword(emailR, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {

                            //verificar
                            if (task.isSuccessful()) {

                                //Crear un objeto de la calse EmpresaC

                                EmpresaC em = new EmpresaC();


                                int pos = emailR.indexOf(".");
                                String nodoF = emailR.substring(0, pos);


                                em.setUid(nodoF + "_key");
                                em.setEmail(emailR);
                                em.setPass(password);
                                em.setNombre(name);
                                em.setTelefono(tel);
                                em.setDomicilio(dom);


                                databaseReference.child("users").child(em.getUid()).setValue(em);


                                limpiarCajas();
                                Toast.makeText(RegistroActivity.this, "Se ha registrado el email:" + emailR, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));

                            } else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(RegistroActivity.this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegistroActivity.this, "Error de registro verifique campos", Toast.LENGTH_LONG).show();
                                }
                            }

                            progressDialog.dismiss();

                        }
                    });

        }

    }
}