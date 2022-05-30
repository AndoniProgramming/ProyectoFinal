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
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

public class PerfilInmobiliaria extends AppCompatActivity {
    private TextView Direccion;
    private TextView Email;
    private TextView Nombre;
    private TextView Telefono;
    private TextView PropiedadesTotal;
    private TextView cambiarDatos;
    private Button Menu;
    private DatabaseReference databaseReference;
    private FirebaseAuth myAuth;
    private Intent intent;
    private Button btneliminar;
    private List<String> listaPropiedadesInmobiliaria;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_inmobiliaria);
        Direccion = findViewById(R.id.txtDireccion);
        Email = findViewById(R.id.txtEmail);
        Nombre = findViewById(R.id.txtNombre);
        Telefono = findViewById(R.id.txtTelefono);
        PropiedadesTotal = findViewById(R.id.txtPropiedadesRegistradas);
        cambiarDatos = findViewById(R.id.txtActualizarDatos);
        Menu = findViewById(R.id.btnMenu);
        myAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        btneliminar = findViewById(R.id.eliminar);
        listaPropiedadesInmobiliaria = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String idD = myAuth.getCurrentUser().getUid();
        ArrayList<String> Lista = new ArrayList<String>();

        //En teoria lista tiene el ID de propiedades a borrar en usuarios

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);
                startActivity(intent);
                finish();

            }
        });
        cambiarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ActuDatosInmobiliarias.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Inmobiliarias").child(idD).child("Propiedades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lista.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    //Id propiedades de la inmobiliaria
                    String auxiliar = dataSnapshot1.getKey();
                    Lista.add(auxiliar);
                    PropiedadesTotal.setText(String.valueOf(snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error en hijos de Propiedades" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        databaseReference.child("Inmobiliarias").child(idD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Direccion.setText(snapshot.child("Direccion").getValue().toString());
                    Email.setText(snapshot.child("Email").getValue().toString());
                    Nombre.setText(snapshot.child("Nombre").getValue().toString());
                    Telefono.setText(snapshot.child("Telefono").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String userId = dataSnapshot1.getKey();
                                        for(int i=0;i<Lista.size();i++) {
                                            databaseReference.child("Usuarios").child(userId).child("PropiedadesFavoritas").child(Lista.get(i)).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Errorr"+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                }
                            );
                            databaseReference.child("Inmobiliarias").child(idD).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess (Void unused){
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Opciones:{
                Intent inss=new Intent(getApplicationContext(),OpcionesInmobiliaria.class);
                startActivity(inss);
            }
            return true;

            case R.id.perfil:{
                Toast.makeText(getApplicationContext(), "Ya estás en el Perfil!", Toast.LENGTH_SHORT).show();

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
