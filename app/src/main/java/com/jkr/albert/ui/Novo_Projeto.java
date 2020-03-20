package com.jkr.albert.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.Mecanica.CircleTransform;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.jkr.albert.model.Projeto;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Novo_Projeto extends AppCompatActivity {
    private Uri FilePath;
    private boolean upload;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Button bt_sair,bt_salvar,bt_escolher_img;
    private EditText input_nome,input_objetivo,input_numagencia;
    private String numaagencia,nome,nomeusuario;
    private double objetivo;
    private CircleImageView img_projeto;
    private final int IMAGEM_INTERNA = 12;
    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference("projetos");
        databaseReference = FirebaseDatabase.getInstance().getReference("projetos");
        setContentView(R.layout.activity_novo__projeto);
        authentication = new Authentication(Novo_Projeto.this);
        puxarDadosUsuario();
        getComponents();
        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!upload) {
                    if(Metodos.isOnline(Novo_Projeto.this)) {
                        if(Metodos.verificarEditText3(input_nome,input_numagencia,input_objetivo)) {
                            getDados();
                            if(objetivo!=0) uploadImage();
                            else Toast.makeText(Novo_Projeto.this, R.string.zero, Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(Novo_Projeto.this, R.string.campos, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(Novo_Projeto.this,R.string.internet,Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Novo_Projeto.this,R.string.andamento,Toast.LENGTH_SHORT).show();
            }
        });
        bt_escolher_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pegarImagem();
            }
        });
        bt_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!upload) finish();
                else Toast.makeText(Novo_Projeto.this,R.string.andamento,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void puxarDadosUsuario() {
        nomeusuario = authentication.getCurrentUser().getDisplayName();
    }

    private void getDados(){
        objetivo = Double.parseDouble(input_objetivo.getText().toString());
        numaagencia = input_numagencia.getText().toString().trim();
        nome = input_nome.getText().toString().trim();
    }

    public void pegarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGEM_INTERNA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEM_INTERNA && resultCode == RESULT_OK) {
            FilePath = data.getData();
            uCrop(FilePath);
        }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            final Uri caminho = UCrop.getOutput(data);
            FilePath = caminho;
            Picasso.get().load(caminho).transform(new CircleTransform()).into(img_projeto);
        }
    }

    private void uCrop(Uri uri) {
        String destinationFileName = "SampleCropImg"+System.currentTimeMillis()+".jpg";
        UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)))
                .withAspectRatio(1, 1)
                .start(this);
    }

    private void getComponents(){
        input_objetivo = findViewById(R.id.input_objetivo);
        input_numagencia = findViewById(R.id.input_numagencia);
        input_nome = findViewById(R.id.input_nomeprojeto);
        bt_salvar = findViewById(R.id.activity_novo_projeto_botao_salvar);
        bt_escolher_img = findViewById(R.id.bt_escolher_img_novo_projeto);
        img_projeto = findViewById(R.id.img_novo_projeto);
        bt_sair = findViewById(R.id.activity_esq_botao_sair);
        progressBar = findViewById(R.id.progressBar);
    }
    private void uploadImage() {
        if(FilePath!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+".png");
            upload=true;
            fileReference.putFile(FilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String id = databaseReference.push().getKey();
                            Projeto proj = new Projeto(id,uri.toString(),nome,numaagencia,objetivo,authentication.getUID(),nomeusuario);
                            databaseReference.child(id).setValue(proj);
                            Toast.makeText(Novo_Projeto.this,R.string.projeto_sal,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Novo_Projeto.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
        else Toast.makeText(this,R.string.nenhumaselecionada,Toast.LENGTH_SHORT).show();
    }
}
