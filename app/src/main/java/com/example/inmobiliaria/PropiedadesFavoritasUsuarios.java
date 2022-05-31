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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropiedadesFavoritasUsuarios extends AppCompatActivity{
    private RecyclerView recyclerView;
    private MyAdapterFavoritasUsuario myAdapter;
    private List<ModeloPropiedad> propiedadList;
    private DatabaseReference databaseReference;
    private FirebaseAuth myAuth;
    private Intent movimiento;
    private TextView txtSinFavoritos;
    private ImageView imgPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_favoritas_usuarios);
        recyclerView=(RecyclerView) findViewById(R.id.RecyclerViewidid);
        propiedadList=new ArrayList<>();
        myAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        txtSinFavoritos=findViewById(R.id.txtSinProp);
        txtSinFavoritos.setVisibility(View.INVISIBLE);
        imgPerfil=findViewById(R.id.imgPerfillll);

    }

        @Override
        protected void onStart() {
            super.onStart();
            //Id del usuario/Cliente
            imgPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inss=new Intent(getApplicationContext(),PerfilUsuario.class);
                    startActivity(inss);
                }
            });


            String idD = myAuth.getCurrentUser().getUid();
            databaseReference.child("Usuarios").child(idD).child("PropiedadesFavoritas").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshott) {
                                propiedadList.clear();
                                if(snapshott.hasChildren()){
                                for(DataSnapshot dataSnapshot1:snapshott.getChildren()) {
                                    ModeloPropiedad modelo=dataSnapshot1.getValue(ModeloPropiedad.class);
                                    modelo.setKey(dataSnapshot1.getKey());
                                    propiedadList.add(modelo);
                                }} else{
                                    txtSinFavoritos.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "No tienes propiedades registradas!", Toast.LENGTH_SHORT).show();
                                }


                                myAdapter=new MyAdapterFavoritasUsuario(getApplicationContext(),propiedadList);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(myAdapter);
                                myAdapter.setOnItemClickListener(new MyAdapterFavoritasUsuario.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        String text=propiedadList.get(position).getTipo();
                                        Toast.makeText(getApplicationContext(), propiedadList.get(position).getDescripcion()+ " "+text , Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerInmobiliaria(int position) {
                                        ModeloPropiedad seleccionado=propiedadList.get(position);
                                        String key=seleccionado.getKey();
                                        movimiento=new Intent(getApplicationContext(),ContactoInmobiliaria.class);
                                        movimiento.putExtra("idPropiedad",key);
                                        startActivity(movimiento);
                                        //Con le key tengo que obtener los datos de la inmobiliaria

                                    }

                                    @Override
                                    public void onBorrar(int position) {
                                        ModeloPropiedad selec=propiedadList.get(position);
                                        //Key es nulo
                                        String key=selec.getKey();
                                        databaseReference.child("Usuarios").child(idD).child("PropiedadesFavoritas").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), selec.getTipo()+" Borrado", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
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