package br.com.felipe.encrypter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button botaoCript, botaoHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }

        botaoCript = findViewById(R.id.botaoCript);
        botaoHide = findViewById(R.id.botaoHide);

        botaoCript.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, SelectCriptActivity.class);
               startActivity(intent);
           }
       });

        botaoHide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectHideActivity.class);
                startActivity(intent);
            }
        });
    }


}
