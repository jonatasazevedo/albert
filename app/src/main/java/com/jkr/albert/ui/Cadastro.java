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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jkr.albert.FireBase.Authentication;
import com.jkr.albert.Mecanica.CircleTransform;
import com.jkr.albert.Mecanica.Metodos;
import com.jkr.albert.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cadastro extends AppCompatActivity {
    private EditText enome_comp,eemail,esenha;
    private Button botao_sair,botao_cadastro;
    private String senha,nome_comp,email;
    private ProgressBar progressBar;
    private Authentication authentication;
    private CircleImageView imageView;
    private Button escolherImg;
    private final int IMAGEM_INTERNA = 12;
    private Uri FilePath;
    private boolean upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        authentication = new Authentication(Cadastro.this);
        getComponentes();
        progressBar.setVisibility(View.GONE);
        botao_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!upload) finish();
                else Toast.makeText(Cadastro.this,R.string.andamento,Toast.LENGTH_SHORT).show();
            }
        });
        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConteudo();
                cad();
            }
        });
        escolherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pegarImagem();
            }
        });
    }

    private void getComponentes() {
        botao_cadastro = findViewById(R.id.activity_cadastro_botao_tela_cadastro);
        botao_sair = findViewById(R.id.activity_cadastro_botao_sair);
        eemail = findViewById(R.id.activity_cadastro_input_email);
        enome_comp = findViewById(R.id.activity_cadastro_input_nome_completo);
        esenha = findViewById(R.id.activity_cadastro_input_senha);
        progressBar = findViewById(R.id.progressBar);
        escolherImg = findViewById(R.id.bt_escolher_img_usuario);
        imageView = findViewById(R.id.img_usuario);
    }

    private void getConteudo() {
        nome_comp = enome_comp.getText().toString();
        email = eemail.getText().toString().trim();
        senha = esenha.getText().toString().trim();
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
            Picasso.get().load(caminho).transform(new CircleTransform()).into(imageView);
        }
    }

    private void uCrop(Uri uri) {
        String destinationFileName = "SampleCropImg"+System.currentTimeMillis()+".jpg";
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(this);
    }

    private void cad(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("usuarios");
        if (!Metodos.verificar(email, senha, Cadastro.this)) return;
        if (!Metodos.isOnline(Cadastro.this)) return;
        if(FilePath==null){
            Toast.makeText(this,"Nenhuma imagem selecionada",Toast.LENGTH_SHORT).show();
            return;
        }
        if(upload){
            Toast.makeText(this,R.string.andamento,Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+".png");
        upload=true;
        fileReference.putFile(FilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        authentication.getFirebaseAuth().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser currentUser = authentication.getFirebaseAuth().getCurrentUser();
                                    final UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nome_comp).setPhotoUri(uri);
                                    currentUser.updateProfile(builder.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(getBaseContext(),Tela_Principal_botton.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
