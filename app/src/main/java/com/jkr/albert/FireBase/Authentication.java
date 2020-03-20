package com.jkr.albert.FireBase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.jkr.albert.ui.EsqueceuSenha;
import com.jkr.albert.ui.Login;
import com.jkr.albert.ui.Tela_Principal_botton;

public class Authentication {
    private FirebaseAuth firebaseAuth;
    private Context contexto;
    private Login login;
    private EsqueceuSenha esqueceuSenha;

    public Authentication(Context ctx){
        contexto = ctx;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void desconectar(){
        FirebaseAuth.getInstance().signOut();
    }

    public boolean check(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser!=null;
    }
    public FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser;
    }

    public String getUID(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser.getUid();
    }

    public void logar(String email, String senha){
        if (!Metodos.isOnline(contexto)) return;
        if(!Metodos.verificar(email,senha,contexto)) return;
        login = (Login) contexto;
        login.apareceProgressBar();
        firebaseAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    login.desapareceProgressBar();
                    if(task.isSuccessful()){
                        Intent intent = new Intent(contexto,Tela_Principal_botton.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        contexto.startActivity(intent);
                    }
                    else Toast.makeText(contexto, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void resetSenha(String email){
        if(!Metodos.verificarEmail(email,contexto)) return;;
        esqueceuSenha = (EsqueceuSenha) contexto;
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(contexto, R.string.linkenviado, Toast.LENGTH_SHORT).show();
                    esqueceuSenha.finish();
                }
                else Toast.makeText(contexto,R.string.errolinkenviado, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
