package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    //RegistroCliente
    private Button btnRegistro;
    private TextView inicioSesion;
    private Intent movimiento;
    private EditText Nombre;
    private EditText Correo;
    private EditText Telefono;
    private EditText Clave;
    private Intent rescate;
    private TextView tituloUsuario;
    private SwitchCompat switchInmo;
    private EditText direccion;
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    private String name = "";
    private String email = "";
    private String password = "";
    private String auxiliar;
    private String dir;
    private int cellphone = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnRegistro = findViewById(R.id.btnregistrar);
        inicioSesion = findViewById(R.id.textView3);
        Nombre = findViewById(R.id.editTextTextPersonName);
        Clave = findViewById(R.id.editTextTextPassword2);
        Correo = findViewById(R.id.editTextTextEmailAddress2);
        Telefono = findViewById(R.id.editTextPhone);
        tituloUsuario = findViewById(R.id.txtUsuarioTitulo);
        switchInmo = (SwitchCompat) findViewById(R.id.switch1);
        direccion = findViewById(R.id.Direccion1);

        switchInmo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchInmo.setText(R.string.inmo);
                    tituloUsuario.setText(R.string.regcli);
                    direccion.setVisibility(View.INVISIBLE);
                } else {
                    tituloUsuario.setText(R.string.reg);
                    direccion.setVisibility(View.VISIBLE);
                    switchInmo.setText(R.string.cli);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Usuario
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Nombre.getText().toString();
                email = Correo.getText().toString();
                password = Clave.getText().toString();
                auxiliar = (Telefono.getText().toString());
                dir = direccion.getText().toString();

                if (auxiliar.equals("")) {auxiliar = "0"; }

                int precio2 = Integer.parseInt(auxiliar);

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && precio2 != 0) {
                    cellphone = precio2;
                        //Si direccioón es visible, se registra una inmobiliaria!
                        if (direccion.getVisibility()==View.VISIBLE){
                        ModeloInmobiliarias modIn=new ModeloInmobiliarias(name,dir,email,cellphone);
                            RegistroInmobiliaria(modIn,password);
                        }else{
                            ModeloClientes cli = new ModeloClientes(name, email, cellphone);
                            RegistroFirebase(cli,password);
                        }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe Instertar los Datos que Faltan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Nos Lleva al MainActivity a Iniciar Sesioón
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movimiento = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(movimiento);
            }
        });
    }

    private void Cliente(ModeloClientes cliente) {
        String id = myAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios").child(id).setValue(cliente.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    movimiento = new Intent(getApplicationContext(), Opciones.class);
                    startActivity(movimiento);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void RegistroInmobiliaria(ModeloInmobiliarias inmobilia, String clave) {

        myAuth.createUserWithEmailAndPassword(inmobilia.getEmail(), clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = myAuth.getCurrentUser().getUid();
                    mDatabase.child("Inmobiliarias").child(id).setValue(inmobilia.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                movimiento = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);
                                startActivity(movimiento);
                                finish();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Registro Completado con Exito", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void RegistroFirebase(ModeloClientes clix,String clave) {
        myAuth.createUserWithEmailAndPassword(clix.getCorreo(), clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        Cliente(clix);
                } else{
                    Toast.makeText(getApplicationContext(), "Regisro Cliente No Exitoso", Toast.LENGTH_SHORT).show();
                }
                }
            }
        );
    }
}

