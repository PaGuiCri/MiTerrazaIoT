package com.pablo.miterrazaiot;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class informacionBT extends AppCompatActivity {

    ListView Lista;
    BluetoothAdapter adaptadorBT;
    ArrayAdapter dispositivos_sincronizados;
    public static final String dispositivoSeleccionado = "direccionMAC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_bt);

        Lista = findViewById(R.id. Lista);

    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarEstadoBT();
        dispositivos_sincronizados = new ArrayAdapter(this, R.layout.nombres);
        Lista.setAdapter(dispositivos_sincronizados);
        Lista.setOnItemClickListener(seleccionLista);
        Set<BluetoothDevice> dispositivos = adaptadorBT.getBondedDevices();
        if(dispositivos.size()>0){
            for(BluetoothDevice device:dispositivos){
                dispositivos_sincronizados.add(device.getName() + "/n" + device.getAddress());
            }
        }else {
            Toast.makeText(this, "No hay Dispositivos Sincronizados", Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener seleccionLista = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String informacion = ((TextView) view).getText().toString();
            String direccion = informacion.substring(informacion.length() -17);
            Intent cambio = new Intent(informacionBT.this, MainActivity.class);
            cambio.putExtra(dispositivoSeleccionado, direccion);
            startActivity(cambio);
        }
    };
    public void verificarEstadoBT(){
        adaptadorBT = BluetoothAdapter.getDefaultAdapter();
        if(adaptadorBT == null){
            Toast.makeText(this, "Bluetooth no detectado", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            if(adaptadorBT.isEnabled()){

            }else {
                Intent intentoEncenderBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intentoEncenderBT);
            }
        }
    }
}