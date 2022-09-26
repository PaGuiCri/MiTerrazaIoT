package com.pablo.miterrazaiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectOptionAuthActivity extends AppCompatActivity {

    TextView btnGoToRegistro, btnGoToLogin;
    SharedPreferences comPref;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToRegistro = findViewById(R.id.btnGoToRegistro);

        comPref = getApplicationContext().getSharedPreferences("ProtCom", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        btnGoToRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegistro();
            }
        });
        
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            /*String protCom = comPref.getString("Usuario", "");
            if(protCom.equals("Bt")) {
                Intent intent = new Intent(SelectOptionAuthActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Intent intent = new Intent(SelectOptionAuthActivity.this, informacionBT.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }*/
            Intent intent = new Intent(SelectOptionAuthActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToRegistro() {
        Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}