package com.pablo.miterrazaiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectBtWiFiActivity extends AppCompatActivity {

    Button btnBluetooth, btnWiFi;
    TextView txtTitSelectProtocol;
    ImageView imgMonstera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bt_wi_fi);

        txtTitSelectProtocol = findViewById(R.id.txtTitSelectProtocolo);
        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnWiFi = findViewById(R.id.btnWifi);
        imgMonstera = findViewById(R.id. img_monstera);


        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             goToBluetooth();
            }
        });

        btnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWiFi();
            }
        });
    }

    private void goToWiFi() {
        Intent intent3 = new Intent(SelectBtWiFiActivity.this, MainActivity.class);
        startActivity(intent3);
    }

    private void goToBluetooth() {
        Intent intent4 = new Intent(SelectBtWiFiActivity.this, informacionBT.class);
        startActivity(intent4);
    }
}