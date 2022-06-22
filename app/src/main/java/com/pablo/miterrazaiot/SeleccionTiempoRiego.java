package com.pablo.miterrazaiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SeleccionTiempoRiego extends AppCompatActivity {

    SeekBar BarraTiempo;
    TextView TxtLimiteRiego, TxtHoraInicioRiego, TxtHoraFinRiego;
    Button BtnEnvioTiempo, BtnEnvioHorario, BtnHoraInicio, BtnHoraFin;
    int T1Hour, T1Minute, T2Hour, T2Minute;

    FirebaseDatabase database;
    DatabaseReference DataLimiteRiego;
    DatabaseReference DataOK;
    DatabaseReference DataHorarioRiego;
    DatabaseReference DataLimiteHoraInicio;
    DatabaseReference DataLimiteHoraFin;
    DatabaseReference DataDatosRiego;
    DatabaseReference DataRiegoConectado;

    private HorarioRiego horarioRiego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_tiempo_riego);

        database = FirebaseDatabase.getInstance();
        DataLimiteRiego = database.getReference("LimiteRiego");
        DataHorarioRiego = database.getReference("HorarioRiego");
        DataLimiteHoraInicio = database.getReference("LimiteHoraInicio");
        DataLimiteHoraFin = database.getReference("LimiteHoraFin");
        DataOK = database.getReference("OK");
        DataDatosRiego = database.getReference("DatosRiego");
        DataRiegoConectado = database.getReference("RiegoConectado");


        BarraTiempo = findViewById(R.id.BarraTiempo);
        BtnEnvioTiempo = findViewById(R.id. BtnEnvioTiempo);
        BtnEnvioHorario = findViewById(R.id. BtnEnvioHorario);
        TxtLimiteRiego = findViewById(R.id. TxtLimiteRiego);
        TxtHoraInicioRiego = findViewById(R.id. TxtHoraInicioRiego);
        TxtHoraFinRiego = findViewById(R.id. TxtHoraFinRiego);
        BtnHoraInicio = findViewById(R.id. BtnHoraInicio);
        BtnHoraFin = findViewById(R.id. BtnHoraFin);


        BarraTiempo.setMax(120);
        BarraTiempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TxtLimiteRiego.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        BtnEnvioTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionTiempoRiego.this, "OK", Toast.LENGTH_SHORT).show();
                String tempTiempo = TxtLimiteRiego.getText().toString();
                Toast.makeText(SeleccionTiempoRiego.this, tempTiempo, Toast.LENGTH_SHORT).show();
                TxtLimiteRiego.setText(tempTiempo);
                DataLimiteRiego.setValue(BarraTiempo.getProgress());
            }
        });

        BtnEnvioHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = TxtHoraInicioRiego.getText().toString();
                String temp1 = TxtHoraFinRiego.getText().toString();
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), temp1, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                DataLimiteHoraInicio.setValue(temp);
                DataLimiteHoraFin.setValue(temp1);
                DataOK.setValue(true);
            }
        });

        DataDatosRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatosRiego datosRiego = snapshot.getValue(DatosRiego.class);
                TxtLimiteRiego.setText("" + datosRiego.getTiempoRiego() + "min.");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        DataHorarioRiego.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HorarioRiego horarioRiego = snapshot.getValue(HorarioRiego.class);
                TxtHoraInicioRiego.setText("" + horarioRiego.getHoraInicioRiego());
                TxtHoraFinRiego.setText("" + horarioRiego.getHoraFinRiego());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void popTimePicker1(View view){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                T1Hour = Hour;
                T1Minute = Minute;
                //BtnHoraInicio.setText(String.format(Locale.getDefault(),"%02d:%02d", T1Hour, T1Minute));
                TxtHoraInicioRiego.setText(String.format(Locale.getDefault(),"%02d:%02d", T1Hour, T1Minute));
            }
        };
        //int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, T1Hour, T1Minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimePicker2(View view){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                T2Hour = Hour;
                T2Minute = Minute;
                //BtnHoraFin.setText(String.format(Locale.getDefault(),"%02d:%02d", T2Hour, T2Minute));
                TxtHoraFinRiego.setText(String.format(Locale.getDefault(),"%02d:%02d", T2Hour, T2Minute));
            }
        };
        //int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, T1Hour, T1Minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}