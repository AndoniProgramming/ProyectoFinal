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

public class Opciones extends AppCompatActivity {

    private CardView btnComprar;
    private CardView btnPropFavoritas;
    private Intent intent;
    private CardView btnPerfil;
    private CardView cerrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba);
        btnComprar= findViewById(R.id.cardVer);
        btnPropFavoritas= findViewById(R.id.cardPropFavor);
        btnPerfil=findViewById(R.id.cardPerfillll);
        cerrar=findViewById(R.id.cardFuera);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),PropiedadesUsuarios.class);
                startActivity(intent);
                finish();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent vamos1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(vamos1);
                finish();
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),PerfilUsuario.class);
                startActivity(intent);
                finish();

            }
        });
        btnPropFavoritas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),PropiedadesFavoritasUsuarios.class);
                startActivity(intent);
                finish();
            }
        });

    }

}