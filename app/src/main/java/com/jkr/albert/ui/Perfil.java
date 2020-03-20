package com.jkr.albert.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.ImageAdapter;
import com.jkr.albert.model.Projeto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends Fragment {

    private ImageAdapter adapter;
    private Authentication authentication;
    private TextView txtnomecomp;
    private Button botao_novo_projeto,botao_sair;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<Projeto> projetos;
    private CircleImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil, container, false);
        txtnomecomp = view.findViewById(R.id.fragment_perfil_nome_usuario);
        botao_novo_projeto = view.findViewById(R.id.fragment_perfil_botao_novo_projeto);
        botao_sair = view.findViewById(R.id.fragment_perfil_botao_deslogar);
        imageView = view.findViewById(R.id.fragment_perfil_img_usuario);
        recyclerView = view.findViewById(R.id.recyclerperfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        authentication = new Authentication(getContext());
        setDadosUsuario();
        puxarDadosProjeto();
        botao_novo_projeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Novo_Projeto.class));
            }
        });
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAlertDialogDeslogar();
            }
        });
        return view;
    }
    public void setDadosUsuario() {
        txtnomecomp.setText(authentication.getCurrentUser().getDisplayName());
        Picasso.get().load(authentication.getCurrentUser().getPhotoUrl()).transform(new CircleTransform()).into(imageView);
    }


    private void puxarDadosProjeto(){
        databaseReference = FirebaseDatabase.getInstance().getReference("projetos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projetos = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Projeto proj = postSnap.getValue(Projeto.class);
                    if(proj.getUsuario().equals(authentication.getUID())) projetos.add(proj);
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

    private void criarAlertDialogDeslogar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Atenção");
        builder.setMessage("Deseja sair da conta?");
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Metodos.setSelect(R.id.navigation_feed);
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                authentication.desconectar();
                Toast.makeText(getContext(),R.string.usuariodesconectado,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
