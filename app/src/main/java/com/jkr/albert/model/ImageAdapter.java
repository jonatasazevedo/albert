package com.jkr.albert.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkr.albert.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<Projeto>  mProjeto;
    private ItemClickListener itemClickListener;
    private DecimalFormat df;
    public ImageAdapter(Context context,ArrayList<Projeto> proj){
        mContext = context;
        mProjeto = proj;
        df = new DecimalFormat("#0.00");
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_projeto,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Projeto projetoAtual = mProjeto.get(position);
        holder.textViewTitulo.setText(projetoAtual.getNome());
        holder.textViewPreco.setText("Objetivo: R$"+df.format(projetoAtual.getObjetivo()));
        Picasso.get().load(projetoAtual.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mProjeto.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitulo;
        public ImageView imageView;
        public TextView textViewPreco;
        public ImageViewHolder(View itemView){
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textview_upload);
            imageView = itemView.findViewById(R.id.imageview_upload);
            textViewPreco = itemView.findViewById(R.id.textview_objetivo);
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
