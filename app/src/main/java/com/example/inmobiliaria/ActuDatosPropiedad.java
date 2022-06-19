package com.example.inmobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActuDatosPropiedad extends AppCompatActivity  {
    private Button btnActProp;
    private EditText edtPrecio;
    private EditText edtDesc;
    private ListView lvTipoPro;
    private String descripcion="";
    private String cat="";
    private Intent getData;
    private String auxiliar;
    DatabaseReference databaseReference;
    FirebaseAuth myAuth;
    private List<String> userIdLista;
    private ImageView imgPerfil;
    private  ImageView imgMenuProp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actu_datos_propiedad);
        btnActProp=findViewById(R.id.btnActualizarPropiedad);
        edtPrecio=findViewById(R.id.edtPrecio);
        edtDesc=findViewById(R.id.edtDescripcion);
        lvTipoPro=findViewById(R.id.lvTipoProp);
        myAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userIdLista=new ArrayList<>();
        imgPerfil=findViewById(R.id.imgCerrarSesion);
        imgMenuProp=findViewById(R.id.menuPropInmo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Referencia de Usuarios que tienen esta propiedad
        //Tipo

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent inss=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(inss);
            }
        });



        ArrayAdapter<CharSequence> adapterCAT = ArrayAdapter.createFromResource(this,R.array.Categoria, android.R.layout.simple_list_item_1);
        lvTipoPro.setAdapter(adapterCAT);
        lvTipoPro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cat=((TextView)view).getText().toString();
                Toast.makeText(getApplicationContext(),((TextView)view).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        //idDInmobiliaria
        String idD = myAuth.getCurrentUser().getUid();

        //Objeto Actualizar
        getData=getIntent();
        ModeloPropiedad prop=(ModeloPropiedad) getData.getSerializableExtra("PropiedadEditar");
        databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIdLista.clear();

                for(DataSnapshot dataSnapshot1:snapshot.getChildren())
                {
                    //Id de todos los usuarios
                    String userId=dataSnapshot1.getKey();
                    databaseReference.child("Usuarios").child(userId).child("PropiedadesFavoritas").child(prop.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userIdLista.add(userId);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "HabemusFracasoBorrando Usuarios", Toast.LENGTH_SHORT).show();

            }
        });

        imgMenuProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vuelta=new Intent(getApplicationContext(),OpcionesInmobiliaria.class);
                startActivity(vuelta);
            }
        });

        btnActProp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Descripcion
               descripcion=edtDesc.getText().toString();

               //Precio
               auxiliar=(edtPrecio.getText().toString());
               if(auxiliar.equals("")){
                   auxiliar="0";
               }
               int precio2=Integer.parseInt(auxiliar);

             if(!descripcion.isEmpty()){ ActualizarDescipcion(descripcion); }

              if(precio2!= 0) { Actprecio(precio2); }

              if(!cat.isEmpty()){ actCategoria(cat); }

               if(descripcion.isEmpty()&&cat.isEmpty()&&precio2==0){
                   Toast.makeText(getApplicationContext(),  "Inserte Datos a Actualizar",Toast.LENGTH_SHORT).show();
               } else{
                   Toast.makeText(getApplicationContext(), "Se actualizaron los cambios ", Toast.LENGTH_SHORT).show();
               }
           }

           //Funciona
           private void actCategoria(String Categoria) {
               prop.setCategoria(Categoria);
               ActPropiedadesInmoEUsu(prop);
           }
           //Vacio
           private void Actprecio(int precio) {
               prop.setPrecio(precio);
               ActPropiedadesInmoEUsu(prop);
           }
            //Vacio
           private void ActualizarDescipcion(String desc) {
               prop.setDescripcion(desc);
               ActPropiedadesInmoEUsu(prop);
           }

           private void ActPropiedadesInmoEUsu(ModeloPropiedad propiedad){
               //Actualizamos Tipo en Inmobiliaria
               databaseReference.child("Inmobiliarias").child(idD).child("Propiedades").child(propiedad.getKey()).setValue(propiedad).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                   }
               });

               //Actualizamos Tipo en cada usuiario que tenga la propiedad
               for(int i=0;i<userIdLista.size();i++){
                   databaseReference.child("Usuarios").child(userIdLista.get(i)).child("PropiedadesFavoritas").
                           child(propiedad.getKey()).setValue(propiedad).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                       }
                   });
               }
           }
       });
    }
}

