package com.pablo.miterrazaiot;

public class Medidas {


    private Higro Higro = new Higro();
    private DHT11 DHT11 = new DHT11();

    public Medidas() {
    }

    public Medidas(com.pablo.miterrazaiot.Higro higro, com.pablo.miterrazaiot.DHT11 DHT11) {
        Higro = higro;
        this.DHT11 = DHT11;
    }

    public com.pablo.miterrazaiot.Higro getHigro() {
        return Higro;
    }

    public void setHigro(com.pablo.miterrazaiot.Higro higro) {
        Higro = higro;
    }

    public com.pablo.miterrazaiot.DHT11 getDHT11() {
        return DHT11;
    }

    public void setDHT11(com.pablo.miterrazaiot.DHT11 DHT11) {
        this.DHT11 = DHT11;
    }
}
