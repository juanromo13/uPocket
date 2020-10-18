package com.aplimovil.upocket;

public class Goal {
    private String meta;
    private int precio;
    private int restante;

    public Goal(String deseo, int precio, int restante) {
        this.meta = deseo;
        this.precio = precio;
        this.restante = restante;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getRestante() {
        return restante;
    }

    public void setRestante(int restante) {
        this.restante = restante;
    }
}
