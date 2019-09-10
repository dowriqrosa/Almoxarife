package com.example.dowriq.almoxarife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class saida_equipamentos extends AppCompatActivity {
    private Estoque p;
    EditText edtQuantidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saida_equipamentos);
        p = (Estoque)getIntent().getSerializableExtra("estoque");
        if(p != null){
            EditText edtNome = (EditText) findViewById(R.id.nomeEquipamento);
            edtNome.setText(p.getNome());
            //edtQuantidade.setText(Integer.toString(p.getQuantidade()));
            edtNome.setEnabled(false);
            ImageView imageView = findViewById(R.id.imgCapturar);

            Picasso.with(this).load(p.getImg())
                    //.resize(100, 100)
                    //.centerCrop()
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
    public void btnSaida(View v){
        if(validar()) {
            try {
                edtQuantidade = (EditText) findViewById(R.id.quantidade);
                int quant = p.getQuantidade();
                quant -= Integer.parseInt(edtQuantidade.getText().toString());
                p.setQuantidade(quant);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Estoque").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(p.getId()).child("quantidade");
                myRef.setValue(p.getQuantidade());
                Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Login.class);
                i.putExtra("msgRetorno", "Bem vindo!");
                this.finish();
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(this, "erro!", Toast.LENGTH_SHORT).show();
                e.getStackTrace();
            }
        }else{
            Toast.makeText(this, "Não ha produto suficiente em estoque!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validar(){
        int quant;
        try {
            edtQuantidade = (EditText) findViewById(R.id.quantidade);
            quant = Integer.parseInt(edtQuantidade.getText().toString());
            if (quant != 0 && quant <= p.getQuantidade()) {
                return true;
            } else {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }
    }
}
