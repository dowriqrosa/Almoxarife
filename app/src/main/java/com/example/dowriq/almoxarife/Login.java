package com.example.dowriq.almoxarife;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText edtEmail, edtSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        //edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.tLogin);
        edtSenha = (EditText) findViewById(R.id.tSenha);
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //FirebaseAuth.getInstance().getCurrentUser().getUid()
                    Intent it = new Intent(getApplicationContext(), Principal.class);
                    startActivity(it);
                }
            }
        };
    }

    public void btnCadastrar(View v){
        Intent i = new Intent(this, cadastro_usuarios.class);
        //i.putExtra("msgRetorno", "Bem vindo!");
        //finish();
        startActivity(i);
    }

    public void btnLogin(View v){
        try {
            final Intent i = new Intent(this, Principal.class);
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Intent i = new Intent(this, Principal.class);
                                //i.putExtra("msgRetorno", "Bem vindo!");
                                finish();
                                startActivity(i);
                                // Sign in success, update UI with the signed-in user's information
                                ///Log.d(TAG, "signInWithEmail:success");
                                // FirebaseUser user = mAuth.getCurrentUser();
                                ///updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //Toast.makeText(Login.this, "Authentication failed.",
                                // Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                Toast.makeText(getApplicationContext(), "erro!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            finish();
        }
    }
}
