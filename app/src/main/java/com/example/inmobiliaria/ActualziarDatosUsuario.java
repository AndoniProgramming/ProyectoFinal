package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActualziarDatosUsuario extends AppCompatActivity {
    private ImageView imgTablero;
    private ImageView imgPerfil;
    private Button btnGuardar;
    private EditText edtNombre;
    private EditText edtTelefono;
    DatabaseReference databaseReference;
    private FirebaseAuth myAuth;
    private Intent intent;
    private String name= "";
    private int cellphone=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualziar_datos_usuario);
        imgTablero=findViewById(R.id.menuUsuario);
        imgPerfil=findViewById(R.id.perfilUsuario);
        btnGuardar=findViewById(R.id.btnGuardar1);
        edtNombre=findViewById(R.id.edtNombre1);
        edtTelefono=findViewById(R.id.edtTelefono1);
        myAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String idD = myAuth.getCurrentUser().getUid();
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), PerfilUsuario.class);
                startActivity(intent);
                finish();
            }
        });
        imgTablero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(getApplicationContext(), Opciones.class);
                    startActivity(intent);
                    finish();
                }
            });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator vueltas = ObjectAnimator.ofFloat(v , "rotation", 0f, 360f);
                vueltas.setDuration(500); //
                vueltas.start();
                name= edtNombre.getText().toString();
                if(!name.isEmpty() && !edtTelefono.getText().toString().trim().equals("")){
                    cellphone = Integer.parseInt(edtTelefono.getText().toString().trim());
                    ActualizarNombre(name);
                    ActualizarTelefono(cellphone);
                    Toast.makeText(getApplicationContext(),  "Nombre y Teléfono Actualizados",Toast.LENGTH_SHORT).show();

                }else if(!name.isEmpty()){
                    ActualizarNombre(name);
                    Toast.makeText(getApplicationContext(),  "Nombre Actualizado:" + name,Toast.LENGTH_SHORT).show();

                }else if(!edtTelefono.getText().toString().trim().equals("")){
                    cellphone = Integer.parseInt(edtTelefono.getText().toString());
                    ActualizarTelefono(cellphone);
                    Toast.makeText(getApplicationContext(),  "Telefono Actualizado: "+ cellphone,Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),  "Introduce datos para actualizar",Toast.LENGTH_SHORT).show();
                }
            }

            private void ActualizarNombre(String nombre) {
                databaseReference.child("Usuarios").child(idD).child("Nombre").setValue(nombre);

            }
            private void ActualizarTelefono(int Telefono) {
                databaseReference.child("Usuarios").child(idD).child("Telefono").setValue(Telefono);

            }
        });
    }
}