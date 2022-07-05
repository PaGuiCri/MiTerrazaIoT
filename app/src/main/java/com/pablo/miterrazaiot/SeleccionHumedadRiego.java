package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeleccionHumedadRiego extends AppCompatActivity {


    Button BtnEnvioHum;
    SeekBar BarraHum;
    TextView TxtHumRiego;

    FirebaseDatabase database;
    DatabaseReference DataLimiteHumedad;
    DatabaseReference DataOK;
    DatabaseReference DataDatosRiego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_humedad_riego);

        BtnEnvioHum = findViewById(R.id. BtnEnvioHum);
        BarraHum = findViewById(R.id. BarraHum);
        TxtHumRiego = findViewById(R.id. TxtHumRiego);

        database = FirebaseDatabase.getInstance();
        DataLimiteHumedad = database.getReference("LimiteHumedad");
        DataOK = database.getReference("OK");
        DataDatosRiego = database.getReference("DatosRiego");

        DataLimiteHumedad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = snapshot.getValue(int.class);
                    TxtHumRiego.setText("" + temp + "%");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        BtnEnvioHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionHumedadRiego.this, "OK", Toast.LENGTH_SHORT).show();
                String tempHum = TxtHumRiego.getText().toString();
                Toast.makeText(SeleccionHumedadRiego.this, tempHum, Toast.LENGTH_SHORT).show();
                TxtHumRiego.setText(tempHum);
                DataLimiteHumedad.setValue(BarraHum.getProgress());
                DataOK.setValue(true);
            }
        });

        BarraHum.setMax(100);
        BarraHum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TxtHumRiego.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}