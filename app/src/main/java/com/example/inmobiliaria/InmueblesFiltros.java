package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class InmueblesFiltros extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spiHab;
    private Spinner spiPrecios;
    private Spinner spiTipo;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmuebles_filtros);

        //spinner habitaciones
        spiHab=findViewById(R.id.spiHab);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.Habitaciones, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiHab.setAdapter(adapter1);
        spiHab.setOnItemSelectedListener(this);

        //spinner filtro precio
        spiPrecios=findViewById(R.id.spiPrecios);
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.Precios, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiPrecios.setAdapter(adapter2);
        spiPrecios.setOnItemSelectedListener(this);

        //spinner filtro Tipo Propiedad
        spiTipo=findViewById(R.id.spiTipo);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.Tipo, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiTipo.setAdapter(adapter3);
        spiTipo.setOnItemSelectedListener(this);


    }

    //Meterle logica Bases de Datos
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent== spiPrecios){
            if(position==0){
                //Arreglar
                Query myMostViewedPostsQuery = databaseReference.child("posts")
                        .orderByChild("metrics/views");
                myMostViewedPostsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                    // TODO: implement the ChildEventListener methods as documented above
                    // ...
                });
                // BBDD precio 1
            } else if (position==1){

            } else if(position==2){

            } else {

            }

        } else if(parent==spiHab){
            if(position==0){
                // bases de datos 1 Hab
            } else if (position==1){

            } else if(position==2){

            } else {

            }

        } else{
            if(position==0){
                // bla bla tipo propiedad
            } else if (position==1){

            } else {

            }

        }




        String choice= parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), choice, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}