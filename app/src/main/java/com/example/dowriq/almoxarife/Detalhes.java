package com.example.dowriq.almoxarife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Detalhes extends AppCompatActivity {
    private Estoque p;
    private EditText edtQuantidade;
    private EditText edtNome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        p = (Estoque)getIntent().getSerializableExtra("estoque");
        if(p != null){
            edtNome = (EditText) findViewById(R.id.nomeEquipamento);
            edtQuantidade = (EditText) findViewById(R.id.quantidade);
            edtNome.setText(p.getNome());
            edtQuantidade.setText(Integer.toString(p.getQuantidade()));
            //edtQuantidade.setText(Integer.toString(p.getQuantidade()));
            edtNome.setEnabled(false);
            edtQuantidade.setEnabled(false);
            ImageView imageView = findViewById(R.id.imgCapturar);
            Picasso.with(this).load(p.getImg())
                    .into(imageView);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.bVoltar) {
            Intent i = new Intent(this, Login.class);
            i.putExtra("msgRetorno", "Bem vindo!");
            this.finish();
            startActivity(i);
        }
    }
}
