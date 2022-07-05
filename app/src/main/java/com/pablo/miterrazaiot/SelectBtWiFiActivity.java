package com.pablo.miterrazaiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectBtWiFiActivity extends AppCompatActivity {

    Button BtnBluetooth, BtnWiFi;
    TextView TxtTitSelectProtocol;
    ImageView ImgMonstera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bt_wi_fi);

        TxtTitSelectProtocol = findViewById(R.id. txt_tit_select_protocolo);
        BtnBluetooth = findViewById(R.id. btn_bluetooth);
        BtnWiFi = findViewById(R.id. btn_wifi);
        ImgMonstera = findViewById(R.id. img_monstera);


        BtnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             goToBluetooth();
            }
        });

        BtnWiFi.setOnClickListener(new View.OnClickListener() {
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