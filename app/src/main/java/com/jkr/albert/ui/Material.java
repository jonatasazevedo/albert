package com.jkr.albert.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.Conteudo;
import com.jkr.albert.model.Materia;
import com.jkr.albert.model.TopicoAdapter;

import java.util.ArrayList;

public class Material extends AppCompatActivity {
    private ImageView img_materia;
    private RecyclerView recyclerView;
    private TopicoAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<String> topicoArrayList;
    private String materia;
    private Button botao_sair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        getComponentes();
        materia = getIntent().getStringExtra(Constantes.CHAVE_MATERIA);
        setComponentes();
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getComponentes(){
        img_materia = findViewById(R.id.img_materia);
        recyclerView = findViewById(R.id.recyclerTopico);
        botao_sair = findViewById(R.id.activity_material_botao_sair);
    }

    private void setComponentes(){
        img_materia.setImageResource(Metodos.getaIdDrawable(getBaseContext(),materia));
        puxarDadosMateria();
    }

    private void puxarDadosMateria() {
        databaseReference = FirebaseDatabase.getInstance().getReference("materias/"+materia+"/topicos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topicoArrayList = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    String top = postSnap.getValue(String.class);
                    topicoArrayList.add(top);
                }
                adapter = new TopicoAdapter(Material.this,topicoArrayList,materia);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
