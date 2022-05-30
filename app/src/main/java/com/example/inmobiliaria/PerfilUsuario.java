package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilUsuario extends AppCompatActivity {

    private TextView Email;
    private TextView Nombre;
    private TextView Telefono;
    private TextView PropiedadesGustan;
    private TextView cambiarDatos;
    private Button Menu;
    private DatabaseReference databaseReference;
    private FirebaseAuth myAuth;
    private Intent intent;
    private Button btnBorrar;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Email = findViewById(R.id.txtEmail1);
        Nombre = findViewById(R.id.txtNombre1);
        Telefono = findViewById(R.id.txtTelefono1);
        PropiedadesGustan = findViewById(R.id.txtPropiedadesGustan);
        cambiarDatos = findViewById(R.id.txtActualizarDatos);
        Menu = findViewById(R.id.btnMenu1);
        myAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        btnBorrar=findViewById(R.id.btnBorrar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String idD = myAuth.getCurrentUser().getUid();
        databaseReference.child("Usuarios").child(idD).child("PropiedadesFavoritas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PropiedadesGustan.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.child("Usuarios").child(idD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Email.setText(snapshot.child("Email").getValue().toString());
                    Nombre.setText(snapshot.child("Nombre").getValue().toString());
                    Telefono.setText(snapshot.child("Telefono").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Opciones.class);
                startActivity(intent);
                finish();

            }
        });
        cambiarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),ActualziarDatosUsuario.class);
                startActivity(intent);
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("Usuarios").child(idD).removeValue().addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Cuenta Eliminada", Toast.LENGTH_SHORT).show();
                                            intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    });
                        }
                    }
                });
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
                Intent inss=new Intent(getApplicationContext(),Opciones.class);
                startActivity(inss);
            }
            return true;

            case R.id.perfil:{
                Toast.makeText(getApplicationContext(), "Ya estás en Perfil", Toast.LENGTH_SHORT).show();
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