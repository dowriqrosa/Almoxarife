package com.example.dowriq.almoxarife;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;


public class cadastro_equipamentos extends AppCompatActivity {
    private Bitmap bitmap;
    //private StorageReference mStorageRef;
    private Estoque produto;
    private UploadTask uploadTask;
    //private String urlFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_equipamentos);
        //ImageView imageView = (ImageView) findViewById(R.id.imgCapturar);
        // Assume thisActivity is the current activity
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void onClick(View v) {
        if (v.getId() == R.id.bVoltar) {
            Intent i = new Intent(this, Login.class);
            //i.putExtra("msgRetorno", "Bem vindo!");
            this.finish();
            startActivity(i);
        }else if(v.getId() == R.id.imgCapturar){
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,0);
        }
    }

    public void btSalvar(View v){
        try{
            EditText edtNome = (EditText) findViewById(R.id.nomeEquipamento);
            EditText edtQuantidade = (EditText) findViewById(R.id.quantidade);
            produto = new Estoque();
            produto.setNome(edtNome.getText().toString());
            produto.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString()));
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("Estoque").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
            myRef.setValue(produto);
            produto.setId(myRef.getKey().toString());
            myRef.setValue(produto);
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Almoxarife").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(produto.getId());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            uploadTask = mStorageRef.putBytes(data);
            //produto.setImg(mStorageRef.getDownloadUrl().toString());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                   produto.setImg(taskSnapshot.getMetadata().getDownloadUrl().toString());
                   myRef.setValue(produto);
                }
            });
            //produto.setImg(mStorageRef.getMetadata().getResult().getDownloadUrl().toString());
            //upImg();
            //produto.setImg(urlFoto);
           // myRef.setValue(produto);
            Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent cadastro = new Intent(this, Principal.class);
            this.finish();
            startActivity(cadastro);
        }catch (Exception e){
            e.getStackTrace();
            Toast.makeText(this, "erro!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(data !=null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imgCapturar);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            imageView.setImageBitmap(bitmap);
        }
    }

}
