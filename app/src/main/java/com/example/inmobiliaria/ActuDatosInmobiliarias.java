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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActuDatosInmobiliarias extends AppCompatActivity {

    private Button btnOpciones;
    private Button btnGuardar;
    private EditText edtNombre;
    private EditText edtTelefono;
    private  EditText edtDireccion;

    DatabaseReference databaseReference;
    private FirebaseAuth myAuth;
    private Intent intent;
    private String name= "";
    private String aux="";
    private String direccion="";
    private int cellphone=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_datos_inmobiliarias);
        btnOpciones=findViewById(R.id.btnOpciones);
        btnGuardar=findViewById(R.id.btnGuardar);
        edtNombre=findViewById(R.id.edtNombre);
        edtTelefono=findViewById(R.id.edtTelefono);
        edtDireccion=findViewById(R.id.edtDireccion);
        myAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


        @Override
        protected void onStart() {
            super.onStart();
            String idD = myAuth.getCurrentUser().getUid();
            btnOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);
                    startActivity(intent);
                    finish();
                }
            });
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name= edtNombre.getText().toString();
                    direccion=edtDireccion.getText().toString();

                    if(!edtTelefono.getText().toString().trim().equals("")){
                        aux=edtTelefono.getText().toString().trim();
                        cellphone = Integer.parseInt(aux);
                        databaseReference.child("Inmobiliarias").child(idD).child("Telefono").setValue(cellphone);
                        Toast.makeText(getApplicationContext(),  "Teléfono Actualizados",Toast.LENGTH_SHORT).show();
                    }

                    if(!name.isEmpty()){
                        databaseReference.child("Inmobiliarias").child(idD).child("Nombre").setValue(name);
                        Toast.makeText(getApplicationContext(),  "Nombre Actualizado:" + name,Toast.LENGTH_SHORT).show();
                    }

                    if(!direccion.isEmpty()){
                        databaseReference.child("Inmobiliarias").child(idD).child("Direccion").setValue(direccion);
                        Toast.makeText(getApplicationContext(),  "Dirección Actualizado:" + direccion,Toast.LENGTH_SHORT).show();
                    }

                    if(direccion.isEmpty()&&name.isEmpty()&&edtTelefono.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),  "Inserte Datos a Actualizar",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


}