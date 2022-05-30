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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class propiedadesInmobiliaria extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<ModeloPropiedad> propiedadList;
    DatabaseReference databaseReference;
    FirebaseAuth myAuth;
    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private Intent editIntent;
    private TextView txtSinFavoritos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_inmobiliaria);
        progressBar=findViewById(R.id.progressBaridnew);
        recyclerView=(RecyclerView) findViewById(R.id.RecyclerViewid);
        propiedadList=new ArrayList<>();
        myAuth = FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        txtSinFavoritos=findViewById(R.id.txtSinProppp);
        txtSinFavoritos.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String idD = myAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child("Inmobiliarias").child(idD).child("Propiedades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propiedadList.clear();
                if(snapshot.hasChildren()){

                    for(DataSnapshot dataSnapshot1:snapshot.getChildren()) {
                        ModeloPropiedad modelo = dataSnapshot1.getValue(ModeloPropiedad.class);
                        modelo.setKey(dataSnapshot1.getKey());

                        propiedadList.add(modelo);
                    }
                } else{
                        txtSinFavoritos.setVisibility(View.VISIBLE);

                    }
                myAdapter=new MyAdapter(propiedadesInmobiliaria.this,propiedadList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(myAdapter);
                progressBar.setVisibility(View.INVISIBLE);

                //Aqui implementariamos el onEdit tambien asi como el onBorrar
                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //Aqui puedo meter Nombre propiedad
                        String text=propiedadList.get(position).getCategoria();
                        String desc=propiedadList.get(position).getTipo();
                        Toast.makeText(getApplicationContext(), desc+" en "+text , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBorrar(int position) {
                    ModeloPropiedad seleccionado=propiedadList.get(position);
                    String key=seleccionado.getKey();
                        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(seleccionado.getFoto());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Borrar Inmobiliaria
                                databaseReference.child("Inmobiliarias").child(idD).child("Propiedades").child(key).removeValue();
                                //Borrar en Usuarios que la guardaron como favorito... si no funciona, cambiar a add value event listener
                                databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot dataSnapshot1:snapshot.getChildren())
                                        {
                                            String userId=dataSnapshot1.getKey();
                                            databaseReference.child("Usuarios").child(userId).child("PropiedadesFavoritas").child(key).removeValue();
                                        }}

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), "HabemusFracasoBorrando Usuarios", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onEditar(int position) {
                        ModeloPropiedad seleccion=propiedadList.get(position);
                        String key=seleccion.getKey();
                        editIntent=new Intent(getApplicationContext(),ActuDatosPropiedad.class);
                        editIntent.putExtra("PropiedadEditar",seleccion);
                        editIntent.putExtra("PropKey", key);
                        startActivity(editIntent);
                        finish();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error: "+ error.getMessage(),Toast.LENGTH_LONG).show();
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
                Intent inss=new Intent(getApplicationContext(),OpcionesInmobiliaria.class);
                startActivity(inss);
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