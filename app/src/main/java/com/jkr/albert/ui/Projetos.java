package com.jkr.albert.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.Mecanica.CircleTransform;
import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.Projeto;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class Projetos extends AppCompatActivity {
    private CircleImageView img;
    private TextView txtobj,txttitulo,txtarrecadado,txtdonoprojeto;
    private Button botao_contribuir,botao_sair,botao_excluir;
    private Projeto proj;
    private ProgressBar progress;
    private Authentication authentication;
    private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);
        getComponentes();
        authentication = new Authentication(Projetos.this);
        df = new DecimalFormat("#0.00");
        proj = (Projeto) getIntent().getSerializableExtra(Constantes.CHAVE);
        if(proj.getUsuario().equals(authentication.getUID())) botao_contribuir.setVisibility(View.INVISIBLE);
        else botao_excluir.setVisibility(View.INVISIBLE);
        setComponents();
        botao_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAlertDialogExcluir();
            }
        });
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        botao_contribuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Projetos.this,Contribuir.class);
                intent.putExtra(Constantes.CHAVE,proj);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getComponentes() {
        img = findViewById(R.id.activity_projetos_imagem);
        botao_excluir = findViewById(R.id.botao_excluir);
        botao_contribuir = findViewById(R.id.botao_contribuir);
        txttitulo = findViewById(R.id.txt_projetos_titulo_projeto);
        txtdonoprojeto = findViewById(R.id.txt_projetos_dono_projeto);
        txtobj = findViewById(R.id.txt_projeto_objetivo);
        txtarrecadado = findViewById(R.id.txt_projeto_arrecadado);
        botao_sair = findViewById(R.id.activity_projetos_botao_sair);
        progress = findViewById(R.id.projetos_progresso_doacao);
        botao_contribuir = findViewById(R.id.botao_contribuir);
    }

    private void setComponents(){
        txttitulo.setText(proj.getNome());
        txtdonoprojeto.setText("de "+proj.getNomeusuario());
        txtobj.setText("Objetivo\nR$ "+df.format(proj.getObjetivo()));
        txtarrecadado.setText("Arrecadado\nR$ "+df.format(proj.getArrecadado()));
        progress.setProgress((int) (100*(proj.getArrecadado()/proj.getObjetivo())));
        Picasso.get().load(proj.getUrl()).transform(new CircleTransform()).into(img);
    }

    private void criarAlertDialogExcluir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Deseja excluir o projeto?");
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("projetos");
                databaseReference.child(proj.getId()).removeValue();
                Toast.makeText(getApplicationContext(),R.string.projetoexcluido,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
