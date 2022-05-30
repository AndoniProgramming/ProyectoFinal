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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class OpcionesInmobiliaria extends AppCompatActivity {

    private Button btnRegistrarPropiedades;
    private Intent vamos;
    private Button btnVerProp;
    private Button btnVerInmobiliaria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_inmobiliaria);
        btnRegistrarPropiedades=findViewById(R.id.btnRegistroPropiedades);
        btnVerProp=findViewById(R.id.btnVerPropiedades);
        btnVerInmobiliaria=findViewById(R.id.btnPerfilInmobiliaria);
    }

        @Override
        protected void onStart() {
            super.onStart();
            btnVerProp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vamos=new Intent(getApplicationContext(),propiedadesInmobiliaria.class);
                    startActivity(vamos);
                    finish();

                }
            });

            btnVerInmobiliaria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vamos=new Intent(getApplicationContext(),PerfilInmobiliaria.class);
                    startActivity(vamos);
                    finish();
                }
            });


            btnRegistrarPropiedades.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vamos=new Intent(getApplicationContext(),RegistroPropiedades.class);
                    startActivity(vamos);
                    finish();

                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Opciones:{
                Toast.makeText(getApplicationContext(), "Ya estás en Opciones", Toast.LENGTH_SHORT).show();
            }
            return true;

            case R.id.perfil:{
                Intent inss=new Intent(getApplicationContext(),PerfilInmobiliaria.class);
                startActivity(inss);
            }
            return true;

            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                Intent inss=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(inss);
            }
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

}