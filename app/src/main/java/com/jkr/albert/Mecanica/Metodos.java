package com.jkr.albert.Mecanica;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jkr.albert.R;
import com.yalantis.ucrop.UCrop;

public class Metodos {
    static int select = R.id.navigation_feed;

    public static int getSelect() {
        return select;
    }

    public static void setSelect(int select1) {
        select = select1;
    }

    public static boolean verificarEmail(String email, Context contexto){
        if(email.equals("")){
            Toast.makeText(contexto, R.string.campos, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(contexto,R.string.email_invalido,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public  static boolean verificarSenha(String senha,Context contexto){
        if(senha.equals("")){
            Toast.makeText(contexto, R.string.campos, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.length()<6){
            Toast.makeText(contexto,R.string.senha_invalida,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isOnline(Context contexto) {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) return true;
        else{
            Toast.makeText(contexto, R.string.internet, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean verificar(String email, String senha, Context context){
        if(!verificarEmail(email,context)) return false;
        if(!verificarSenha(senha,context)) return false;
        return true;
    }
    public static boolean verificarEditText3(EditText inp1,EditText inp2,EditText inp3){
        if(inp1.getText().toString().equals("") || inp2.getText().toString().equals("") && inp3.getText().toString().equals("")) return false;
        return true;
    }

    public static boolean verificarEditText2(EditText inp1,EditText inp2){
        if(inp1.getText().toString().equals("") || inp2.getText().toString().equals("")) return false;
        return true;
    }


    public static int getaIdDrawable(Context context,String name){
        Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }
}
