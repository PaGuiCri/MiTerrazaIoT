package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText textInputEmail, textInputPassword;
    TextView btnLogin;

    FirebaseAuth mAuth;
    DatabaseReference database;
    SharedPreferences comPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEmail = findViewById(R.id. textInputEmail);
        textInputPassword = findViewById(R.id. textInputPassword);
        btnLogin = findViewById(R.id.btnGoToLogin);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        comPref = getApplicationContext().getSharedPreferences("ProtCom", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email = textInputEmail.getText().toString();
        String password = textInputPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Log.w(Utilities.class.getSimpleName(), "campo no encontrado");
                                FirebaseAuth.getInstance().getCurrentUser().reload();
                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.w(Utilities.class.getSimpleName(), "Usuario reautenticado");
                                    }
                                });
                                if (user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Login completado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity.this, "Email no Verificado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }else {
                Toast.makeText(this, "La contraseña debe tener más de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Correo electrónico y contraseña obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
}