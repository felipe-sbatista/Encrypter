package br.com.felipe.encrypter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.felipe.utils.Constantes;

public class SelectHideActivity extends AppCompatActivity {

    Button botaoHide, botaoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hide);

        botaoShow = findViewById(R.id.showBotao);
        botaoHide = findViewById(R.id.hideBotao);

        botaoShow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectHideActivity.this, HideActivity.class);
                intent.putExtra("modo", Constantes.SHOW_FILE);
                startActivity(intent);
            }
        });

        botaoHide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectHideActivity.this, HideActivity.class);
                intent.putExtra("modo", Constantes.HIDE_FILE);
                startActivity(intent);
                finish();
            }
        });
    }

}
