package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class OpcionesInmobiliaria extends AppCompatActivity {

    private CardView cardRegistrar;
    private CardView cardVerPropiedades;
    private CardView cardPeril;
    private CardView cerrarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_inmobiliaria);
        cerrarSesion = findViewById(R.id.cardCerrarSesion);
        cardPeril = findViewById(R.id.cardPerfil);
        cardVerPropiedades = findViewById(R.id.cardMisPropiedades);
        cardRegistrar = findViewById(R.id.cardRegistrarPropiedades);
        cardVerPropiedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vamos=new Intent(getApplicationContext(),propiedadesInmobiliaria.class);
                startActivity(vamos);
            }
        });

        cardPeril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vamos1=new Intent(getApplicationContext(),PerfilInmobiliaria.class);
                startActivity(vamos1);
                finish();
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent inss=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(inss);
            }
        });


        cardRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vamo2=new Intent(getApplicationContext(),RegistroPropiedades.class);
                startActivity(vamo2);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}