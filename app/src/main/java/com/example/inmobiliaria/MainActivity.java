package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Inicio Sesion
    private Button btnInicio;
    private TextView registro;
    private EditText nombreUsuario;
    private EditText password;
    private Intent inta;
    private Intent help;
    private Intent rescate;

    private String usuario;
    private String scontraseña;
    private TextView restablecerContra;

    private FirebaseAuth myAuth;
    private String userID;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInicio = findViewById(R.id.Log_In);
        registro = findViewById(R.id.Registro);
        nombreUsuario = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        myAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        restablecerContra=findViewById(R.id.olvidarContra);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Registro
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inta = new Intent(getApplicationContext(), Registro.class);
                startActivity(inta);
                finish();
            }
        });

        // Log In
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = nombreUsuario.getText().toString();
                scontraseña = password.getText().toString();
                if (!usuario.isEmpty() && !scontraseña.isEmpty()) {
                    inicioSesion();
                } else {
                    Toast.makeText(getApplicationContext(), "Rellena los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        restablecerContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inta = new Intent(getApplicationContext(), RestablecerContra.class);
                startActivity(inta);
                finish();
            }
        });
    }

    private void inicioSesion() {

        myAuth.signInWithEmailAndPassword(usuario, scontraseña).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Usuarios
                inta = new Intent(getApplicationContext(), Opciones.class);
                //inmobiliarias
                help = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);

                userID  = FirebaseAuth.getInstance().getCurrentUser().getUid();

                database.child("Usuarios")
                        .child(userID) // Create a reference to the child node directly
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //Es Cliente
                                    startActivity(inta);
                                    finish();
                                } else {
                                    //Es Inmobiliaria
                                    startActivity(help);
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

            } else {
                Toast.makeText(getApplicationContext(), "Comprueba los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

