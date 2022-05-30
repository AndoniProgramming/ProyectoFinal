package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class PropiedadesUsuarios extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapterUsuarioPropiedades myAdapter;
    private List<ModeloPropiedad> propiedadListaa;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    private Intent contactoInmobiliaria;
    FirebaseAuth myAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_usuarios);
        progressBar = findViewById(R.id.progressBaridnew);
        recyclerView = findViewById(R.id.RecyclerViewid);
        propiedadListaa = new ArrayList<>();
        myAuth = FirebaseAuth.getInstance();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Inmobiliarias");


        String idD = myAuth.getCurrentUser().getUid();

        //arreglar dops referencias llevan a lo mismo la 1
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propiedadListaa.clear();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    String madre = dataSnapshot1.getKey();

                    databaseReference1.child("Inmobiliarias").child(madre).child("Propiedades").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshott) {
                            for (DataSnapshot dataSnapshot2 : snapshott.getChildren()) {
                                ModeloPropiedad modelo = dataSnapshot2.getValue(ModeloPropiedad.class);
                                modelo.setKey(dataSnapshot2.getKey());
                                propiedadListaa.add(modelo);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                myAdapter = new MyAdapterUsuarioPropiedades(getApplicationContext(), propiedadListaa);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myAdapter);

                myAdapter.setOnItemClickListener(new MyAdapterUsuarioPropiedades.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //Aqui puedo meter Nombre propiedad
                        String text = propiedadListaa.get(position).getTipo();
                        Toast.makeText(getApplicationContext(),  propiedadListaa.get(position).getDescripcion()+" " + text , Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onContacto(int position) {
                        ModeloPropiedad seleccionado = propiedadListaa.get(position);
                        String key = seleccionado.getKey();
                        contactoInmobiliaria = new Intent(getApplicationContext(), ContactoInmobiliaria.class);
                        contactoInmobiliaria.putExtra("idPropiedad", key);
                        startActivity(contactoInmobiliaria);
                        //Con le key tengo que obtener los datos de la inmobiliaria
                    }

                    //Funciona
                    @Override
                    public void onFavorito(int position) {
                        ModeloPropiedad seleccionado = propiedadListaa.get(position);
                        String key = seleccionado.getKey();
                        databaseReference1.child("Usuarios").child(idD).child("PropiedadesFavoritas").child(key).setValue(seleccionado);
                        Toast.makeText(getApplicationContext(), "Propiedad Agregada a Favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                Intent inss=new Intent(getApplicationContext(),PerfilUsuario.class);
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
