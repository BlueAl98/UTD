package com.example.utd.residentes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utd.MensajesResidentes;
import com.example.utd.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EnvioDatosResidentesActivity extends AppCompatActivity {

    private Button benviar, benviarVisible;
    private TextView tvnombrecv;
    private EditText txtnombre, txtTitulo;
    private ImageView imagenSubir;
    private Spinner spinner;

    Session session;



    private static final int file = 1;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage storage;



    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_datos_residentes);


        inicializarFirebase();

        benviar = findViewById(R.id.bEenviar);
        tvnombrecv = findViewById(R.id.tvnombrecv);
        txtnombre = findViewById(R.id.txtEnombre);
        spinner = findViewById(R.id.spinnerC);
        txtTitulo =  findViewById(R.id.txtTituloFinal);
        imagenSubir = findViewById(R.id.imagenSubir);
        benviarVisible = findViewById(R.id.bEenviarVISIBLE);

        progressDialog = new ProgressDialog(this);


        //Llenar Spinner

        String [] opciones = {"Mecatrónica", "Energías Renovables", "Desarrollo y Gestión de Software","Logística Internacional",
        "Mantenimiento Industrial","Desarrollo e Innovacion Empresarial","Diseño Digital" };

        ArrayAdapter <String>adapter = new ArrayAdapter<String>(this, R.layout.spinner_blue, opciones);
         spinner.setAdapter(adapter);






        String emailAsociado = getIntent().getStringExtra("email");
        String tituloo = getIntent().getStringExtra("titulo");

        imagenSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileupload();
            }
        });

        txtTitulo.setText(tituloo);






        benviarVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                String nombreF = txtnombre.getText().toString();
               // String carreraF = txtcarrera.getText().toString();
                String ruta = tvnombrecv.getText().toString();


                //verificamos si esta vacia
                if (TextUtils.isEmpty(nombreF)  ||TextUtils.isEmpty(ruta )  ){

                    Toast.makeText(EnvioDatosResidentesActivity.this,"Por favaro ingrese los campos solicitados",Toast.LENGTH_LONG).show();
                }else {



                    ProgressDialog dialog = ProgressDialog.show(EnvioDatosResidentesActivity.this, "", "Detecting...",
                            true);
                    dialog.show();


                    benviar.setVisibility(View.VISIBLE);
                    benviarVisible.setVisibility(View.INVISIBLE);


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 2000); // 3000 milliseconds delay

                    Toast.makeText(EnvioDatosResidentesActivity.this, "Datos correctos presione enviar", Toast.LENGTH_SHORT).show();


                }




            }




        });




    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
    }


    public void fileupload(){


            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent,file);


        }





    public void limpiarcajas(){

        txtnombre.setText("");
        tvnombrecv.setText("");
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == file ) {

            if (resultCode == RESULT_OK) {
                Uri FileUri = data.getData();


               tvnombrecv.setText("CV LISTO");

                 benviar.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {



                         String nombreF = txtnombre.getText().toString();
                       //  String carreraF = txtcarrera.getText().toString();

                         //verificamos si esta vacia
                         if (TextUtils.isEmpty(nombreF) ||TextUtils.isEmpty(FileUri.getLastPathSegment()) ){

                             Toast.makeText(EnvioDatosResidentesActivity.this,"Ingrese su nombre  ",Toast.LENGTH_LONG).show();
                         }else {


                             progressDialog.setMessage("Subiendo....");
                             progressDialog.show();


                             String identificador = UUID.randomUUID().toString();

                             StorageReference Folder = FirebaseStorage.getInstance().getReference().child("curriculo");

                             final StorageReference file_name = Folder.child(identificador);

                             file_name.putFile(FileUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {


                                 String tituloo = getIntent().getStringExtra("titulo");
                                 String idAS = getIntent().getStringExtra("idAS");
                                 String carreaSelect = spinner.getSelectedItem().toString();


                                 MensajesResidentes nuevoInteres = new MensajesResidentes();


                                 nuevoInteres.setId(identificador);
                                 nuevoInteres.setNombre(nombreF);
                                 nuevoInteres.setCarrera(carreaSelect);
                                 nuevoInteres.setRuta("curriculo/" + identificador);
                                 nuevoInteres.setUrl(String.valueOf(uri));
                                 nuevoInteres.setTitulo(tituloo);
                                 nuevoInteres.setIdasociado(idAS);


                                 String emailAsociado = getIntent().getStringExtra("email");

                                 String emailAsociadoF = "Mensajes_" + emailAsociado;


                                 databaseReference.child(emailAsociadoF).child(idAS).child(nuevoInteres.getId()).setValue(nuevoInteres);


                                 //Metodo para enviar correo la empresa

                                 String emailSend = emailAsociado + ".com";

                                 enviarCorreo(emailSend);


                                 Toast.makeText(EnvioDatosResidentesActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();


                                 progressDialog.dismiss();

                                 limpiarcajas();


                             })).addOnFailureListener(new OnFailureListener() {


                                 @Override
                                 public void onFailure(@NonNull @NotNull Exception e) {
                                     Toast.makeText(EnvioDatosResidentesActivity.this, "Tu CV pesa mas de 200kb ", Toast.LENGTH_SHORT).show();

                                     progressDialog.dismiss();
                                 }
                             });


                         }



                     }
                 });




            }

        }



    }

    private void enviarCorreo(String emailSend) {

        String username = "utdappfirebase@gmail.com";
        String pass = "utd123456";

        // String email = txtemail.getText().toString();




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties props = new Properties();

        props.put("mail.smtp.host","smtp.googlemail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");


        try {
            session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, pass);
                }
            });

            if (session!= null){
                Message message =  new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setSubject("Interesado en Realizar Residencia");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailSend));
                message.setText("Tienes un alumno interesado en realizar su residencia en tu empresa." +
                        "Ingresa a la aplicacion de la UTD e inicia sesion para mas informacion");
                Transport.send(message);

            }


        }catch (Exception e){
            e.printStackTrace();
        }



    }


}