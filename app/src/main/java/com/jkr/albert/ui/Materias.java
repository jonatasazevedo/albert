package com.jkr.albert.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jkr.albert.R;
import com.jkr.albert.model.Constantes;

public class Materias extends Fragment{
    private ImageButton[] botao_materia = new ImageButton[10];
    private String[] conteudo = {"linguaportuguesa","matematica","artes","historia","filosofia","geografia","quimica","biologia","fisica","sociologia"};
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_materias,container,false);
        getComponentes(v);
        acaodosbotoes();
        return v;
    }

    private void getComponentes(View v){
        botao_materia[0] = v.findViewById(R.id.bt_lingua_portuguesa);
        botao_materia[1] = v.findViewById(R.id.bt_matematica);
        botao_materia[2] = v.findViewById(R.id.bt_artes);
        botao_materia[3] = v.findViewById(R.id.bt_historia);
        botao_materia[4] = v.findViewById(R.id.bt_filosofia);
        botao_materia[5] = v.findViewById(R.id.bt_geografia);
        botao_materia[6] = v.findViewById(R.id.bt_quimica);
        botao_materia[7] = v.findViewById(R.id.bt_biologia);
        botao_materia[8] = v.findViewById(R.id.bt_fisica);
        botao_materia[9] = v.findViewById(R.id.bt_sociologia);
    }

    private void acaodosbotoes(){
        for(int i=0;i<10;i++){
            final int finalI = i;
            botao_materia[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),Material.class);
                    intent.putExtra(Constantes.CHAVE_MATERIA,conteudo[finalI]);
                    startActivity(intent);
                }
            });
        }
    }

}
