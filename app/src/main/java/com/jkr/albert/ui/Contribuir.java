package com.jkr.albert.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jkr.albert.Mecanica.CircleTransform;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.Projeto;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contribuir extends AppCompatActivity {
    private Projeto proj;
    private Button botao_sair,botao_doar;
    private EditText input_valor,input_numagencia;
    private CircleImageView imageView;
    private ProgressBar progressBar;
    private TextView txt_titulo,txt_dono;
    private double valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuir);
        proj = (Projeto) getIntent().getSerializableExtra(Constantes.CHAVE);
        getComponents();
        setComponents();
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        botao_doar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Metodos.verificarEditText2(input_valor,input_numagencia)){
                    getDados();
                    if(valor!=0) criarAlertDialog();
                    else Toast.makeText(Contribuir.this,R.string.zero,Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Contribuir.this,R.string.campos,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDados() {
        valor = Double.parseDouble(input_valor.getText().toString());
    }

    private void getComponents(){
        botao_sair = findViewById(R.id.activity_contribuir_botao_sair);
        botao_doar = findViewById(R.id.botao_doar);
        input_valor = findViewById(R.id.input_valor);
        input_numagencia = findViewById(R.id.input_contribuir_numagencia);
        imageView = findViewById(R.id.activity_contribuir_imagem);
        progressBar = findViewById(R.id.contribuir_progresso_doacao);
        txt_titulo = findViewById(R.id.txt_contribuir_titulo_projeto);
        txt_dono = findViewById(R.id.txt_contribuir_dono_projeto);
    }

    private void setComponents(){
        progressBar.setProgress((int) (100*(proj.getArrecadado()/proj.getObjetivo())));
        txt_titulo.setText(proj.getNome());
        txt_dono.setText("de "+proj.getNomeusuario());
        Picasso.get().load(proj.getUrl()).transform(new CircleTransform()).into(imageView);
    }

    private void criarAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Deseja continuar a transação?");
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference databaseReference;
                proj.setArrecadado(proj.getArrecadado()+valor);
                databaseReference = FirebaseDatabase.getInstance().getReference("projetos");
                databaseReference.child(proj.getId()).setValue(proj);
                Toast.makeText(getApplicationContext(),R.string.doacao,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

}
