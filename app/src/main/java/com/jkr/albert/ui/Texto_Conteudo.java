package com.jkr.albert.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;
import com.jkr.albert.model.Conteudo;

public class Texto_Conteudo extends AppCompatActivity {
    private TextView texto_conteudo,titulo_conteudo;
    private Button botao_sair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto__conteudo);
        texto_conteudo = findViewById(R.id.txt_texto_conteudo);
        botao_sair = findViewById(R.id.activity_conteudo_botao_sair);
        titulo_conteudo = findViewById(R.id.txt_titulo_conteudo);
        Conteudo cont = (Conteudo) getIntent().getSerializableExtra(Constantes.CHAVE_CONTEUDO);
        titulo_conteudo.setText(cont.getTitulo());
        texto_conteudo.setText(cont.getTexto());
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
