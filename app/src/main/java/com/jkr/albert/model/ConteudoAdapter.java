package com.jkr.albert.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;

import java.util.ArrayList;

public class ConteudoAdapter extends RecyclerView.Adapter<ConteudoAdapter.ConteudoViewHolder>{
    private Context mContext;
    private ArrayList<Conteudo> conteudos;
    private String materia;
    private ItemClickListener itemClickListener;

    public ConteudoAdapter(Context context,ArrayList<Conteudo> conteudos,String materia){
        mContext = context;
        this.conteudos = conteudos;
        this.materia = materia;
    }

    @NonNull
    @Override
    public ConteudoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_conteudo,parent,false);
        return new ConteudoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConteudoViewHolder holder, int position) {
        holder.textViewConteudo.setText(conteudos.get(position).getTitulo());
        holder.imageMateria.setImageResource(Metodos.getaIdDrawable(mContext,materia));
    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }

    public class ConteudoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewConteudo;
        public ImageView imageMateria;
        public ConteudoViewHolder(View itemView){
            super(itemView);
            textViewConteudo = itemView.findViewById(R.id.txt_conteudo);
            imageMateria = itemView.findViewById(R.id.icone_materia);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
