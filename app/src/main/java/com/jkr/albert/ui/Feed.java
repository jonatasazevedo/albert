package com.jkr.albert.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.Mecanica.CircleTransform;
import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.ImageAdapter;
import com.jkr.albert.model.Projeto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;


public class Feed extends Fragment{
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private TextView txtNome;
    private Authentication authentication;
    private DatabaseReference databaseReference;
    private ArrayList<Projeto> projetos;
    private CircleImageView circleimageView;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        authentication = new Authentication(getContext());
        recyclerView = v.findViewById(R.id.recyclerView);
        txtNome = v.findViewById(R.id.txt_bemvindo);
        circleimageView = v.findViewById(R.id.circleImageView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        puxarDadosProjeto();
        setDadosUsuario();
        return v;
    }

    public void setDadosUsuario() {
        txtNome.setText("Bem vindo(a) "+authentication.getCurrentUser().getDisplayName()+"!");
        Picasso.get().load(authentication.getCurrentUser().getPhotoUrl()).transform(new CircleTransform()).into(circleimageView);
    }

    private void puxarDadosProjeto(){
        databaseReference = FirebaseDatabase.getInstance().getReference("projetos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projetos = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Projeto proj = postSnap.getValue(Projeto.class);
                    projetos.add(proj);
                }
                Collections.reverse(projetos);
                adapter = new ImageAdapter(getContext(),projetos);
                adapter.setOnItemClickListener(new ImageAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getContext(),Projetos.class);
                        intent.putExtra(Constantes.CHAVE,projetos.get(position));
                        startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
