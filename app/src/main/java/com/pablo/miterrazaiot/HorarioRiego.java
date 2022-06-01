package com.pablo.miterrazaiot;

public class HorarioRiego {

    private String HoraInicioRiego;
    private String HoraFinRiego;

    public HorarioRiego() {
    }

    public HorarioRiego(String horaInicioRiego, String horaFinRiego) {
        HoraInicioRiego = horaInicioRiego;
        HoraFinRiego = horaFinRiego;
    }

    public String getHoraInicioRiego() {
        return HoraInicioRiego;
    }

    public void setHoraInicioRiego(String horaInicioRiego) {
        HoraInicioRiego = horaInicioRiego;
    }

    public String getHoraFinRiego() {
        return HoraFinRiego;
    }

    public void setHoraFinRiego(String horaFinRiego) {
        HoraFinRiego = horaFinRiego;
    }
}
