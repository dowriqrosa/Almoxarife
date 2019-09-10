package com.example.dowriq.almoxarife;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cadastro_usuarios extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText nomeCompleto, email, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuarios);
        mAuth = FirebaseAuth.getInstance();

        nomeCompleto = (EditText) findViewById(R.id.NomeCompleto);
        email = (EditText) findViewById(R.id.Email);
        senha = (EditText) findViewById(R.id.Senha);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.bVoltar) {
            Intent i = new Intent(this, Login.class);
            //i.putExtra("msgRetorno", "Bem vindo!");
            finish();
            startActivity(i);
        }
    }

    public void btSalvar(View view){

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(cadastro_usuarios.this, "Erro ao criar usuário!",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nome").setValue(nomeCompleto.getText().toString());
                            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email.getText().toString());
                            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(cadastro_usuarios.this, "Bem Vindo!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //DatabaseReference myRef = database.getReference("Estoque").push();
                           // myRef.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            pLogin();
                        }
                    }
                });
    }

    public void pLogin(){
        Intent i = new Intent(this, Login.class);
        //i.putExtra("msgRetorno", "Bem vindo!");
        finish();
        startActivity(i);
    }
}
