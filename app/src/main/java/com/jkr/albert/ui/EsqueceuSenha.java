package com.jkr.albert.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.R;

public class EsqueceuSenha extends AppCompatActivity {
    private EditText txtEmail;
    private Button bt_reset,bt_sair;
    private String email;
    Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);
        getComponents();
        authentication = new Authentication(EsqueceuSenha.this);
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConteudo();
                authentication.resetSenha(email);
            }
        });
        bt_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getComponents(){
        txtEmail = findViewById(R.id.email_reset);
        bt_reset = findViewById(R.id.botao_reset);
        bt_sair = findViewById(R.id.activity_esq_botao_sair);
    }

    private void getConteudo(){
        email = txtEmail.getText().toString().trim();
    }
}
