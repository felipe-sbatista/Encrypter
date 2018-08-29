package br.com.felipe.encrypter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import br.com.felipe.utils.Constantes;

public class CriptActivity extends AppCompatActivity {

   private Button botao;
   private String modo;
   private Switch botaoSwitch;
   private byte[] senha;
   private EditText inputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cript);
        //config
        Intent intent = getIntent();
        modo = intent.getStringExtra("modo");

        //init
        botao = findViewById(R.id.selecionar_arquivo);
        botaoSwitch = findViewById(R.id.botao_switch);
        inputSenha = findViewById(R.id.textSenha);


        if(modo.equals(Constantes.DESCRIPTOGRAFAR_FILE)){
            botaoSwitch.setClickable(false);
            botaoSwitch.setVisibility(View.INVISIBLE);
        }

        botao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String password = "0";
                if(inputSenha.length() > 0){
                    password = inputSenha.getText().toString();
                }
                try {
                    senha = gerarSenha(password);
                } catch (Exception e){
                    e.printStackTrace();
                }
                new MaterialFilePicker()
                        .withActivity(CriptActivity.this)
                        .withRequestCode(1000)
                        .withHiddenFiles(true)
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
                case Constantes.CRIPTOGRAFAR_FILE:{
                    if(botaoSwitch.isChecked()) {
                        filePath = ajustarPath(filePath);
                    }
                    configCripter(Cipher.ENCRYPT_MODE, file , filePath);
                    break;
                }

                case Constantes.DESCRIPTOGRAFAR_FILE:{
                    configCripter(Cipher.DECRYPT_MODE, file , filePath);
                    break;
                }
            }

        }
        transicaoActivity();
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


    private void configCripter(int modoCipher, File origem, String destino){
        byte [] result;
        try {
            result = execute(modoCipher,origem);
            if(result == null){
                transicaoActivity();
                return;
            }
            if(!botaoSwitch.isChecked()){
                origem.delete();
            }
            FileOutputStream outputFile = new FileOutputStream(new File(destino));

            //save
            outputFile.write(result);
            outputFile.close();
            exibirMensagem("Operacação completa!");
        } catch(Exception e){
            e.printStackTrace();
            exibirMensagem("Não foi possível realizar a descriptografia");
        }
    }

    private byte[] execute(int modoCipher, File origem) {
        byte[] input, result = null;
        try {
            //configuracoes
            FileInputStream fileOrigem = new FileInputStream(origem);
            SecretKeySpec key = new SecretKeySpec(senha, "AES");
            //set tipo de criptografia
            Cipher cipher = Cipher.getInstance("AES");
            //set o modo
            cipher.init(modoCipher, key);

            input = new byte[(int) origem.length()];
            fileOrigem.read(input);
            fileOrigem.close();

            //executa
            result  = cipher.doFinal(input);

        } catch (InvalidKeyException eW) {
            exibirMensagem("Senha incorreta!");
            transicaoActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String ajustarPath(String path){
        String paths [] = path.split("/");
        String pathFinal="";
        paths[paths.length-1] ="protegido-"+ paths[paths.length - 1];
        for(String s:paths){
            pathFinal = pathFinal + "/"+ s;
        }
        return pathFinal;
    }


    private byte[] gerarSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(senha.getBytes("UTF-8"));
    }

    private void transicaoActivity(){
        Intent intent = new Intent(CriptActivity.this, SelectCriptActivity.class);
        startActivity(intent);
        finish();
    }

    private void exibirMensagem(String msg){
        Toast toast = Toast.makeText(CriptActivity.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}
