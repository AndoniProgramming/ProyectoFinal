package com.example.inmobiliaria;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistroPropiedades extends AppCompatActivity {
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    private ImageView mImageView;
    private Button btnRegistroPropiedades;
    private EditText Calle;
    private EditText Ciudad;
    private EditText CodigoPostal;
    private EditText Baños;
    private EditText Habitaciones;
    private EditText Precio;
    private ListView Tipo;
    private ListView Categoria;
    private EditText Descripcion;
    private EditText Tamaño;
    private Intent lego;
    ActivityResultLauncher<String> mgetContent;
    private Button escogeFotos;
    private String calle="";
    private String ciudad="";
    private String descripcion="";
    private int codigopostal=0;
    private int precio=0;
    private int habitaciones=0;
    private int baños=0;
    private int tamaño=0;
    private String tipo="";
    private String categoria="";
    private String auxiliarUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_propiedades);
        Calle=findViewById(R.id.edCalle);
        Ciudad=findViewById(R.id.edCiudad);
        Descripcion=findViewById(R.id.Description);
        CodigoPostal=findViewById(R.id.edCodigo);
        Baños=findViewById(R.id.edBaños);
        Habitaciones=findViewById(R.id.edHabitaciones);
        Precio=findViewById(R.id.edPrecio);
        Tamaño=findViewById(R.id.edTamaño);
        Tipo=findViewById(R.id.lvTipoAlquilerVenta);
        Categoria=findViewById(R.id.lvCategoriaApartCasa);
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnRegistroPropiedades=findViewById(R.id.btnRegistrar);
        escogeFotos=findViewById(R.id.btnCargarFotos);
        mImageView=findViewById(R.id.image_view);
    }

    @Override
    protected void onStart() {
        super.onStart();


        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,R.array.Tipo, android.R.layout.simple_list_item_1);
        Tipo.setAdapter(adapterTipo);
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this,R.array.Categoria, android.R.layout.simple_list_item_1);
        Categoria.setAdapter(adapterCategoria);

        Tipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipo=((TextView)view).getText().toString();
                Toast.makeText(getApplicationContext(),((TextView)view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        Categoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoria=((TextView)view).getText().toString();
                Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();

            }
        });

        escogeFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgetContent.launch("image/*");
            }
        });
        mgetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                mImageView.setImageURI(result);
                Toast.makeText(getApplicationContext(),  "Espere mientras carga",Toast.LENGTH_SHORT).show();
                StorageReference ImagenesCarpeta= FirebaseStorage.getInstance().getReference();
                StorageReference nombreImagen=ImagenesCarpeta.child("Imagen"+ result.getLastPathSegment());
                nombreImagen.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        nombreImagen.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=String.valueOf(uri);
                                auxiliarUrl=url;
                                Toast.makeText(getApplicationContext(),  "Subida Con Exito",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        btnRegistroPropiedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Precio.getText().toString().equals("") && !Tamaño.getText().toString().equals("") && !tipo.isEmpty() && !categoria.isEmpty()&& !Baños.getText().toString().equals("") && !Habitaciones.getText().toString().equals("") && !CodigoPostal.getText().toString().equals("")){
                    codigopostal = Integer.parseInt(CodigoPostal.getText().toString());
                    habitaciones=Integer.parseInt(Habitaciones.getText().toString());
                    baños=Integer.parseInt(Baños.getText().toString());
                    tamaño = Integer.parseInt(Tamaño.getText().toString());
                    precio=Integer.parseInt(Precio.getText().toString());
                    calle=Calle.getText().toString();
                    ciudad=Ciudad.getText().toString();
                    descripcion=Descripcion.getText().toString();
                     ModeloPropiedad modProp= new ModeloPropiedad(calle,ciudad,codigopostal,tipo,auxiliarUrl,baños,habitaciones,tamaño,categoria,descripcion,precio);
                     RegistroPropiedadFirebase(modProp);
                 } else{
                    Toast.makeText(getApplicationContext(),  "Debe Instertar los Datos que Faltan",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RegistroPropiedadFirebase(ModeloPropiedad mp) {
        String id = myAuth.getCurrentUser().getUid();
        String id2=mDatabase.push().getKey();
        //id Inmobiliaria
        mDatabase.child("Inmobiliarias").child(id).child("Propiedades").child(id2).setValue(mp.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    lego = new Intent(getApplicationContext(), OpcionesInmobiliaria.class);
                    startActivity(lego);
                    finish();
                    Toast.makeText(getApplicationContext(), "Registro Completado con Exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
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
