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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactoInmobiliaria extends AppCompatActivity {
    private TextView Direccion;
    private TextView Email;
    private TextView Nombre;
    private TextView Telefono;
    private DatabaseReference databaseReference;
    private ImageView imgmenu;
    private Intent intent;
    private String aix = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_inmobiliaria);
        Direccion = findViewById(R.id.direccionInmo);
        Email = findViewById(R.id.txtEmail111);
        Nombre = findViewById(R.id.txtNombre111);
        Telefono = findViewById(R.id.TelefonoInmo);
        imgmenu = findViewById(R.id.menuUsuCasa);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Opciones.class);
                startActivity(intent);
                finish();
            }
        });

        databaseReference.child("Inmobiliarias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    //Llave de todas las inmobiliarias
                    String inmo = dataSnapshot1.getKey();
                    aix = inmo;
                    //LLave del valor selecionado en el Propiedades Usuario onContacto
                    String valor = getIntent().getExtras().getString("idPropiedad");
                    databaseReference.child("Inmobiliarias").child(inmo).child("Propiedades").child(valor).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshott) {
                            for (DataSnapshot dataSnapshot2 : snapshott.getChildren()) {
                                //Me voy a la inmobiliaria
                                databaseReference.child("Inmobiliarias").child(aix).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Direccion.setText(snapshot.child("Direccion").getValue().toString());
                                        Email.setText(snapshot.child("Email").getValue().toString());
                                        Nombre.setText(snapshot.child("Nombre").getValue().toString());
                                        Telefono.setText(snapshot.child("Telefono").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }}
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                         }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

