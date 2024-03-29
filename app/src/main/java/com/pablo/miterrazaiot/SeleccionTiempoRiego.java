package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SeleccionTiempoRiego extends AppCompatActivity {

    SeekBar barraTiempo;
    TextView txtLimiteRiego, txtHoraInicioRiego, txtHoraFinRiego, btnEnvioTiempo, btnEnvioHorario, titHoraInicio, titHoraFin;
    int t1Hour, t1Minute, t2Hour, t2Minute;

    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database;
    DatabaseReference DataLimiteRiego;
    DatabaseReference DataOK;
    DatabaseReference DataLimiteHoraInicio;
    DatabaseReference DataLimiteHoraFin;
    DatabaseReference DataDatosRiego;
    DatabaseReference DataRiegoConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_tiempo_riego);

        database = FirebaseDatabase.getInstance();
        DataLimiteRiego = database.getReference(addId("LimiteRiego"));
        DataLimiteHoraInicio = database.getReference(addId("LimiteHoraInicio"));
        DataLimiteHoraFin = database.getReference(addId("LimiteHoraFin"));
        DataOK = database.getReference(addId("OK"));
        DataDatosRiego = database.getReference(addId("DatosRiego"));
        DataRiegoConectado = database.getReference(addId("RiegoConectado"));

        barraTiempo = findViewById(R.id.BarraTiempo);
        btnEnvioTiempo = findViewById(R.id. BtnEnvioTiempo);
        btnEnvioHorario = findViewById(R.id. BtnEnvioHorario);
        txtLimiteRiego = findViewById(R.id. TxtLimiteRiego);
        txtHoraInicioRiego = findViewById(R.id. TxtHoraInicioRiego);
        txtHoraFinRiego = findViewById(R.id. TxtHoraFinRiego);
        titHoraInicio = findViewById(R.id.titHoraInicio);
        titHoraFin = findViewById(R.id.titHoraFin);

        barraTiempo.setMax(120);
        barraTiempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtLimiteRiego.setText(progress + "min.");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnEnvioTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionTiempoRiego.this, "OK", Toast.LENGTH_SHORT).show();
                String tempTiempo = txtLimiteRiego.getText().toString();
                Toast.makeText(SeleccionTiempoRiego.this, tempTiempo, Toast.LENGTH_SHORT).show();
                txtLimiteRiego.setText(tempTiempo);
                DataLimiteRiego.setValue(barraTiempo.getProgress());
            }
        });

        btnEnvioHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = txtHoraInicioRiego.getText().toString();
                String temp1 = txtHoraFinRiego.getText().toString();
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), temp1, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                DataLimiteHoraInicio.setValue(temp);
                DataLimiteHoraFin.setValue(temp1);
                DataOK.setValue(true);
            }
        });

        DataLimiteRiego.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (!Utilities.comprobarCampo(snapshot)) return;
              int temp = snapshot.getValue(int.class);
              txtLimiteRiego.setText("" + temp + "min.");
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
          }
        });

        DataLimiteHoraInicio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!Utilities.comprobarCampo(snapshot)) return;
                String temp = snapshot.getValue(String.class);
                txtHoraInicioRiego.setText("" + temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DataLimiteHoraFin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!Utilities.comprobarCampo(snapshot)) return;
                String temp = snapshot.getValue(String.class);
                txtHoraFinRiego.setText("" + temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @NonNull
    private String addId(String campo) {
        return "/" + id + "/" + campo;
    }

    public void popTimePicker1(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                t1Hour = Hour;
                t1Minute = Minute;
                //BtnHoraInicio.setText(String.format(Locale.getDefault(),"%02d:%02d", T1Hour, T1Minute));
                txtHoraInicioRiego.setText(String.format(Locale.getDefault(),"%02d:%02d", t1Hour, t1Minute));
            }
        };
        //int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, t1Hour, t1Minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimePicker2(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                t2Hour = Hour;
                t2Minute = Minute;
                //BtnHoraFin.setText(String.format(Locale.getDefault(),"%02d:%02d", T2Hour, T2Minute));
                txtHoraFinRiego.setText(String.format(Locale.getDefault(),"%02d:%02d", t2Hour, t2Minute));
            }
        };
        //int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, t1Hour, t1Minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}