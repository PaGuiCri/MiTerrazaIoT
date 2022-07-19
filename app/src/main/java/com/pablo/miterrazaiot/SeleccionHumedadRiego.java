package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeleccionHumedadRiego extends AppCompatActivity {

    SeekBar barraHum;
    TextView txtHumRiego, btnEnvioHum;

    FirebaseDatabase database;
    DatabaseReference DataLimiteHumedad;
    DatabaseReference DataOK;
    DatabaseReference DataDatosRiego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_humedad_riego);

        btnEnvioHum = findViewById(R.id.btnEnvioHum);
        barraHum = findViewById(R.id.barraHum);
        txtHumRiego = findViewById(R.id.txtHumRiego);

        database = FirebaseDatabase.getInstance();
        DataLimiteHumedad = database.getReference("LimiteHumedad");
        DataOK = database.getReference("OK");
        DataDatosRiego = database.getReference("DatosRiego");

        DataLimiteHumedad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = snapshot.getValue(int.class);
                    txtHumRiego.setText("" + temp + "%");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnEnvioHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionHumedadRiego.this, "OK", Toast.LENGTH_SHORT).show();
                String tempHum = txtHumRiego.getText().toString();
                Toast.makeText(SeleccionHumedadRiego.this, tempHum, Toast.LENGTH_SHORT).show();
                txtHumRiego.setText(tempHum);
                DataLimiteHumedad.setValue(barraHum.getProgress());
                DataOK.setValue(true);
            }
        });

        barraHum.setMax(100);
        barraHum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtHumRiego.setText(progress + "%");
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