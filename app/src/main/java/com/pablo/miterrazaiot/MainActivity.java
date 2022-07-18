package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextView txtHumAmb, txtTempAmb, txtLimiteHum, txtHumSus, txtTiempoRiego;
    Button btnAccesoTiempo, btnAccesoHum;
    ToggleButton btnAuto, btnMailIni, btnMailRiego;

    FirebaseDatabase database;
    DatabaseReference DataLimiteHumedad;
    DatabaseReference DataRiegoAuto;
    DatabaseReference DataMailIni;
    DatabaseReference DataMailRiego;
    DatabaseReference DataLimiteRiego;
    DatabaseReference DataMedidas;
    DatabaseReference DataOK;
    DatabaseReference DataDatosRiego;
    DatabaseReference DataRiegoConectado;
    DatabaseReference DataHorarioRiego;

    private Medidas medidas;
    private DHT11 DHT11;
    private Higro Higro;
    private DatosRiego datosRiego;
    private HorarioRiego horarioRiego;
    int tempAmb, humAmb, humSus;
    boolean riegoOn = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTempAmb = findViewById(R.id.txtTempAmb);
        txtHumAmb = findViewById(R.id.txtHumAmb);
        txtLimiteHum = findViewById(R.id.txtLimiteHum);
        txtHumSus = findViewById(R.id.txtHumSus);

        btnAccesoHum = findViewById(R.id.btnAccesoHum);
        btnAccesoTiempo = findViewById(R.id.btnAccesoTiempo);
        txtTiempoRiego = findViewById(R.id.txtTiempoRiego);
        btnAuto = findViewById(R.id.btnAuto);
        btnMailIni = findViewById(R.id.btnMailIni);
        btnMailRiego = findViewById(R.id.btnMailRiego);

        database = FirebaseDatabase.getInstance();
        DataLimiteHumedad = database.getReference("LimiteHumedad");
        DataLimiteRiego = database.getReference("LimiteRiego");
        DataMedidas = database.getReference("Medidas");
        DataRiegoAuto = database.getReference("RiegoAUTO");
        DataMailIni = database.getReference("MailIni");
        DataMailRiego = database.getReference("MailRiego");
        DataOK = database.getReference("OK");
        DataDatosRiego = database.getReference("DatosRiego");
        DataRiegoConectado = database.getReference("RiegoConectado");
        DataHorarioRiego = database.getReference("HorarioRiego");

        btnAccesoTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectTemp();
            }
        });

        btnAccesoHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectHum();
            }
        });

        DataRiegoConectado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                riegoOn = snapshot.getValue(boolean.class);
                if(riegoOn){
                    txtLimiteHum.setTextColor(Color.rgb(0,255,0));
                    txtTiempoRiego.setTextColor(Color.rgb(0,255,0));
                    Toast.makeText(getApplicationContext(), "RIEGO CONECTADO", Toast.LENGTH_SHORT).show();
                }else {
                    txtLimiteHum.setTextColor(Color.rgb(255,0,0));
                    txtTiempoRiego.setTextColor(Color.rgb(255,0,0));
                    Toast.makeText(getApplicationContext(), "RIEGO DESCONECTADO", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DataRiegoAuto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean temp = snapshot.getValue(boolean.class);
                if(temp){
                    btnAuto.setTextColor(-16711936);
                    btnAuto.setChecked(true);
                }else {
                    btnAuto.setTextColor(-65536);
                    btnAuto.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DataMailIni.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Boolean temp = snapshot.getValue(boolean.class);
                if(temp){
                    btnMailIni.setTextColor(-16711936);
                    btnMailIni.setChecked(true);
                }else {
                    btnMailIni.setTextColor(-65536);
                    btnMailIni.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DataMailRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean temp = snapshot.getValue(boolean.class);
                if(temp){
                    btnMailRiego.setTextColor(-16711936);
                    btnMailRiego.setChecked(true);
                }else {
                    btnMailRiego.setTextColor(-65536);
                    btnMailRiego.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DataHorarioRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HorarioRiego horarioRiego = snapshot.getValue(HorarioRiego.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DataDatosRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatosRiego datosRiego = snapshot.getValue(DatosRiego.class);
                if(riegoOn){
                    txtLimiteHum.setText("" + datosRiego.getHumRiego() + "%");
                    txtTiempoRiego.setText("" + datosRiego.getTiempoRiego() + "min.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DataLimiteHumedad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = snapshot.getValue(int.class);
                if(!riegoOn){
                    txtLimiteHum.setText("" + temp + "%");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DataLimiteRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = snapshot.getValue(int.class);
                if(!riegoOn){
                    txtTiempoRiego.setText("" + temp + "min.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DataMedidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Medidas medidas = snapshot.getValue(Medidas.class);
                txtHumAmb.setText("" + medidas.getDHT11().getHumedad() +"%");
                txtTempAmb.setText("" + medidas.getDHT11().getTemperatura() +"ºC");
                txtHumSus.setText("" + medidas.getHigro().getHumedadSuelo() +"%");
                tempAmb = medidas.getDHT11().getTemperatura();
                humAmb = medidas.getDHT11().getHumedad();
                humSus = medidas.getHigro().getHumedadSuelo();

                if (tempAmb <=0 && tempAmb >=-5){
                    txtTempAmb.setTextColor(Color.parseColor("#1da7bb"));
                }else if (tempAmb >0 && tempAmb <=5){
                    txtTempAmb.setTextColor(Color.parseColor("#00a2a5"));
                }else if (tempAmb >5 && tempAmb <=10){
                    txtTempAmb.setTextColor(Color.parseColor("#009c8a"));
                }else if (tempAmb >10 && tempAmb <=15){
                    txtTempAmb.setTextColor(Color.parseColor("#0e956b"));
                }else if (tempAmb >15 && tempAmb <=20){
                    txtTempAmb.setTextColor(Color.parseColor("#378c4b"));
                }else if (tempAmb >20 && tempAmb <=25){
                    txtTempAmb.setTextColor(Color.parseColor("#508129"));
                }else if (tempAmb >25 && tempAmb <=30){
                    txtTempAmb.setTextColor(Color.parseColor("#657500"));
                }else if (tempAmb >30 && tempAmb <=35){
                    txtTempAmb.setTextColor(Color.parseColor("#786600"));
                }else if (tempAmb >35 && tempAmb <=40){
                    txtTempAmb.setTextColor(Color.parseColor("#8a5400"));
                }else if (tempAmb >40 && tempAmb <=45){
                    txtTempAmb.setTextColor(Color.parseColor("#993d00"));
                }else  if (tempAmb >45 && tempAmb <=50){
                    txtTempAmb.setTextColor(Color.parseColor("#a51708"));
                }

                if (humSus >0 && humSus <=10){
                    txtHumSus.setTextColor(Color.parseColor("#3195e3"));
                }else if (humSus >10 && humSus <=20){
                    txtHumSus.setTextColor(Color.parseColor("#0189e2"));
                }else if (humSus >20 && humSus <=30){
                    txtHumSus.setTextColor(Color.parseColor("#007de0"));
                }else if (humSus >30 && humSus <=40){
                    txtHumSus.setTextColor(Color.parseColor("#0071dd"));
                }else if (humSus >40 && humSus <=50){
                    txtHumSus.setTextColor(Color.parseColor("#0064d9"));
                }else if (humSus >50 && humSus <=60){
                    txtHumSus.setTextColor(Color.parseColor("#0057d4"));
                }else if (humSus >60 && humSus <=70){
                    txtHumSus.setTextColor(Color.parseColor("#0049cf"));
                }else if (humSus >7 && humSus <=80){
                    txtHumSus.setTextColor(Color.parseColor("#0039c7"));
                }else if (humSus >80 && humSus <=90){
                    txtHumSus.setTextColor(Color.parseColor("#0028bf"));
                }else if (humSus >90 && humSus <=100){
                    txtHumSus.setTextColor(Color.parseColor("#090db5"));
                }

                if (humAmb >0 && humAmb <=10){
                    txtHumAmb.setTextColor(Color.parseColor("#3195e3"));
                }else if (humAmb >10 && humAmb <=20){
                    txtHumAmb.setTextColor(Color.parseColor("#0189e2"));
                }else if (humAmb >20 && humAmb <=30){
                    txtHumAmb.setTextColor(Color.parseColor("#007de0"));
                }else if (humAmb >30 && humAmb <=40){
                    txtHumAmb.setTextColor(Color.parseColor("#0071dd"));
                }else if (humAmb >40 && humAmb <=50){
                    txtHumAmb.setTextColor(Color.parseColor("#0064d9"));
                }else if (humAmb >50 && humAmb <=60){
                    txtHumAmb.setTextColor(Color.parseColor("#0057d4"));
                }else if (humAmb >60 && humAmb <=70){
                    txtHumAmb.setTextColor(Color.parseColor("#0049cf"));
                }else if (humAmb >7 && humAmb <=80){
                    txtHumAmb.setTextColor(Color.parseColor("#0039c7"));
                }else if (humAmb >80 && humAmb <=90){
                    txtHumAmb.setTextColor(Color.parseColor("#0b17b6"));
                }else if (humAmb >90 && humAmb <=100){
                    txtHumAmb.setTextColor(Color.parseColor("#090db5"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void goToSelectHum() {
        Intent intent2 = new Intent(MainActivity.this, SeleccionHumedadRiego.class);
        startActivity(intent2);
    }

    private void goToSelectTemp() {
        Intent intent = new Intent(MainActivity.this, SeleccionTiempoRiego.class);
        startActivity(intent);
    }

    public void onclick(View view) {
        if (view.getId() == R.id.btnAuto) {
            if (btnAuto.isChecked()) {
                Toast.makeText(getApplicationContext(), "AUTO ON", Toast.LENGTH_SHORT).show();
                DataRiegoAuto.setValue(true);
                DataOK.setValue(true);
            } else {
                Toast.makeText(getApplicationContext(), "AUTO OFF", Toast.LENGTH_SHORT).show();
                DataRiegoAuto.setValue(false);
                DataOK.setValue(true);
            }
        }
        if (view.getId() == R.id.btnMailIni) {
            if (btnMailIni.isChecked()) {
                Toast.makeText(getApplicationContext(), "Envio Mail Activado", Toast.LENGTH_SHORT).show();
                DataMailIni.setValue(true);
                DataOK.setValue(true);
            } else {
                Toast.makeText(getApplicationContext(), "Envio Mail Desactivado", Toast.LENGTH_SHORT).show();
                DataMailIni.setValue(false);
                DataOK.setValue(true);
            }
        }
        if (view.getId() == R.id.btnMailRiego) {
            if (btnMailRiego.isChecked()) {
                Toast.makeText(getApplicationContext(), "Envio Mail Activado", Toast.LENGTH_SHORT).show();
                DataMailRiego.setValue(true);
                DataOK.setValue(true);
            } else {
                Toast.makeText(getApplicationContext(), "Envio Mail Desactivado", Toast.LENGTH_SHORT).show();
                DataMailRiego.setValue(false);
                DataOK.setValue(true);
            }
        }
    }
}
