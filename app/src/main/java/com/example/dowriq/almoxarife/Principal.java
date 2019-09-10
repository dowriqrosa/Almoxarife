package com.example.dowriq.almoxarife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Estoque> list = new ArrayList<Estoque>();
    private ListView listView;
    private StorageReference desertRef;
    private SwipeRefreshLayout swipeLayout;
    //private FirebaseAuth mAuth;
    private EditText nomeCompleto, email, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //your method to refresh content
               // listView.removeAllViews();
                carregarListView();
            }
        });
        carregarListView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefEmail = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email");
        DatabaseReference myRefUser = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nome");
        // Read from the database
        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView email = (TextView) findViewById(R.id.textEmail);
                email.setText(value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView nome = (TextView) findViewById(R.id.textNome);
                nome.setText(value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.excluir) {
            //String selecionados = "";

            //Cria um array com os itens selecionados no listview
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            if(checked.size() != 0){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Estoque").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                desertRef = FirebaseStorage.getInstance().getReference("Almoxarife").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                for (int i = 0; i < checked.size(); i++) {
                    Estoque produto = (Estoque) listView.getItemAtPosition(checked.keyAt(i));
                    //pega os itens marcados
                    myRef.child(produto.getId()).removeValue();
                    desertRef.child(produto.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                }
                Toast.makeText(this, "Equipamento Apagado com Sucesso!", Toast.LENGTH_LONG).show();
                carregarListView();
            }else{
                Toast.makeText(this, "Selecione Um Equipamento!", Toast.LENGTH_LONG).show();
                //carregarListView();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //ConstraintLayout lista = (ConstraintLayout) findViewById(R.id.LLista);
        if (id == R.id.cEquipamento) {
            Intent cadastro = new Intent(this, cadastro_equipamentos.class);
            this.finish();
            startActivity(cadastro);
        } else if (id == R.id.eSaida) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            for (int i = 0; i < checked.size(); i++) {
                Estoque estoque = (Estoque) listView.getItemAtPosition(checked.keyAt(i));
                //pega os itens marcados
                Intent saida = new Intent(getApplicationContext(),saida_equipamentos.class);
                saida.putExtra("estoque",estoque);
                this.finish();
                startActivity(saida);
            }

        } else if (id == R.id.eEntrada) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            for (int i = 0; i < checked.size(); i++) {
                Estoque estoque = (Estoque) listView.getItemAtPosition(checked.keyAt(i));
                //pega os itens marcados
                Intent entrada = new Intent(getApplicationContext(),Entrada_Equipamentos.class);
                entrada.putExtra("estoque",estoque);
                this.finish();
                startActivity(entrada);
            }
        } else if (id == R.id.detalhe) {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            for (int i = 0; i < checked.size(); i++) {
                Estoque estoque = (Estoque) listView.getItemAtPosition(checked.keyAt(i));
                //pega os itens marcados
                Intent detalhes = new Intent(getApplicationContext(),Detalhes.class);
                detalhes.putExtra("estoque",estoque);
                this.finish();
                startActivity(detalhes);
            }
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void carregarListView() {
        //função parada deo refesh
        listView = (ListView) findViewById(R.id.listEstoque);
        final ArrayAdapter<Estoque> adaptador = new ArrayAdapter<Estoque>(this, android.R.layout.simple_list_item_checked, list);
        adaptador.clear();
        listView.setAdapter(adaptador);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Estoque").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getValue(Estoque.class));
                adaptador.notifyDataSetChanged();
                if(swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(Estoque.class));
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
