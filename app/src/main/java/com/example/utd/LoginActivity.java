package com.example.utd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private EditText  Email, pass;
    private Button bregistrar, bingresa;
    private ProgressDialog progressDialog;

    //Objeto de firebase
     FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                /* si esta activo el modo oscuro lo desactiva */
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

        //inicialize
        firebaseAuth = FirebaseAuth.getInstance();


        Email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPass);
        bregistrar = findViewById(R.id.btRegistrar);
        bingresa = findViewById(R.id.btnIngresar);


        progressDialog = new ProgressDialog(this);

        //Evento de registrar
        bregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(),RegistroActivity.class);
                startActivity(intent);
            }
        });

        //Evento de ingresa
        bingresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loguearUsuario();


            }
        });




    }



    private void loguearUsuario() {

        String emailL = Email.getText().toString().trim();
        String passwordL = pass.getText().toString().trim();

        //verificamos si esta vacia
        if (TextUtils.isEmpty(emailL)){
            Toast.makeText(this,"ingrese el email",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(passwordL)){
            Toast.makeText(this,"ingrese el password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Ingresando....");
        progressDialog.show();

        //Loeguear
        firebaseAuth.signInWithEmailAndPassword(emailL,passwordL)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {

                        //verificar
                        if(task.isSuccessful()){



                            Intent intent = new Intent(getApplication(),PerfilActivity.class);

                            intent.putExtra(PerfilActivity.user,emailL);
                            intent.putExtra("pass",passwordL);
                            startActivity(intent);


                            Email.setText("");
                            pass.setText("");

                            Toast.makeText(LoginActivity.this,"Bienvenido:"+emailL,Toast.LENGTH_LONG).show();





                        }else{

                                Toast.makeText(LoginActivity.this, "Error usuario o password", Toast.LENGTH_LONG).show();

                        }

                        progressDialog.dismiss();

                    }
                });

    }

    @Override
    protected void onStart() {
   FirebaseUser user = firebaseAuth.getCurrentUser();

   if (user!=null){
       Intent intent = new Intent(getApplication(),PerfilActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

       startActivity(intent);


   }

   super.onStart();


    }

}