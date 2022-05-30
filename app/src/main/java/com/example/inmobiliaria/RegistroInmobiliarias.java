package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroInmobiliarias extends AppCompatActivity {
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;

    private Button btnRegistro;
    private TextView inicioSesion;
    private Intent movimiento;

    private String name= "";
    private String email="";
    private String password = "";
    private String direccion="";
    private int cellphone=0;

    private EditText Nombre;
    private EditText Correo;
    private EditText Telefono;
    private EditText Clave;
    private EditText Direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_inmobiliarias);
        Nombre=findViewById(R.id.Nombre);
        Correo=findViewById(R.id.EmailAddress);
        Telefono= findViewById(R.id.Telefono);
        Direccion=findViewById(R.id.Direccion);
        Clave=findViewById(R.id.Clave);
        btnRegistro=findViewById(R.id.btnRegistro);
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    protected void onStart() {
        super.onStart();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                direccion=Direccion.getText().toString();
                name= Nombre.getText().toString();
                email=Correo.getText().toString();
                password= Clave.getText().toString();

                if(!direccion.isEmpty() && !name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !Telefono.getText().toString().equals("")){
                    cellphone = Integer.parseInt(Telefono.getText().toString());
                   // Hasta Aquií LLEGO
                    RegistroFirebase();

                } else{
                    Toast.makeText(getApplicationContext(),  "Debe Instertar los Datos que Faltan",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RegistroFirebase() {
        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ModeloInmobiliarias inmo= new ModeloInmobiliarias(name,direccion,email,cellphone);
                    String id= myAuth.getCurrentUser().getUid();
                    mDatabase.child("Inmobiliarias").child(id).setValue(inmo.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                movimiento = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);
                                startActivity(movimiento);
                                finish();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Registro Completado con Exito", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

