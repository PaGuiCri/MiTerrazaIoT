package com.pablo.miterrazaiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectBtWiFiActivity extends AppCompatActivity {

    TextView txtTitSelectProtocol,btnBluetooth, btnWiFi;
    ImageView imgMonstera;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usuarios;
    DatabaseReference dataProtCom;
    SharedPreferences comPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bt_wi_fi);

        txtTitSelectProtocol = findViewById(R.id.txtTitSelectProtocolo);
        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnWiFi = findViewById(R.id.btnWifi);
        imgMonstera = findViewById(R.id. img_monstera);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usuarios = database.getReference("/Usuarios");
        String id = mAuth.getCurrentUser().getUid();
        dataProtCom = database.getReference("/Usuarios" + "/" + id + "/ProtCom");

        comPref = getApplicationContext().getSharedPreferences("ProtCom", MODE_PRIVATE);
        SharedPreferences.Editor editor = comPref.edit();

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Usuario", "Bt");
                editor.apply();
                String protCom = comPref.getString("Usuario", "");
                dataProtCom.setValue(protCom);
                goToBluetooth();
            }
        });

        btnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Usuario", "Wifi");
                editor.apply();
                String protCom = comPref.getString("Usuario", "");
                dataProtCom.setValue(protCom);
                goToWiFi();
            }
        });
    }

    private void goToWiFi() {
        Intent intent = new Intent(SelectBtWiFiActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToBluetooth() {
        Intent intent = new Intent(SelectBtWiFiActivity.this, informacionBT.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}