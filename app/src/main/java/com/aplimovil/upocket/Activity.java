package com.aplimovil.upocket;

public class Activity {
    private String movimiento;
    private String valor;
    private String fecha;
    private int tipo;

    public Activity(String movimiento, String valor, String fecha, int tipo) {
        this.movimiento = movimiento;
        this.valor = valor;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
