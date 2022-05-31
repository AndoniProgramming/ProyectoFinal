package com.example.inmobiliaria;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<ModeloPropiedad>propiedadList;
    private OnItemClickListener listener;

    public MyAdapter(Context context, List<ModeloPropiedad>propiedadList){
        this.context=context;
        this.propiedadList=propiedadList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModeloPropiedad modelo=propiedadList.get(position);
        holder.txtBaños.setText(String.valueOf(modelo.getBaños()));
        holder.txtCodigo.setText(String.valueOf(modelo.getCodigoPostal()));
        holder.txtHabitaciones.setText(String.valueOf(modelo.getHabitaciones()));
        holder.txtTamaño.setText(String.valueOf(modelo.getTamaño()));
        Picasso.get().load(modelo.getFoto())
                .placeholder(R.mipmap.ic_launcher)
                .fit().
                centerCrop()
                .into(holder.imageView);
        holder.txtCalle.setText(modelo.getCalle());
        holder.txtCiudad.setText(modelo.getCiudad());
        holder.txtTipo.setText(modelo.getTipo());
        holder.txtPrecio.setText(String.valueOf(modelo.getPrecio()));
        holder.txtCategoria.setText(modelo.getCategoria());
        holder.txtDescripcion.setText(modelo.getDescripcion());


    }

    @Override
    public int getItemCount() {
         return propiedadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {
        TextView txtBaños;
        TextView txtCalle;
        TextView txtCategoria;
        TextView txtCiudad;
        TextView txtCodigo;
        TextView txtDescripcion;
        ImageView imageView;
        TextView txtHabitaciones;
        TextView txtTamaño;
        TextView txtTipo;
        TextView txtPrecio;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBaños= itemView.findViewById(R.id.CardBañosViewid);
            txtCalle=itemView.findViewById(R.id.CardCalleViewid);
            txtCategoria=itemView.findViewById(R.id.CardCategoriaViewid);
            txtCiudad=itemView.findViewById(R.id.CardCiudadViewid);
            txtCodigo=itemView.findViewById(R.id.CardCPViewid);
            txtDescripcion=itemView.findViewById(R.id.CardDescripcionViewid);
            imageView=itemView.findViewById(R.id.CardImageViewid);
            txtHabitaciones=itemView.findViewById(R.id.CardHabitacionViewid);
            txtTamaño=itemView.findViewById(R.id.CardTamañoViewid);
            txtTipo=itemView.findViewById(R.id.CardTipoViewid);
            txtPrecio=itemView.findViewById(R.id.CardPrecioViewid);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Escoge Opcion");
            MenuItem borrar=menu.add(Menu.NONE,1,1,"BorrarPropiedad");
            MenuItem editar=menu.add(Menu.NONE,2,2,"EditarPropiedad");

            borrar.setOnMenuItemClickListener(this);
            editar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listener.onBorrar(position);
                            return true;
                        case 2:
                            listener.onEditar(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onBorrar(int position);
        void onEditar(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

}


