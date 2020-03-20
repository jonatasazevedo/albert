package com.jkr.albert.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkr.albert.R;
import com.jkr.albert.ui.Projetos;
import com.jkr.albert.ui.Texto_Conteudo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TopicoAdapter extends RecyclerView.Adapter<TopicoAdapter.TopicoViewHolder> {
    private Context mContext;
    private ArrayList<String> topicos;
    private String materia;

    public TopicoAdapter(Context context,ArrayList<String> topicos,String materia){
        mContext = context;
        this.topicos = topicos;
        this.materia = materia;
    }

    @NonNull
    @Override
    public TopicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_topico,parent,false);
        return new TopicoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicoViewHolder holder, int position) {
        holder.textViewTopico.setText(topicos.get(position));
        puxarDadosConteudo(topicos.get(position),holder.recycler_conteudos,materia);
    }

    @Override
    public int getItemCount() {
        return topicos.size();
    }

    private void puxarDadosConteudo(String titulo, final RecyclerView recyclerView,final String materia) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("topicos/"+titulo);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Conteudo> conteudoArrayList = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Conteudo conteudo = postSnap.getValue(Conteudo.class);
                    conteudoArrayList.add(conteudo);
                }
                ConteudoAdapter adapter = new ConteudoAdapter(mContext,conteudoArrayList,materia);
                adapter.setOnItemClickListener(new ConteudoAdapter.ItemClickListener(){
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(mContext,Texto_Conteudo.class);
                        intent.putExtra(Constantes.CHAVE_CONTEUDO,conteudoArrayList.get(position));
                        mContext.startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class TopicoViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTopico;
        public RecyclerView recycler_conteudos;
        public TopicoViewHolder(View itemView){
            super(itemView);
            textViewTopico = itemView.findViewById(R.id.txt_topico);
            recycler_conteudos = itemView.findViewById(R.id.recycler_conteudos);
        }
    }
}
