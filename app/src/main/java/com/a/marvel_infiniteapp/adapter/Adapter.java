package com.a.marvel_infiniteapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.marvel_infiniteapp.R;
import com.a.marvel_infiniteapp.model.modelo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;

import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {




   //private List<modelo> listaFilmes;
    private List<modelo> listaFilmes;


    public Adapter(List<modelo> lista) {
        this.listaFilmes = lista;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lista_principal,parent,false);

        return new MyViewHolder(itemLista);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {



          modelo modelo = listaFilmes.get(position);

        holder.titulo_ID.setText(modelo.getTitulo());
        holder.genero_ID.setText(modelo.getGenero());
        holder.ano_ID.setText(modelo.getAno());
        String caminhoImagemPoster = modelo.getPoster();
        holder.nota_filme_ID.setText("Nota : "+modelo.getNota());
        Glide.with(holder.poster_ID).load(caminhoImagemPoster).into(holder.poster_ID);

        int ID = modelo.getID();
     }


    @Override
    public int getItemCount() {


        //return 1;
        return listaFilmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView  titulo_ID;
        TextView  genero_ID;
        TextView  ano_ID;
        TextView  nota_filme_ID;
        ImageView  poster_ID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            titulo_ID       = itemView.findViewById(R.id.titulo_ID);
            genero_ID       = itemView.findViewById(R.id.genero_ID);
            ano_ID          = itemView.findViewById(R.id.ano_ID);
            nota_filme_ID   = itemView.findViewById(R.id.nota_filme_ID);
            poster_ID       = itemView.findViewById(R.id.poster_ID);

        }
    }


}//fim do adaptador
