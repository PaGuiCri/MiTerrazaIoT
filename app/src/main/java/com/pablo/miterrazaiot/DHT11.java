package com.pablo.miterrazaiot;

public class DHT11 {

    private int Temperatura;
    private int Humedad;


    public DHT11() {
    }

    public DHT11(int temperatura, int humedad) {
        Temperatura = temperatura;
        Humedad = humedad;
    }

    public int getTemperatura() {
        return Temperatura;
    }

    public void setTemperatura(int temperatura) {
        Temperatura = temperatura;
    }

    public int getHumedad() {
        return Humedad;
    }

    public void setHumedad(int humedad) {
        Humedad = humedad;
    }
}
