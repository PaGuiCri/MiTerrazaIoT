package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Handler entradaBT;
    final int mensajeRecibido = 0;
    private BluetoothAdapter adaptadorBT;
    private BluetoothSocket socketBT;
    private StringBuilder contenidoMensaje = new StringBuilder();
    private static final UUID uuidBT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String direccion;


    TextView TxtHumAmb, TxtTempAmb, TxtLimiteHum, TxtHumSus, TxtTiempoRiego;
    Button BtnAccesoTiempo, BtnAccesoHum;
    ToggleButton BtnAuto, BtnManual;

    FirebaseDatabase database;
    DatabaseReference DataLimiteHumedad;
    DatabaseReference DataRiegoAuto;
    DatabaseReference DataRiegoManual;
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

    int TempAmb, HumAmb, HumSus;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entradaBT = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if(msg.what == mensajeRecibido){
                    String lectura = (String) msg.obj;
                    contenidoMensaje.append(lectura);
                    int finalMensaje = contenidoMensaje.indexOf(".");
                    if(finalMensaje>0){
                        String mensaje = contenidoMensaje.substring(0, finalMensaje);
                        contenidoMensaje.delete(0, contenidoMensaje.length());
                        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        adaptadorBT = BluetoothAdapter.getDefaultAdapter();

        TxtTempAmb = findViewById(R.id. TxtTempAmb);
        TxtHumAmb = findViewById(R.id. TxtHumAmb);
        TxtLimiteHum = findViewById(R.id. TxtLimiteHum);
        TxtHumSus = findViewById(R.id. TxtHumSus);

        BtnAccesoHum = findViewById(R.id. BtnAccesoHum);
        BtnAccesoTiempo = findViewById(R.id.BtnAccesoTiempo);
        TxtTiempoRiego = findViewById(R.id. TxtTiempoRiego);
        BtnAuto = findViewById(R.id.BtnAuto);
        BtnManual = findViewById(R.id. BtnManual);

        database = FirebaseDatabase.getInstance();
        DataLimiteHumedad = database.getReference("LimiteHumedad");
        DataLimiteRiego = database.getReference("LimiteRiego");
        DataMedidas = database.getReference("Medidas");
        DataRiegoAuto = database.getReference("RiegoAUTO");
        DataRiegoManual = database.getReference("RiegoManual");
        DataOK = database.getReference("OK");
        DataDatosRiego = database.getReference("DatosRiego");
        DataRiegoConectado = database.getReference("RiegoConectado");
        DataHorarioRiego = database.getReference("HorarioRiego");

        BtnAccesoTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectTemp();
            }
        });

        BtnAccesoHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectHum();
            }
        });

        DataRiegoConectado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean temp = snapshot.getValue(boolean.class);
                if(temp == true){
                    TxtTiempoRiego.setTextColor(Color.rgb(0,255,0));
                    Toast.makeText(getApplicationContext(), "RIEGO CONECTADO", Toast.LENGTH_SHORT).show();

                }else if(temp == false) {
                    TxtTiempoRiego.setTextColor(Color.rgb(255,0,0));
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
                if(temp == true){
                    BtnAuto.setTextColor(-16711936);
                    BtnAuto.setChecked(true);
                }else if( temp == false) {
                    BtnAuto.setTextColor(-65536);
                    BtnAuto.setChecked(false);
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
                TxtLimiteHum.setText("" + datosRiego.getHumRiego() + "%");
                TxtTiempoRiego.setText("" + datosRiego.getTiempoRiego() + "min.");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DataMedidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Medidas medidas = snapshot.getValue(Medidas.class);
                TxtHumAmb.setText("" + medidas.getDHT11().getHumedad() +"%");
                TxtTempAmb.setText("" + medidas.getDHT11().getTemperatura() +"ºC");
                TxtHumSus.setText("" + medidas.getHigro().getHumedadSuelo() +"%");
                TempAmb = medidas.getDHT11().getTemperatura();
                HumAmb = medidas.getDHT11().getHumedad();
                HumSus = medidas.getHigro().getHumedadSuelo();

                if (TempAmb<=0 && TempAmb>=-5){
                    TxtTempAmb.setTextColor(Color.parseColor("#1da7bb"));
                }else if (TempAmb>0 && TempAmb<=5){
                    TxtTempAmb.setTextColor(Color.parseColor("#00a2a5"));
                }else if (TempAmb>5 && TempAmb<=10){
                    TxtTempAmb.setTextColor(Color.parseColor("#009c8a"));
                }else if (TempAmb>10 && TempAmb<=15){
                    TxtTempAmb.setTextColor(Color.parseColor("#0e956b"));
                }else if (TempAmb>15 && TempAmb<=20){
                    TxtTempAmb.setTextColor(Color.parseColor("#378c4b"));
                }else if (TempAmb>20 && TempAmb<=25){
                    TxtTempAmb.setTextColor(Color.parseColor("#508129"));
                }else if (TempAmb>25 && TempAmb<=30){
                    TxtTempAmb.setTextColor(Color.parseColor("#657500"));
                }else if (TempAmb>30 && TempAmb<=35){
                    TxtTempAmb.setTextColor(Color.parseColor("#786600"));
                }else if (TempAmb>35 && TempAmb<=40){
                    TxtTempAmb.setTextColor(Color.parseColor("#8a5400"));
                }else if (TempAmb>40 && TempAmb<=45){
                    TxtTempAmb.setTextColor(Color.parseColor("#993d00"));
                }else  if (TempAmb>45 && TempAmb<=50){
                    TxtTempAmb.setTextColor(Color.parseColor("#a51708"));
                }

                if (HumSus>0 && HumSus<=10){
                    TxtHumSus.setTextColor(Color.parseColor("#3195e3"));
                }else if (HumSus>10 && HumSus<=20){
                    TxtHumSus.setTextColor(Color.parseColor("#0189e2"));
                }else if (HumSus>20 && HumSus<=30){
                    TxtHumSus.setTextColor(Color.parseColor("#007de0"));
                }else if (HumSus>30 && HumSus<=40){
                    TxtHumSus.setTextColor(Color.parseColor("#0071dd"));
                }else if (HumSus>40 && HumSus<=50){
                    TxtHumSus.setTextColor(Color.parseColor("#0064d9"));
                }else if (HumSus>50 && HumSus<=60){
                    TxtHumSus.setTextColor(Color.parseColor("#0057d4"));
                }else if (HumSus>60 && HumSus<=70){
                    TxtHumSus.setTextColor(Color.parseColor("#0049cf"));
                }else if (HumSus>7 && HumSus<=80){
                    TxtHumSus.setTextColor(Color.parseColor("#0039c7"));
                }else if (HumSus>80 && HumSus<=90){
                    TxtHumSus.setTextColor(Color.parseColor("#0028bf"));
                }else if (HumSus>90 && HumSus<=100){
                    TxtHumSus.setTextColor(Color.parseColor("#090db5"));
                }

                if (HumAmb>0 && HumAmb<=10){
                    TxtHumAmb.setTextColor(Color.parseColor("#3195e3"));
                }else if (HumAmb>10 && HumAmb<=20){
                    TxtHumAmb.setTextColor(Color.parseColor("#0189e2"));
                }else if (HumAmb>20 && HumAmb<=30){
                    TxtHumAmb.setTextColor(Color.parseColor("#007de0"));
                }else if (HumAmb>30 && HumAmb<=40){
                    TxtHumAmb.setTextColor(Color.parseColor("#0071dd"));
                }else if (HumAmb>40 && HumAmb<=50){
                    TxtHumAmb.setTextColor(Color.parseColor("#0064d9"));
                }else if (HumAmb>50 && HumAmb<=60){
                    TxtHumAmb.setTextColor(Color.parseColor("#0057d4"));
                }else if (HumAmb>60 && HumAmb<=70){
                    TxtHumAmb.setTextColor(Color.parseColor("#0049cf"));
                }else if (HumAmb>7 && HumAmb<=80){
                    TxtHumAmb.setTextColor(Color.parseColor("#0039c7"));
                }else if (HumAmb>80 && HumAmb<=90){
                    TxtHumAmb.setTextColor(Color.parseColor("#0b17b6"));
                }else if (HumAmb>90 && HumAmb<=100){
                    TxtHumAmb.setTextColor(Color.parseColor("#090db5"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private BluetoothSocket creacionBluetoothSocket(BluetoothDevice device) throws IOException{
        return device.createRfcommSocketToServiceRecord(uuidBT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent obtenerMAC = getIntent();
        direccion = obtenerMAC.getStringExtra(informacionBT.dispositivoSeleccionado);
        BluetoothDevice device = adaptadorBT.getRemoteDevice(direccion);
        try{
            socketBT = creacionBluetoothSocket(device);
        }catch (IOException e){
        }
        try {
            socketBT.connect();
        }catch (IOException e){
            try {
                Toast.makeText(this, "No fue posible conectar el socket con ese dispositivo", Toast.LENGTH_SHORT).show();
                socketBT.close();
            }catch (IOException e2){
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            socketBT.close();
        }catch (IOException e2){
        }
    }

    public class conexion extends  Thread {
        public InputStream InStream;
        public OutputStream OutStream;

        public conexion(BluetoothSocket socket) {
            try {
                InStream = socketBT.getInputStream();
                OutStream = socketBT.getOutputStream();
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[256];
            int bytes;
            while(true){
                try{
                    bytes = InStream.read(buffer);
                    String Mensaje = new String(buffer, 0, bytes);
                    entradaBT.obtainMessage(mensajeRecibido, Mensaje).sendToTarget();
                }catch (IOException e){
                    break;
                }
            }
        }
        public void write(String input){
            try {
                OutStream.write(input.getBytes());
            }catch (IOException e){
                Toast.makeText(MainActivity.this, "Error envio info", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
        if (view.getId() == R.id.BtnAuto) {
            if (BtnAuto.isChecked()) {
                Toast.makeText(getApplicationContext(), "AUTO ON", Toast.LENGTH_SHORT).show();
                DataRiegoAuto.setValue(true);
                DataOK.setValue(true);
                BtnManual.setClickable(false);
            } else {
                Toast.makeText(getApplicationContext(), "AUTO OFF", Toast.LENGTH_SHORT).show();
                DataRiegoAuto.setValue(false);
                DataOK.setValue(true);
                BtnManual.setClickable(true);
            }
        }
        if(view.getId() == R.id.BtnManual){
            if(BtnManual.isChecked()){
                DataRiegoManual.setValue(true);
                DataOK.setValue(true);
                BtnManual.setTextColor(-16711936);
                Toast.makeText(MainActivity.this, "RIEGO MANUAL ACTIVADO", Toast.LENGTH_SHORT).show();
            }else {
                DataRiegoManual.setValue(false);
                DataOK.setValue(true);
                BtnManual.setTextColor(-65536);
                Toast.makeText(MainActivity.this, "RIEGO MANUAL DESACTIVADO", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
