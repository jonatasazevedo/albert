package com.jkr.albert.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.R;

public class Login extends AppCompatActivity {
    private Authentication authentication;
    private EditText txt_email,txt_senha;
    private String email,senha;
    private Button botao_cadastrar,botao_entrar,botao_esqueceusenha;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getComponentes();
        desapareceProgressBar();
        authentication = new Authentication(Login.this);
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getConteudo();
                authentication.logar(email,senha);
            }
        });

        botao_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Cadastro.class));
            }
        });

        botao_esqueceusenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), EsqueceuSenha.class));
            }
        });
    }

    private void getConteudo(){
        email = txt_email.getText().toString();
        senha = txt_senha.getText().toString();
    }

    private void getComponentes(){
        botao_esqueceusenha = findViewById(R.id.activity_login_botao_esqueci_senha);
        botao_entrar = findViewById(R.id.activity_login_botao_entrar);
        botao_cadastrar = findViewById(R.id.activity_login_botao_cadastro);
        txt_email = findViewById(R.id.activity_login_text_email);
        txt_senha = findViewById(R.id.activity_login_txt_senha);
        progressBar = findViewById(R.id.progressBar2);
    }

    public void apareceProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void desapareceProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

}
