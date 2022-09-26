package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView btnRegistro;
    TextInputEditText txtNombre, txtEmail, txtPassword;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegistro = findViewById(R.id.btnRegistro);
        txtNombre = findViewById(R.id.txtNombre);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Usuarios = database.getReference("/Usuarios");

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    void registerUser() {
        final String nombre = txtNombre.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();

        if(!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification();
                            String id = mAuth.getCurrentUser().getUid();
                            saveUser(id, nombre, email);
                            Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, "Email de verificación enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(RegisterActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

     void saveUser(String id, String nombre, String email) {
        Users users = new Users();
        users.setEmail(email);
        users.setNombre(nombre);

        Usuarios.child(id).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "Registro erroneo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}