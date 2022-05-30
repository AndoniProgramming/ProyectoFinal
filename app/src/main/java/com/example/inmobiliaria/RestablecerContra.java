package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RestablecerContra extends AppCompatActivity {
    private EditText email;
    private Button restablecer;
    private ImageView  inicioSesion;
    private Intent volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contra);
        // Referencia edit text, inmicio sesion y restablecer
        email=findViewById(R.id.edEmailRecuperar);
        restablecer=findViewById(R.id.btnEnviarInstru);
        inicioSesion=findViewById(R.id.imageView12);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = email.getText().toString();

                if(emailAddress.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Introduce  Email ", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Email enviado a: " + emailAddress, Toast.LENGTH_SHORT).show();
                                } else{
                                        Toast.makeText(getApplicationContext(), "Registrate, es gratis! el email no pertenece a ninguna cuenta." + emailAddress, Toast.LENGTH_SHORT).show();
                                }
                            }});
                }
            }
        });

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(volver);
                finish();
            }
        });

    }
}