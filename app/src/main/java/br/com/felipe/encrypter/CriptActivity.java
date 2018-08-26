package br.com.felipe.encrypter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CriptActivity extends AppCompatActivity {

   private Button botao;
   private String modo;
   private Switch botaoSwitch;
   private static String senha = "0123456789abcdef";
   private TextInputLayout t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cript);
        Intent intent = getIntent();
        modo = intent.getStringExtra("modo");
        botao = findViewById(R.id.selecionar_arquivo);
        botaoSwitch = findViewById(R.id.botao_switch);
        if(modo.equals("Descriptografar")){
            botaoSwitch.setClickable(false);
            botaoSwitch.setVisibility(View.INVISIBLE);
        }

        botao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(CriptActivity.this)
                        .withRequestCode(1000)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            File file = new File(filePath);
            switch (this.modo){
                case "Criptografar":{
                    if(botaoSwitch.isChecked()) {
                        filePath = ajustarPath(filePath);
                    }
                    run(Cipher.ENCRYPT_MODE, file , filePath);
                    break;
                }

                case "Descriptografar":{
                    run(Cipher.DECRYPT_MODE, file , filePath);
                    break;
                }
            }

        }
        Intent intent = new Intent(CriptActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissao concedida", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permissao negada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void run(int modoCipher,File origem, String destino){
        try {
            //Init
            FileInputStream inputFile = new FileInputStream(origem);
            SecretKeySpec key = new SecretKeySpec(senha.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(modoCipher, key);
            byte[] input = new byte[(int) origem.length()];

            //parse
            inputFile.read(input);
            inputFile.close();
            File file;
            if(!botaoSwitch.isChecked()){
                origem.delete();
            }
            file = new File(destino);

            byte[] result = cipher.doFinal(input);

            FileOutputStream outputFile = new FileOutputStream(file);

            //save
            outputFile.write(result);
            outputFile.close();
            Toast toast = Toast.makeText(CriptActivity.this, "Operacação completa!", Toast.LENGTH_SHORT);
            toast.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String ajustarPath(String path){
        String paths [] = path.split("/");
        String pathFinal="";
        paths[paths.length-1] ="protegido-"+ paths[paths.length - 1];
        for(String s:paths){
            pathFinal = pathFinal + "/"+ s;
        }
        return pathFinal;
    }

}
