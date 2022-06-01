package com.pablo.miterrazaiot;

public class DatosRiego {

    private int HumRiego;
    private int TiempoRiego;

    public DatosRiego() {
    }

    public DatosRiego(int humriego, int tiempoRiego) {
        HumRiego = humriego;
        TiempoRiego = tiempoRiego;
    }

    public int getHumRiego() {
        return HumRiego;
    }

    public void setHumRiego(int humRiego) {
        HumRiego = humRiego;
    }

    public int getTiempoRiego() {
        return TiempoRiego;
    }

    public void setTiempoRiego(int tiempoRiego) {
        TiempoRiego = tiempoRiego;
    }
}
