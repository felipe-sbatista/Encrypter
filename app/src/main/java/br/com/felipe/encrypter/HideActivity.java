package br.com.felipe.encrypter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

import br.com.felipe.utils.Constantes;


public class HideActivity extends AppCompatActivity {

    private Button botaoHider;
    private String modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide);

        //config
        Intent intent = getIntent();
        modo = intent.getStringExtra("modo");
        botaoHider = findViewById(R.id.botaoHider);
        botaoHider.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(HideActivity.this)
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
                case Constantes.HIDE_FILE:{
                    hideFile(file);
                    break;
                }

                case Constantes.SHOW_FILE:{
                    showFile(file);
                    break;
                }
            }

        }
        Intent intent = new Intent(HideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    protected void hideFile(File file){
        String [] nomes = file.getPath().split("/");
        String nome = nomes[nomes.length-1];
        if(nome.charAt(0) == '.'){
            exibirMensagem("O arquivo já está oculto!");
        }else{
            String path = setPath(nomes);
            File novo = new File(path + "."+nome);
            file.renameTo(novo);
            exibirMensagem("Operação concluida!");
        }
    }

    protected void showFile(File file){
        String [] nomes = file.getPath().split("/");
        String nome = nomes[nomes.length-1];
        if(nome.charAt(0) != '.'){
            exibirMensagem("O arquivo não está oculto!");
        }else{
            String path = setPath(nomes);
            File novo = new File(path + nome.substring(1,nome.length()));
            file.renameTo(novo);
            exibirMensagem("Operação concluida!");
        }
    }

    private void exibirMensagem(String msg){
        Toast toast = Toast.makeText(HideActivity.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private String setPath(String paths[]){
        String pathFinal="";
        for(int i=0; i<paths.length-1; i++){
            pathFinal = pathFinal +paths[i] + "/";
        }
        return pathFinal;
    }

}