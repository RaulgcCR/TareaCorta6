package com.example.raul.inventario;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class Edit_Product extends AppCompatActivity {

    int productID;
    ImageView imageView;
    StorageReference imageRef;
    Uri pathURL;
    private Uri mImageUri;
    EditText editTextNombre, editTextPrecio, editTextDescripcion;
    StorageReference storageRef;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__product);

        sharedPreferences = this.getSharedPreferences("Firebase", getApplicationContext().MODE_PRIVATE);
        storageRef = FirebaseStorage.getInstance().getReference();

        editTextNombre = (EditText)findViewById(R.id.editText_Nombre);
        editTextPrecio = (EditText)findViewById(R.id.editText_Precio);
        editTextDescripcion = (EditText)findViewById(R.id.editText_Descripcion);
        imageView = (ImageView) findViewById(R.id.imageViewImagen);
        Button button = (Button) findViewById(R.id.botonIngresar);

        Intent intent = getIntent();
        productID = intent.getIntExtra("ProductID", -1);

        if(productID == -1){
            //nuevo producto
            productID = Lista_Productos_Activity.productos.size() + 1;

        }else{
            //producto existente
            button.setVisibility(View.INVISIBLE);
            editTextNombre.setEnabled(false);
            editTextPrecio.setEnabled(false);
            editTextDescripcion.setEnabled(false);
            imageView.setEnabled(false);

            editTextNombre.setText(Lista_Productos_Activity.productos.get(productID).getNombre());
            editTextPrecio.setText(String.valueOf(Lista_Productos_Activity.productos.get(productID).getPrecio()));
            editTextDescripcion.setText(Lista_Productos_Activity.productos.get(productID).getDescripcion());
            Bitmap bitmap = null;
            try {
                bitmap = new ImageTask().execute(Lista_Productos_Activity.productos.get(productID).getImagen()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    public void botonIngresarClicked(View view){
        Producto producto = new Producto(editTextNombre.getText().toString(), Float.parseFloat(editTextPrecio.getText().toString()),
                editTextDescripcion.getText().toString(), pathURL.toString());

        Lista_Productos_Activity.productos.add(producto);

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("producto" + productID).setValue(producto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Producto ingresado correctamente...",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }


    public void setPhotoButton(View view){
        cargarImagen();
    }

    private void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (/*requestCode == ACTIVITY_SELECT_IMAGE &&*/ resultCode == RESULT_OK) {
            Uri path = data.getData();
            mImageUri = path;
            imageRef = storageRef.child("Imagen" + productID + ".jpg");

            imageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Edit_Product.this, "Se subio correctamente", Toast.LENGTH_SHORT).show();
                    pathURL = taskSnapshot.getDownloadUrl();
                }
            });
            imageView.setImageURI(path);

        }
    }
}
