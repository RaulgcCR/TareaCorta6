package com.example.raul.inventario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Lista_Productos_Activity extends AppCompatActivity {

    TextView textView;
    static ArrayList<Producto> productos = new ArrayList<Producto>();
    static ArrayAdapter arrayAdapter;
    CustomListView customListView;
    ListView listView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__productos_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("Firebase", getApplicationContext().MODE_PRIVATE);

        int ID = sharedPreferences.getInt("ID", -1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_new_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Edit_Product.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        textView = (TextView) findViewById(R.id.email);
        textView.setText(intent.getStringExtra("USER"));

        listView = (ListView)findViewById(R.id.products_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Edit_Product.class);
                intent.putExtra("ProductID", position);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int noteid = position;
                new AlertDialog.Builder(Lista_Productos_Activity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Producto...").setMessage("Esta seguro?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productos.remove(noteid);
                        arrayAdapter.notifyDataSetChanged();


                    }
                }).setNegativeButton("No", null).show();
                return true;
            }
        });


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                Iterator<DataSnapshot> productosDB = dataSnapshot.getChildren().iterator();
                productos.clear();
                while(productosDB.hasNext()){
                    DataSnapshot productoDB= productosDB.next();
                    Producto producto= productoDB.getValue(Producto.class);
                    productos.add(producto);
                }

                customListView = new CustomListView(Lista_Productos_Activity.this, productos);
                listView.setAdapter(customListView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), Edit_Product.class); //CAMBIAR
                        intent.putExtra("ProductID", position);
                        startActivity(intent);
                    }

                });
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_product) {
            Intent intent = new Intent(getApplicationContext(), Edit_Product.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
