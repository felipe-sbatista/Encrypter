package br.com.felipe.encrypter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.felipe.utils.Constantes;


public class SelectCriptActivity extends AppCompatActivity {

    Button botaoEncript, botaoDescript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cript);

        botaoDescript = findViewById(R.id.botaoDescript);
        botaoEncript = findViewById(R.id.botaoEncript);

        botaoEncript.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(SelectCriptActivity.this, CriptActivity.class);
               intent.putExtra("modo", Constantes.CRIPTOGRAFAR_FILE);
               startActivity(intent);
               finish();
           }
       });

        botaoDescript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectCriptActivity.this, CriptActivity.class);
                intent.putExtra("modo", Constantes.DESCRIPTOGRAFAR_FILE);
                startActivity(intent);
                finish();
            }
        });
    }


}