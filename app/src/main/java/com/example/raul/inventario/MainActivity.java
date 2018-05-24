package com.example.raul.inventario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    TextView textView_email, textView_pass;
    Button button_log_in, button_sign_in;
    SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView_email = (TextView) findViewById(R.id.editText_email);
        textView_pass = (TextView) findViewById(R.id.editText_pass);
        button_log_in = (Button) findViewById(R.id.button_log_in);
        button_sign_in = (Button) findViewById(R.id.button_sign_in);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

    }

    public void crearCuenta(View view){
        String email = textView_email.getText().toString();
        String password = textView_pass.getText().toString();
        Log.d("EMAIL", "Email is:" + email);
        if(!email.isEmpty() && !password.isEmpty()){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("CREATE_USER", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Error creando cuenta...\n" + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(MainActivity.this, Lista_Productos_Activity.class);
                                intent.putExtra("USER", mAuth.getCurrentUser().getEmail().toString());
                                startActivity(intent);
                            }

                            // ...
                        }
                    });
        }
    }

    public void iniciarSesion(View view){
        String email = textView_email.getText().toString();
        String password = textView_pass.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("LOG_IN_USER", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("LOG_IN_USER", "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, "Error iniciando sesión...\n" + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(MainActivity.this, Lista_Productos_Activity.class);
                                intent.putExtra("USER", mAuth.getCurrentUser().getEmail().toString());
                                startActivity(intent);

                            }

                            // ...
                        }
                    });
        }
    }
}
