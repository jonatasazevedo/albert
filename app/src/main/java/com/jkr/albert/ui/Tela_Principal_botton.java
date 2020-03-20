package com.jkr.albert.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;

import java.util.Objects;

public class Tela_Principal_botton extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private int select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        navigationView = findViewById(R.id.navigationView);
        setFragments();
    }

    private void acaoNavigationView(){
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment itemSelecionado = null;
                if(menuItem.getItemId()==select) return true;
                switch (menuItem.getItemId()){
                    case R.id.navigation_user:
                        itemSelecionado = new Perfil();
                        Metodos.setSelect(R.id.navigation_user);
                        break;
                    case R.id.navigation_feed:
                        itemSelecionado = new Feed();
                        Metodos.setSelect(R.id.navigation_feed);
                        break;
                    case R.id.navigation_material:
                        itemSelecionado = new Materias();
                        Metodos.setSelect(R.id.navigation_material);
                        break;
                    case R.id.navigation_palestra:
                        itemSelecionado = new Palestra();
                        Metodos.setSelect(R.id.navigation_palestra);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Objects.requireNonNull(itemSelecionado)).commit();
                return true;
            }
        });
    }

    private void setFragments() {
        Fragment itemSelecionado = null;
        switch (Metodos.getSelect()){
            case R.id.navigation_user:
                itemSelecionado = new Perfil();
                break;
            case R.id.navigation_feed:
                itemSelecionado = new Feed();
                break;
            case R.id.navigation_material:
                itemSelecionado = new Materias();
                break;
            case R.id.navigation_palestra:
                itemSelecionado = new Palestra();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Objects.requireNonNull(itemSelecionado)).commit();
        acaoNavigationView();
    }
}
